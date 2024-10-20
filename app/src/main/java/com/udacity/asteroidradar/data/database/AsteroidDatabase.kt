package com.udacity.asteroidradar.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.api.getEndDate
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.api.models.AsteroidModel
import com.udacity.asteroidradar.api.models.ImageOfTodayModel
import kotlinx.coroutines.flow.Flow



@Dao
interface AsteroidDao {

    @Query("select * from asteroid_data where closeApproachDate >= :startDate and closeApproachDate <= :endData order by closeApproachDate asc")
    fun getAsteroidsList(
        startDate: String = getTodayDate(),
        endData: String = getEndDate()
    ): Flow<List<AsteroidModel>>

    @Query("select * from asteroid_data")
    fun getAllAsteroid(
    ): Flow<List<AsteroidModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroidList: AsteroidModel)

}

@Dao
interface ImageOfTodayDao {
    @Query("select * from image_of_day_data where :currentDate = date")
    fun getImageOfToday(currentDate: String): Flow<ImageOfTodayModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImageOfToday(imageOfTodayModel: ImageOfTodayModel)
}

@Database(entities = [AsteroidModel::class, ImageOfTodayModel::class], version = 2)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val imageOfTodayDao: ImageOfTodayDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids"
            ).fallbackToDestructiveMigration().build()
        }
    }
    return INSTANCE
}