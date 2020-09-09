/*
 * May-2020
 */

package com.breiter.movietowatchapp

import android.app.Application
import androidx.work.*
import com.breiter.movietowatchapp.data.repository.IMovieRepository
import com.breiter.movietowatchapp.data.util.Constants
import com.breiter.movietowatchapp.data.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit


class MovieToWatchApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    val movieRepository: IMovieRepository
        get() = ServiceLocator.provideRepository(this)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
        setupRecurringWork()
    }

    private fun delayedInit() {
        applicationScope.launch {
            Timber.plant(Timber.DebugTree())
        }
    }

    /**
     * WorkManager background job to fetch genre network data weekly.
     */
    private fun setupRecurringWork() {

        val constrains = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .build()

        val repeatingRequest =
            PeriodicWorkRequestBuilder<RefreshDataWorker>(7, TimeUnit.DAYS)
                .setConstraints(constrains)
                .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            Constants.DataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}