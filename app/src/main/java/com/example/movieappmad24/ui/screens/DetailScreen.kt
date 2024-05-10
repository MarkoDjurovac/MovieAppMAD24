package com.example.movieappmad24.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.example.movieappmad24.R
import com.example.movieappmad24.data.MovieDatabase
import com.example.movieappmad24.data.MovieRepository
import com.example.movieappmad24.viewmodels.DetailScreenViewModel
import com.example.movieappmad24.viewmodels.ViewModelFactory
import com.example.movieappmad24.ui.widgets.HorizontalScrollableImageView
import com.example.movieappmad24.ui.widgets.MovieRow
import com.example.movieappmad24.ui.widgets.SimpleTopAppBar

@Composable
fun DetailScreen(
    movieId: Long,
    navController: NavController,
) {
    val db = MovieDatabase.getDatabase(LocalContext.current)
    val repository = MovieRepository(movieDao = db.movieDao(), movieImageDao = db.movieImageDao())
    val factory = ViewModelFactory(repository, movieId)
    val viewModel: DetailScreenViewModel = viewModel(factory = factory)

    val movie by viewModel.movie.collectAsState()

    Scaffold (
        topBar = {
            SimpleTopAppBar(title = movie.movie.title) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            }
        }
    ){ innerPadding ->
        Column {
            MovieRow(
                modifier = Modifier.padding(innerPadding),
                movie = movie,
                onFavoriteClick = { _ -> viewModel.toggleFavoriteMovie() }
                )

            Divider(modifier = Modifier.padding(4.dp))

            Column {
                Text("Movie Trailer")
                VideoPlayer(trailerURL = movie.movie.trailer)
            }

            Divider(modifier = Modifier.padding(4.dp))

            HorizontalScrollableImageView(movie = movie)
        }
    }
}

@Composable
fun VideoPlayer(trailerURL: String){

    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }

    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(
                "android.resource://${context.packageName}/${R.raw.trailer_placeholder}"
            ))
            prepare()
            playWhenReady = true
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            exoPlayer.release()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16 / 9f),
        factory = {
            PlayerView(context).also { playerView ->
                playerView.player = exoPlayer
            }
        },
        update = {
            when(lifecycle) {
                Lifecycle.Event.ON_RESUME -> {
                    it.onResume()
                    //it.player?.play()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    it.onPause()
                    it.player?.pause()
                }
                else -> Unit
            }
        }
    )
}