package com.example.movielist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movielist.R
import com.facebook.stetho.Stetho
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Stetho.initializeWithDefaults(this);
    }
}
