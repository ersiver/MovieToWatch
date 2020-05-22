package com.breiter.movietowatchapp.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * DTO are responsible for parsing responses from network.
 */

@JsonClass(generateAdapter = true)
data class MovieResponseDTO(@Json(name = "results") val moviesDTO: List<MovieDTO>)

@JsonClass(generateAdapter = true)
data class MovieDTO(
    val id: Int,
    val title: String?,
    val overview: String,
    @Json(name = "vote_average") val rating: Double?,
    @Json(name = "poster_path") val posterUrl: String?,
    @Json(name = "original_language") val language: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "genre_ids") val genreIds: List<Int>?
)

@JsonClass(generateAdapter = true)
data class GenreResponseDTO(@Json(name = "genres") val genresDTO: List<GenreDTO>)

@JsonClass(generateAdapter = true)
data class GenreDTO(val id: Int, val name: String)



