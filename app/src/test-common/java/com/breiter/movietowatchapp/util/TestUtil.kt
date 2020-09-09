package com.breiter.movietowatchapp.util

import com.breiter.movietowatchapp.data.domain.Movie

object TestUtil {
    fun createMovie(id: Int, title: String): Movie = Movie(
        id = id,
        title = title,
        overview = "Test overview",
        rating = 8.2,
        posterUrl = "Test url",
        language = "Test language",
        releaseDate = "Test date",
        genreIds = listOf(1)
    )
}