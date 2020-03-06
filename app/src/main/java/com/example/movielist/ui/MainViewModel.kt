package com.example.movielist.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.movielist.api.MovieResponse
import com.example.movielist.repository.MovieRepository
import com.example.movielist.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Inject

class MainViewModel
@Inject
constructor(
    val movieRepository: MovieRepository
): ViewModel()
{


    fun testRequest():LiveData<GenericApiResponse<MovieResponse>>{
        return movieRepository.testRequest();
    }


}