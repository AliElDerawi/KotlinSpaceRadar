package com.udacity.asteroidradar.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.udacity.asteroidradar.domain.model.AsteroidModel
import com.udacity.asteroidradar.util.AsteroidStoreApp
import com.udacity.asteroidradar.util.Constants
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun parseAsteroidsJsonResult(jsonResult: JSONObject): List<AsteroidModel> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val asteroidModelList = mutableListOf<AsteroidModel>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {
        if (nearEarthObjectsJson.has(formattedDate)) {
            val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

            for (i in 0 until dateAsteroidJsonArray.length()) {
                val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
                val id = asteroidJson.getLong("id")
                val codename = asteroidJson.getString("name")
                val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                val estimatedDiameter =
                    asteroidJson.getJSONObject("estimated_diameter").getJSONObject("kilometers")
                        .getDouble("estimated_diameter_max")

                val closeApproachData =
                    asteroidJson.getJSONArray("close_approach_data").getJSONObject(0)
                val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                    .getDouble("kilometers_per_second")
                val distanceFromEarth =
                    closeApproachData.getJSONObject("miss_distance").getDouble("astronomical")
                val isPotentiallyHazardous =
                    asteroidJson.getBoolean("is_potentially_hazardous_asteroid")

                val asteroidModel = AsteroidModel(
                    id = id,
                    codename = codename,
                    closeApproachDate = formattedDate,
                    absoluteMagnitude = absoluteMagnitude,
                    estimatedDiameter = estimatedDiameter,
                    relativeVelocity = relativeVelocity,
                    distanceFromEarth = distanceFromEarth,
                    isPotentiallyHazardous = isPotentiallyHazardous
                )
                asteroidModelList.add(asteroidModel)
            }
        }
    }

    return asteroidModelList
}

private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale("en"))
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}

fun getTodayDate(): String {
    val calendar = Calendar.getInstance()
    val currentTime = calendar.time
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale("en"))
    return dateFormat.format(currentTime)
}

fun getEndDate(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS - 1)
    val currentTime = calendar.time
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale("en"))
    return dateFormat.format(currentTime)
}

fun isNetworkConnected(): Boolean {
    val connectivityManager = AsteroidStoreApp.getApp().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
    return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}
