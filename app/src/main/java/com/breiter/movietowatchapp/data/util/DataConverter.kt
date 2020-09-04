package com.breiter.movietowatchapp.data.util

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*


class DataConverter {
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val jsonAdapter: JsonAdapter<List<Int>> =
        moshi.adapter<List<Int>>(Types.newParameterizedType(List::class.java, Integer::class.java))
            .nonNull()

    @TypeConverter
    fun genreListToString(genreIds: List<Int>?): String {
        if (genreIds == null)
            return jsonAdapter.toJson(Collections.emptyList<Int>())

        return jsonAdapter.toJson(genreIds)
    }

    @TypeConverter
    fun stringToGenreList(json: String?): List<Int>? {
        if (json == null)
            return Collections.emptyList()

        return jsonAdapter.fromJson(json)
    }
}