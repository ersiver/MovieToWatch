package com.breiter.movietowatchapp.data.util

import com.breiter.movietowatchapp.data.database.DatabaseGenre
import com.breiter.movietowatchapp.data.database.DatabaseMovie
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.network.GenreResponseDTO
import com.breiter.movietowatchapp.data.network.MovieResponseDTO


/**
 * Mapping DTO to domain and database objects
 */
fun MovieResponseDTO.asDomainModel(): List<Movie> {
    return moviesDTO.map {
        Movie(
            id = it.id,
            title = it.title,
            overview = it.overview,
            rating = it.rating,
            posterUrl = it.posterUrl,
            language = it.language,
            releaseDate = it.releaseDate,
            genreIds = it.genreIds
        )
    }
}

fun GenreResponseDTO.asDatabaseModel(): List<DatabaseGenre> {
    return genresDTO.map {
        DatabaseGenre(
            id = it.id,
            name = it.name
        )
    }
}

/**
 * Mapping domain Movie object to database Movie object
 */
fun Movie.asDatabaseModel(): DatabaseMovie {
    return DatabaseMovie(
        id = id,
        title = title,
        overview = overview,
        rating = rating,
        posterUrl = posterUrl,
        language = language,
        releaseDate = releaseDate,
        genreIds = genreIds
    )
}

/**
 * Mapping database Movie lists do domain objects
 */
fun List<DatabaseMovie>.asDomainModel(): List<Movie> {
    return map() {
        Movie(
            id = it.id,
            title = it.title,
            overview = it.overview,
            rating = it.rating,
            posterUrl = it.posterUrl,
            language = it.language,
            releaseDate = it.releaseDate,
            genreIds = it.genreIds
        )
    }
}

