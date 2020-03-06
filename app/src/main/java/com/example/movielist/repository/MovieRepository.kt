package com.example.movielist.repository

import androidx.lifecycle.LiveData
import com.example.movielist.api.MovieResponse
import com.example.movielist.api.OpenApiMainService
import com.example.movielist.util.GenericApiResponse

class MovieRepository(
    val openApiMainService:OpenApiMainService
) {

    fun testRequest():LiveData<GenericApiResponse<MovieResponse>>{
        return openApiMainService.test();
    }
}