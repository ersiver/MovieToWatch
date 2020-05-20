package com.breiter.movietowatchapp.data.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET("search/movie")
     fun getMoviesAsync(
        @Query("api_key") apiKey: String,
        @Query("query") title: String,
        @Query("page") page: Int = 1
    ): Deferred<MovieResponseDTO>


    @GET("genre/movie/list")
     fun getGenresAsync(
        @Query("api_key") apiKey: String
    ): Deferred<GenreResponseDTO>
}

