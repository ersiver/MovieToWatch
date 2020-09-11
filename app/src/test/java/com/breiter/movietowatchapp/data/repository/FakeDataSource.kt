package com.breiter.movietowatchapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.breiter.movietowatchapp.data.database.DatabaseGenre
import com.breiter.movietowatchapp.data.database.GenreDao
import com.breiter.movietowatchapp.data.database.ILocalDataSource
import com.breiter.movietowatchapp.data.domain.Movie
import com.breiter.movietowatchapp.data.network.GenreDTO
import com.breiter.movietowatchapp.data.network.GenreResponseDTO
import com.breiter.movietowatchapp.util.TestUtil
import org.junit.Test

class FakeDataSource() : ILocalDataSource {

    private val movie1 = TestUtil.createMovie(1,"TITLE1")
    private val movie2 = TestUtil.createMovie(2,"TITLE2")
    private val movie3 = TestUtil.createMovie(3,"TITLE3")
    val movies: MutableList<Movie> = mutableListOf(movie1, movie2, movie3)

    private val genreList: MutableList<GenreResponseDTO> = mutableListOf()

    override suspend fun insert(movie: Movie) {
        movies.add(movie)
    }

    override suspend fun delete(movie: Movie) {
        movies.remove(movie)
    }

    override suspend fun isSaved(movie: Movie): Boolean {
        return movies.contains(movie)
    }

    override suspend fun insertAll(genres: GenreResponseDTO) {
       genreList.add(genres)
    }

    override fun getSavedMovies(): LiveData<List<Movie>> {
        val savedData = MutableLiveData<List<Movie>>()
        savedData.value = movies
        return savedData
    }

    override fun getGenresNames(ids: List<Int>): LiveData<List<String>> {
        TODO()
    }
}

