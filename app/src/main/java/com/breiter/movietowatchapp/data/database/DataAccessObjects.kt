package com.breiter.movietowatchapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(databaseMovie: DatabaseMovie)

    @Delete
    fun delete(databaseMovie: DatabaseMovie)

    @Query("SELECT * FROM saved_movies_table")
    fun getSavedMovies(): LiveData<List<DatabaseMovie>>

    @Query("SELECT COUNT(*) FROM saved_movies_table WHERE movie_id = :id")
    fun moviesCount(id: Int): Int
}

@Dao
interface GenreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(genres: List<DatabaseGenre>)

    @Query("SELECT genre_name FROM genres_table WHERE genre_id IN (:idList)")
    fun getGenres(idList: List<Int>): LiveData<List<String>>
}