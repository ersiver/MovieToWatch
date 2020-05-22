package com.breiter.movietowatchapp.data.util

class Constants {

    companion object {
        const val BASE_URL = "http://api.themoviedb.org/3/"
        const val API_KEY = "xxx" //obtain your api_key
        const val IMAGE_URL = "https://image.tmdb.org/t/p/w500/"
    }

    object DataWorker{
        const val WORK_NAME = "com.breiter.movietowatchapp.data.work.RefreshDataWorker"
    }

}