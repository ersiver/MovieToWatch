package com.breiter.movietowatchapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_movies_table")
data class DatabaseMovie(
    @PrimaryKey @ColumnInfo(name = "movie_id") val id: Int,
    @ColumnInfo(name = "movie_title") val title: String? = null,
    @ColumnInfo(name = "movie_overview") val overview: String? = null,
    @ColumnInfo(name = "movie_rating") val rating: Double? = null,
    @ColumnInfo(name = "movie_poster_url") val posterUrl: String?= null,
    @ColumnInfo(name = "movie_language") val language: String? = null,
    @ColumnInfo(name = "movie_release_date") val releaseDate: String? = null,
    @ColumnInfo(name = "movie_genres") val genreIds: List<Int>? = null
)

@Entity(tableName = "genres_table")
data class DatabaseGenre(
    @PrimaryKey @ColumnInfo(name = "genre_id") val id: Int,
    @ColumnInfo(name = "genre_name") val name: String? = null
)

