package com.example.myapplication.Music

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object MusicPlayerManager {

    private var mediaPlayer: MediaPlayer? = null

    fun play(context: Context, uri: Uri) {

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }

        mediaPlayer?.apply {
            try {
                reset()
                setDataSource(context, uri)

                prepare() // OK για μικρά local files
                start()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun resume() {
        mediaPlayer?.start()
    }

    fun seekTo(pos: Int) {
        mediaPlayer?.seekTo(pos)
    }

    fun getPosition(): Int = mediaPlayer?.currentPosition ?: 0

    fun getDuration(): Int = mediaPlayer?.duration ?: 0

    fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}