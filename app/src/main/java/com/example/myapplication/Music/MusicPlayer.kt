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

    // 🔥 ΑΥΤΑ ΕΛΕΙΠΑΝ
    fun getDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}