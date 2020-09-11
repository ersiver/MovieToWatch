package com.breiter.movietowatchapp.data.repository

import com.breiter.movietowatchapp.data.network.GenreResponseDTO
import com.breiter.movietowatchapp.data.network.MovieDTO
import com.breiter.movietowatchapp.data.network.MovieResponseDTO
import com.breiter.movietowatchapp.data.network.MovieService
import com.breiter.movietowatchapp.util.TestUtil
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import org.junit.Test

class FakeService : MovieService {

    override fun getMoviesAsync(
        apiKey: String,
        title: String,
        page: Int
    ): Deferred<MovieResponseDTO> {

        val apiMovies = TestUtil.apiMovies()
        val moviesMatchingTitles :MutableList<MovieDTO> = mutableListOf()

        for(movie in apiMovies){
            if(movie.title == title)
                moviesMatchingTitles.add(movie)
        }

        val movieResponse = MovieResponseDTO(moviesMatchingTitles)
        return CompletableDeferred(movieResponse)
    }

    override fun getGenresAsync(apiKey: String): Deferred<GenreResponseDTO> {
       val genreResponse = TestUtil.createGenres()
        return CompletableDeferred(genreResponse)
    }
}