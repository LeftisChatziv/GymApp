package com.example.myapplication.data.local.converter

import androidx.room.TypeConverter
import com.example.myapplication.data.local.entity.MuscleActivation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MuscleConverters {

    private val gson = Gson()

    @TypeConverter
    fun fromList(list: List<MuscleActivation>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(data: String): List<MuscleActivation> {
        val type = object : TypeToken<List<MuscleActivation>>() {}.type
        return gson.fromJson(data, type)
    }
}