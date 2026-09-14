package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.domain.model.AsteroidApiFilter
import com.udacity.asteroidradar.domain.repository.AsteroidRepository
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class RefreshDataWorker(
    private val asteroidRepository: AsteroidRepository, appContext: Context, params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            asteroidRepository.refreshAsteroids(AsteroidApiFilter.SHOW_WEEK)
            asteroidRepository.refreshImageOfDay()
            Result.success()
        } catch (e: HttpException) {
            Timber.e(e, "HTTP error occurred while refreshing data")
            Result.retry()
        } catch (e: IOException) {
            Timber.e(e, "Network error occurred while refreshing data")
            Result.retry()
        } catch (e: Exception) {
            Timber.e(e, "Unknown error occurred")
            Result.failure()
        }
    }

}
