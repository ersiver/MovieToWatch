package com.breiter.movietowatchapp.data.work

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.ListenableWorker.Result
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class RefreshDataWorkerTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun refreshDataFromNetworkTest() {
        val worker = TestListenableWorkerBuilder<RefreshDataWorker>(context).build()

        val result = worker.startWork().get()

        assertThat(result, `is` (Result.success()))
    }
}