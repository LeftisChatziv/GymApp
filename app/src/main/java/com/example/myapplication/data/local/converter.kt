package com.example.myapplication.data.local.converter

import androidx.room.TypeConverter
import com.example.myapplication.data.local.entity.MuscleActivation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MuscleConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromList(list: List<MuscleActivation>?): String {
        return gson.toJson(list ?: emptyList<MuscleActivation>())
    }

    @TypeConverter
    fun toList(data: String?): List<MuscleActivation> {
        if (data.isNullOrBlank()) return emptyList()

        return try {
            val type = object : TypeToken<List<MuscleActivation>>() {}.type

            @Suppress("UNCHECKED_CAST")
            gson.fromJson<List<MuscleActivation>>(data, type)

        } catch (e: Exception) {
            emptyList()
        }
    }
}