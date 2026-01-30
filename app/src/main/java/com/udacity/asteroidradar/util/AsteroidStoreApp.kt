package com.udacity.asteroidradar.util

import androidx.multidex.MultiDexApplication
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.udacity.asteroidradar.di.dataModule
import com.udacity.asteroidradar.di.databaseModule
import com.udacity.asteroidradar.di.domainModule
import com.udacity.asteroidradar.di.presentationModule
import com.udacity.asteroidradar.di.workerModule
import com.udacity.asteroidradar.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AsteroidStoreApp : MultiDexApplication() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    companion object {
        @Volatile
        private var mAsteroidAppInstance: AsteroidStoreApp? = null

        fun getApp(): AsteroidStoreApp {
            return mAsteroidAppInstance ?: synchronized(this) {
                mAsteroidAppInstance ?: AsteroidStoreApp().also { mAsteroidAppInstance = it }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        mAsteroidAppInstance = this
        Timber.plant(Timber.DebugTree())
        delayedInit()

        // Initialize Koin with organized modules
        startKoin {
            androidContext(this@AsteroidStoreApp)
            modules(
                listOf(
                    presentationModule,  // ViewModels
                    domainModule,        // Use Cases
                    dataModule,          // Repository & Data Sources
                    databaseModule,      // Room Database & DAOs
                    workerModule         // WorkManager Workers
                )
            )
        }

    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                RefreshDataWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                repeatingRequest
            )
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

}