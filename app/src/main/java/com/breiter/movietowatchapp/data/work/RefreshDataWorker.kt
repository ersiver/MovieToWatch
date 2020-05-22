package com.breiter.movietowatchapp.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.breiter.movietowatchapp.data.database.MovieDatabase
import com.breiter.movietowatchapp.data.network.RetrofitClient
import com.breiter.movietowatchapp.data.repository.MovieRepository
import retrofit2.HttpException

/**
 * WorkManager background job to fetch genre network data weekly.
 */
class RefreshDataWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(
    appContext, params
) {
    override suspend fun doWork(): Result {
        val movieDatabase = MovieDatabase.getInstance(applicationContext)
        val repository = MovieRepository(movieDatabase, RetrofitClient())

        try {
            repository.getGenresFromNetwork()
        } catch (e: HttpException) {
            return Result.retry()
        }

        return Result.success()
    }
}