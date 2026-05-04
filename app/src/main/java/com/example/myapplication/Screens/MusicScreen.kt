package com.example.myapplication.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "🎧 My Music") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text(text = "←")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(songs) { song ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.playSong(song, context)
                        }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = song.title)
                            Text(
                                text = song.artist,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🎚 PROGRESS BAR
            if (currentSong != null && duration > 0) {

                val progress =
                    (currentPosition / duration.toFloat()).coerceIn(0f, 1f)

                Column {

                    Slider(
                        value = progress,
                        onValueChange = { newValue ->
                            viewModel.seekTo((newValue * duration).toInt())
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = formatMusicTime(currentPosition))
                        Text(text = formatMusicTime(duration))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // 🎮 CONTROLS
            Card {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Button(onClick = {
                        viewModel.previous(context)
                    }) {
                        Text(text = "⏮")
                    }

                    Button(onClick = {
                        if (isPlaying) {
                            viewModel.pause()
                        } else {
                            viewModel.resume()
                        }
                    }) {
                        Text(text = if (isPlaying) "Pause" else "Play")
                    }

                    Button(onClick = {
                        viewModel.next(context)
                    }) {
                        Text(text = "⏭")
                    }
                }
            }
        }
    }
}

// ⏱ FORMAT
fun formatMusicTime(ms: Int): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}