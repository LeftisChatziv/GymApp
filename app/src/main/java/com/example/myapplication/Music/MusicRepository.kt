package com.example.myapplication.Music

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore

class MusicRepository(private val context: Context) {

    fun getSongs(): List<Song> {

        val songs = mutableListOf<Song>()

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST
        )

        // ✅ FILTER (VERY IMPORTANT)
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

        context.contentResolver.query(
            uri,
            projection,
            selection,
            null,
            sortOrder
        )?.use { cursor ->

            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

            while (cursor.moveToNext()) {

                val id = cursor.getLong(idCol)
                val title = cursor.getString(titleCol) ?: "Unknown"
                val artist = cursor.getString(artistCol) ?: "Unknown"

                val songUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                songs.add(Song(id, title, artist, songUri))
            }
        }

        return songs
    }
}