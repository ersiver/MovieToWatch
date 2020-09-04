package com.breiter.movietowatchapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.breiter.movietowatchapp.R

/**
 * This is an application that uses the Navigation library.
 * Content is displayed by Fragments.
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}