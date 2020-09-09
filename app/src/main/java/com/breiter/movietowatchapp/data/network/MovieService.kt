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
import retrofit2.http.Query

/**
 * Main network interface which will fetch a list movies
 */
interface MovieService {

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


    companion object {
        fun create(): MovieService {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .baseUrl(BASE_URL)
                .build()
                .create(MovieService::class.java)
        }
    }
}