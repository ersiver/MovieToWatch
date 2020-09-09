package com.breiter.movietowatchapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

//    companion object {
//        @Volatile
//        private var INSTANCE: MovieDatabase? = null
//
//        fun getInstance(context: Context): MovieDatabase {
//            synchronized(this) {
//                var instance = INSTANCE
//
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        MovieDatabase::class.java,
//                        "movie_database"
//                    )
//                        .fallbackToDestructiveMigration()
//                        .build()
//                    INSTANCE = instance
//                }
//                return instance
//            }
//        }
//    }
}
