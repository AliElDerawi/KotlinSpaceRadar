package com.udacity.asteroidradar.util

import androidx.multidex.MultiDexApplication
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.udacity.asteroidradar.data.database.AsteroidDao
import com.udacity.asteroidradar.data.database.AsteroidDatabase
import com.udacity.asteroidradar.data.database.ImageOfTodayDao
import com.udacity.asteroidradar.data.database.getDatabase
import com.udacity.asteroidradar.data.repository.AsteroidRepository
import com.udacity.asteroidradar.data.repository.AsteroidRepositoryImpl
import com.udacity.asteroidradar.data.source.AsteroidLocalDataSource
import com.udacity.asteroidradar.data.source.AsteroidLocalDataSourceImpl
import com.udacity.asteroidradar.features.main.viewModel.MainViewModel
import com.udacity.asteroidradar.data.source.AsteroidRemoteDataSource
import com.udacity.asteroidradar.data.source.AsteroidRemoteDataSourceImpl
import com.udacity.asteroidradar.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
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

        val myModule = module {
            viewModelOf(::MainViewModel)

            singleOf(::getDatabase)
            single<AsteroidDao> {
                get<AsteroidDatabase>().asteroidDao
            }
            single<ImageOfTodayDao> {
                get<AsteroidDatabase>().imageOfTodayDao
            }

            workerOf(::RefreshDataWorker)

            single<AsteroidRemoteDataSource> {
                AsteroidRemoteDataSourceImpl()
            }
            single<AsteroidLocalDataSource> {
                AsteroidLocalDataSourceImpl(get(),get())
            }

            single<AsteroidRepository> {
                AsteroidRepositoryImpl(
                    remoteDataSource = get(),
                    get(),
                    ioDispatcher = Dispatchers.IO
                )
            }

        }

        startKoin {
            androidContext(this@AsteroidStoreApp)
            modules(listOf(myModule))
        }

    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true).apply {
                setRequiresDeviceIdle(true)
            }
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