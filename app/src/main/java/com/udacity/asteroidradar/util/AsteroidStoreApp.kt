package com.udacity.asteroidradar.util

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.udacity.asteroidradar.data.database.getDatabase
import com.udacity.asteroidradar.features.main.MainViewModel
import com.udacity.asteroidradar.data.repository.AsteroidRepository
import com.udacity.asteroidradar.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AsteroidStoreApp : Application() {

    val applicationScope = CoroutineScope(Dispatchers.Default)

    companion object {
        @Volatile
        private var mAsteroidAppInstance: AsteroidStoreApp? = null

        fun getApp(): AsteroidStoreApp {
            if (mAsteroidAppInstance == null) {
                synchronized(AsteroidStoreApp::class.java) {
                    if (mAsteroidAppInstance == null) mAsteroidAppInstance = AsteroidStoreApp()
                }
            }
            return mAsteroidAppInstance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        mAsteroidAppInstance = this
        Timber.plant(Timber.DebugTree())
        delayedInit()

        val myModule = module {
            //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()


            single {
                MainViewModel(get(), get())
            }

            single {
                getDatabase(get())
            }

            single {
                RefreshDataWorker(get(), get(), get())
            }

            single { AsteroidRepository(get()) }

        }

        startKoin {
            androidContext(this@AsteroidStoreApp)
            modules(listOf(myModule))
        }

    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true).setRequiresCharging(true).apply {
                setRequiresDeviceIdle(true)
            }.build()

        val repeatingRequest =
            PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS).setConstraints(
                constraints
            ).build()

        WorkManager.getInstance(getApp().applicationContext).enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, repeatingRequest
        )
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }


}