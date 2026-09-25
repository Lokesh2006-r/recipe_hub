package com.example.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface MealDbApi {
    @GET("search.php")
    suspend fun searchMeals(@Query("s") query: String): MealResponse

    @GET("lookup.php")
    suspend fun lookupMeal(@Query("i") id: String): MealResponse

    @GET("filter.php")
    suspend fun filterByCategory(@Query("c") category: String): MealResponse

    companion object {
        private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

        fun create(): MealDbApi {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .build()

            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(MealDbApi::class.java)
        }
    }
}
