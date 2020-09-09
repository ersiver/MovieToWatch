package com.breiter.movietowatchapp.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.breiter.movietowatchapp.MovieToWatchApplication
import retrofit2.HttpException

/**
 * WorkManager background job to fetch genre network data weekly.
 */
class RefreshDataWorker(val appContext: Context, params: WorkerParameters) : CoroutineWorker(
    appContext, params
) {
    override suspend fun doWork(): Result {
        val app = appContext.applicationContext as MovieToWatchApplication
        val repository = app.movieRepository

        try {
            repository.getGenresFromNetwork()
        } catch (e: HttpException) {
            return Result.retry()
        }

        return Result.success()
    }
}
