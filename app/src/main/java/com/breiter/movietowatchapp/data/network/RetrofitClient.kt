package com.breiter.movietowatchapp.data.network

import com.breiter.movietowatchapp.data.util.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitClient {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(Constants.BASE_URL)
        .build()

    private val retrofitService: MovieApi by lazy {
        retrofit.create(MovieApi::class.java)
    }

     fun getMoviesAsync(title: String)
            = retrofitService.getMoviesAsync(Constants.API_KEY, title)

     fun getGenresAsync()
            = retrofitService.getGenresAsync(Constants.API_KEY)
}