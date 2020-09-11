package com.breiter.movietowatchapp.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.repository.IMovieRepository
import kotlinx.coroutines.runBlocking

/**
 * Implementation of a data source with static access to the data for easy testing.
 */
class FakeRepository : IMovieRepository {
    var moviesFakeData: LinkedHashMap<Int, Movie> = LinkedHashMap()
    var genresFakeData: LinkedHashMap<Int, String> = LinkedHashMap()

    override val _searchedMovies = MutableLiveData<List<Movie>>()
    override val searchedMovies: LiveData<List<Movie>>
        get() = _searchedMovies

    override val _isSavedMovie = MutableLiveData<Boolean>()
    override val isSavedMovie: LiveData<Boolean>
        get() = _isSavedMovie


    override suspend fun insert(movie: Movie) {
        moviesFakeData[movie.id] = movie
        _isSavedMovie.postValue(true)
    }

    override suspend fun delete(movie: Movie) {
        moviesFakeData.remove(movie.id)
        _isSavedMovie.postValue(false)
    }

    override suspend fun setSaved(movie: Movie) {
        val isSaved = moviesFakeData.containsKey(movie.id)
        _isSavedMovie.postValue(isSaved)
    }

    override fun getSavedMovies(): LiveData<List<Movie>> {
        val savedMovies = MutableLiveData<List<Movie>>()
        val movieSource = moviesFakeData.values.toList()
        savedMovies.value = movieSource
        return savedMovies
    }

    override fun getGenresNames(ids: List<Int>): LiveData<List<String>> {
        val genreLiveData = MutableLiveData<List<String>>()
        genreLiveData.value = genresFakeData.values.toList()
        return genreLiveData
    }

    override suspend fun getMovies(title: String) {
        val movie1 = TestUtil.createMovie(1, title)

        val list = listOf(movie1)
        _searchedMovies.value = list
    }

    override suspend fun getGenresFromNetwork() {
        genresFakeData[0] = "drama"
        genresFakeData[1] = "comedy"
        genresFakeData[2] = "romanse"
        genresFakeData[3] = "sci-fi"
    }

    suspend fun add(vararg movies: Movie) {
        for (movie in movies) {
            insert(movie)
        }
    }
}