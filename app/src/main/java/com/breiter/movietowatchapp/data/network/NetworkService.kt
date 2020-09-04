package com.breiter.movietowatchapp.data.network

import com.breiter.movietowatchapp.data.util.Constants.Companion.API_KEY
import com.breiter.movietowatchapp.data.util.Constants.Companion.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val service: MovieApi by lazy {

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

    retrofit.create(MovieApi::class.java)
}

fun getMovieApiService() = service

/**
 * Main network interface which will fetch a list movies
 */
interface MovieApi {

    @GET("search/movie")
    fun getMoviesAsync(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("query") title: String,
        @Query("page") page: Int = 1
    ): Deferred<MovieResponseDTO>


    @GET("genre/movie/list")
    fun getGenresAsync(
        @Query("api_key") apiKey: String = API_KEY
    ): Deferred<GenreResponseDTO>
}