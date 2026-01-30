package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.data.source.remote.dto.ImageOfDayDto
import com.udacity.asteroidradar.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

val client = OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor { it ->
    val url =
        it.request().url.newBuilder().addQueryParameter("api_key", BuildConfig.NASA_API_KEY).build()
    it.proceed(it.request().newBuilder().url(url).build())
}

private val retrofit = Retrofit.Builder().client(client.build()).baseUrl(Constants.BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface AsteroidApiService {
    /**
     * Returns a Coroutine [List] of [MarsProperty] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroid(
        @Query("start_date") startDate: String, @Query("end_date") endDate: String
    ): String

    @GET("planetary/apod")
    suspend fun getImageOfTheDay(): ImageOfDayDto
}

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy { retrofit.create(AsteroidApiService::class.java) }
}

