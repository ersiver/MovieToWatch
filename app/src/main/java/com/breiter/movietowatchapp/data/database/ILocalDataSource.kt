package com.breiter.movietowatchapp.data.database

import androidx.lifecycle.LiveData
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.network.GenreResponseDTO

interface ILocalDataSource {

    suspend fun insert(movie: Movie)

    suspend fun delete(movie: Movie)

    suspend fun isSaved(movie: Movie): Boolean

    suspend fun insertAll(genres: GenreResponseDTO)

    fun getSavedMovies(): LiveData<List<Movie>>

    fun getGenresNames(ids: List<Int>): LiveData<List<String>>
}