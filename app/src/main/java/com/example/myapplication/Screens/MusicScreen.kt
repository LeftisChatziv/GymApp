package com.example.myapplication.Screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.Music.Song
import com.example.myapplication.viewmodel.MusicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(
    viewModel: MusicViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {

    val context = LocalContext.current

    val songs by viewModel.songs.collectAsState()
    val currentPosition by viewModel.currentPosition.collectAsState()
    val duration by viewModel.duration.collectAsState()
    val currentSong by viewModel.currentSong.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎧 My Music") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->

        if (isLandscape) {

            // ================= LANDSCAPE =================
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // 🎵 LEFT: SONG LIST
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(songs) { song ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { viewModel.playSong(song, context) }
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text(song.title)
                                Text(
                                    song.artist,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                // 🎮 RIGHT: CONTROLS
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {

                    if (currentSong != null && duration > 0) {

                        val progress =
                            (currentPosition / duration.toFloat()).coerceIn(0f, 1f)

                        Slider(
                            value = progress,
                            onValueChange = {
                                viewModel.seekTo((it * duration).toInt())
                            }
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(formatMusicTime(currentPosition))
                            Text(formatMusicTime(duration))
                        }

                        Spacer(Modifier.height(24.dp))
                    }

                    Card {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {

                            Button(onClick = { viewModel.previous(context) }) {
                                Text("⏮")
                            }

                            Button(onClick = {
                                if (isPlaying) viewModel.pause()
                                else viewModel.resume()
                            }) {
                                Text(if (isPlaying) "Pause" else "Play")
                            }

                            Button(onClick = { viewModel.next(context) }) {
                                Text("⏭")
                            }
                        }
                    }
                }
            }

        } else {

            // ================= PORTRAIT =================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(songs) { song ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { viewModel.playSong(song, context) }
                        ) {
                            Column(Modifier.padding(12.dp)) {
                                Text(song.title)
                                Text(
                                    song.artist,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                if (currentSong != null && duration > 0) {

                    val progress =
                        (currentPosition / duration.toFloat()).coerceIn(0f, 1f)

                    Column {

                        Slider(
                            value = progress,
                            onValueChange = {
                                viewModel.seekTo((it * duration).toInt())
                            }
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(formatMusicTime(currentPosition))
                            Text(formatMusicTime(duration))
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                }

                Card {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Button(onClick = { viewModel.previous(context) }) {
                            Text("⏮")
                        }

                        Button(onClick = {
                            if (isPlaying) viewModel.pause()
                            else viewModel.resume()
                        }) {
                            Text(if (isPlaying) "Pause" else "Play")
                        }

                        Button(onClick = { viewModel.next(context) }) {
                            Text("⏭")
                        }
                    }
                }
            }
        }
    }
}

fun formatMusicTime(ms: Int): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}