package com.breiter.movietowatchapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.breiter.movietowatchapp.data.domain.Movie

interface IMovieRepository {
    val _searchedMovies: MutableLiveData<List<Movie>>
    val searchedMovies: LiveData<List<Movie>>
    val _isSavedMovie: MutableLiveData<Boolean>
    val isSavedMovie: LiveData<Boolean>

    suspend fun insert(movie: Movie)

    suspend fun delete(movie: Movie)

    suspend fun setSaved(movie: Movie)

    fun getSavedMovies(): LiveData<List<Movie>>

    fun getGenresNames(ids: List<Int>): LiveData<List<String>>

    suspend fun getMovies(title: String)

    suspend fun getGenresFromNetwork()
}