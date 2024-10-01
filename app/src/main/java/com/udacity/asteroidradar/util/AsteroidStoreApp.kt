package com.udacity.asteroidradar.util

import android.app.Application
import android.os.Build
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.udacity.asteroidradar.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AsteroidStoreApp : Application() {

    val applicationScope = CoroutineScope(Dispatchers.Default)

    companion object {
        @Volatile
        private var mAsteroidAppInstance: AsteroidStoreApp? = null

        fun getInstance(): AsteroidStoreApp? {
            if (mAsteroidAppInstance == null) {
                synchronized(AsteroidStoreApp::class.java) {
                    if (mAsteroidAppInstance == null) mAsteroidAppInstance = AsteroidStoreApp()
                }
            }
            return mAsteroidAppInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        mAsteroidAppInstance = this
        Timber.plant(Timber.DebugTree())
        delayedInit()
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true).setRequiresCharging(true).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRequest =
            PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS).setConstraints(
                    constraints
                ).build()

        WorkManager.getInstance(AsteroidStoreApp.getInstance()!!.applicationContext).enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, repeatingRequest
        )
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }


}