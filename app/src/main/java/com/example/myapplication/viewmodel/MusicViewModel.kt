package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.myapplication.Music.Song
import com.example.myapplication.Music.MusicPlayer
import com.example.myapplication.Music.MusicRepository
class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MusicRepository(application.applicationContext)
    private val player = MusicPlayer()

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    private var currentIndex = 0

    init {
        loadSongs()
    }

    fun loadSongs() {
        viewModelScope.launch {
            _songs.value = repository.getSongs()
        }
    }

    fun playSong(song: Song, context: android.content.Context) {
        player.play(context, song.uri)
    }

    fun pause() = player.pause()
    fun resume() = player.resume()

    fun next(context: android.content.Context) {
        val list = _songs.value
        if (list.isEmpty()) return

        currentIndex = (currentIndex + 1) % list.size
        playSong(list[currentIndex], context)
    }

    fun previous(context: android.content.Context) {
        val list = _songs.value
        if (list.isEmpty()) return

        currentIndex = if (currentIndex - 1 < 0) list.size - 1 else currentIndex - 1
        playSong(list[currentIndex], context)
    }
}