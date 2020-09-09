package com.breiter.movietowatchapp.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.breiter.movietowatchapp.MovieToWatchApplication
import retrofit2.HttpException
import timber.log.Timber

/**
 * WorkManager background job to fetch genre network data weekly.
 */
class RefreshDataWorker(val appContext: Context, params: WorkerParameters) : CoroutineWorker(
    appContext, params
) {
    override suspend fun doWork(): Result {
        Timber.i("Work request started.")

        val app = appContext.applicationContext as MovieToWatchApplication
        val repository = app.movieRepository

        try {
            repository.getGenresFromNetwork()
            Timber.i("Work request succeed.")
        } catch (e: HttpException) {
            Timber.i("Work request failed. ${e.message()}")
            return Result.retry()
        }

        return Result.success()
    }
}