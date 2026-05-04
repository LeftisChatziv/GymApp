package com.example.myapplication.Music

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

class MusicPlayer {

    private var mediaPlayer: MediaPlayer? = null

    fun play(context: Context, uri: Uri) {

        mediaPlayer?.release()

        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, uri)
            prepare()
            start()
        }
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun resume() {
        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}