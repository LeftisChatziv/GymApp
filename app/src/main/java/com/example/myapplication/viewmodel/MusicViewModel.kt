package com.example.myapplication.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.myapplication.Music.Song
import com.example.myapplication.Music.MusicPlayerManager

class MusicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = com.example.myapplication.Music.MusicRepository(
        application.applicationContext
    )

    // 🎵 SONGS
    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    // ⏱ PROGRESS
    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration

    // 🎧 PLAYER STATE (IMPORTANT FIX)
    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong: StateFlow<Song?> = _currentSong

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying

    private var currentIndex = 0
    private var progressJob: Job? = null

    init {
        loadSongs()
    }

    fun loadSongs() {
        viewModelScope.launch {
            _songs.value = repository.getSongs()
        }
    }

    fun playSong(song: Song, context: Context) {

        val list = _songs.value
        currentIndex = list.indexOf(song).coerceAtLeast(0)

        _currentSong.value = song
        _isPlaying.value = true

        MusicPlayerManager.play(context, song.uri)

        _duration.value = MusicPlayerManager.getDuration()
        _currentPosition.value = 0

        startProgressUpdates()
    }

    fun pause() {
        MusicPlayerManager.pause()
        _isPlaying.value = false
        stopProgressUpdates()
    }

    fun resume() {
        MusicPlayerManager.resume()
        _isPlaying.value = true
        startProgressUpdates()
    }

    fun next(context: Context) {
        val list = _songs.value
        if (list.isEmpty()) return

        currentIndex = (currentIndex + 1) % list.size
        playSong(list[currentIndex], context)
    }

    fun previous(context: Context) {
        val list = _songs.value
        if (list.isEmpty()) return

        currentIndex =
            if (currentIndex - 1 < 0) list.size - 1
            else currentIndex - 1

        playSong(list[currentIndex], context)
    }

    fun seekTo(position: Int) {
        MusicPlayerManager.seekTo(position)
        _currentPosition.value = position
    }

    private fun startProgressUpdates() {
        progressJob?.cancel()

        progressJob = viewModelScope.launch {
            while (true) {

                _currentPosition.value = MusicPlayerManager.getPosition()
                _duration.value = MusicPlayerManager.getDuration()

                delay(500)
            }
        }
    }

    private fun stopProgressUpdates() {
        progressJob?.cancel()
        progressJob = null
    }

    override fun onCleared() {
        super.onCleared()
        MusicPlayerManager.release()
        progressJob?.cancel()
    }
}