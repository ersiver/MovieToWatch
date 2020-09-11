package com.breiter.movietowatchapp.util

import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.network.GenreDTO
import com.breiter.movietowatchapp.data.network.GenreResponseDTO
import com.breiter.movietowatchapp.data.network.MovieDTO

object TestUtil {

    /**
     * Helper function to create instance of Movie with
     * given title and id parameters. Used across app tests.
     */
    fun createMovie(id: Int, title: String): Movie = Movie(
        id = id,
        title = title,
        overview = "During the 1980s, a failed stand-up comedian is driven insane and turns to a life of crime and chaos in Gotham City while becoming an infamous psychopathic crime figure.",
        rating = 8.2,
        posterUrl = "udDclJoHjfjb8Ekgsd4FDteOkCU.jpg",
        language = "en",
        releaseDate = "2019",
        genreIds = listOf(80, 18, 53)
    )

    /**
     * Helper function to create instance of Move. Used across app tests.
     */
    fun createMovie(): Movie = createMovie(475557, "Joker")


    /**
     * Helper function to create GenreResponse used in
     * FakeService for Repository testing purpose.
     */
    fun createGenres(): GenreResponseDTO {
        return GenreResponseDTO(
            listOf(
                GenreDTO(18, "Drama"),
                GenreDTO(53, "Thriller"),
                GenreDTO(80, "Crime"),
                GenreDTO(12, "Adventure"),
                GenreDTO(35, "Comedy")
            )
        )
    }

    /**
     * Helper function to create list of MovieDTO used in FakeService
     * for Repository testing purpose.
     * Intentionally given two items' titles of TITLE1.
     */
    fun apiMovies(): List<MovieDTO> {
        val movie1 = movieDTO(1, "TITLE1")
        val movie2 = movieDTO(2, "TITLE2")
        val movie3 = movieDTO(3, "TITLE3")
        val movie4 = movieDTO(4, "TITLE1")

        return listOf(movie1, movie2, movie3, movie4)
    }

    /**
     * Helper function to create instance of MovieDTO.
     */
    private fun movieDTO(id: Int, title: String): MovieDTO = MovieDTO(
        id = id,
        title = title,
        overview = "During the 1980s, a failed stand-up comedian is driven insane and turns to a life of crime and chaos in Gotham City while becoming an infamous psychopathic crime figure.",
        rating = 8.2,
        posterUrl = "udDclJoHjfjb8Ekgsd4FDteOkCU.jpg",
        language = "en",
        releaseDate = "2019",
        genreIds = listOf(80, 18, 53)
    )

}
