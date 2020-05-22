package com.breiter.movietowatchapp.data.domain


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val title: String?,
    val overview: String?,
    val rating: Double?,
    val posterUrl: String?,
    val language: String?,
    val releaseDate: String?,
    val genreIds: List<Int>?
) : Parcelable



