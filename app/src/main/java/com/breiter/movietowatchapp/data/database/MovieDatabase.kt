package com.breiter.movietowatchapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.breiter.movietowatchapp.data.util.DataConverter

@Database(
    entities = [DatabaseMovie::class, DatabaseGenre::class], version = 1, exportSchema = false
)
@TypeConverters(DataConverter::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
    abstract val genreDao: GenreDao
}
