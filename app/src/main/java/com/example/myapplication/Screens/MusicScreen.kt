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

    var currentSong by remember { mutableStateOf<Song?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎧 My Music") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text("←") // μπορείς να το αλλάξεις σε Icon
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

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(songs) { song ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            currentSong = song
                            viewModel.playSong(song, context)
                            isPlaying = true
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

            Spacer(modifier = Modifier.height(16.dp))

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
                        if (isPlaying) viewModel.pause() else viewModel.resume()
                        isPlaying = !isPlaying
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