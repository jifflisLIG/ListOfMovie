package com.example.movielist.api

import androidx.lifecycle.LiveData
import com.example.movielist.util.GenericApiResponse
import retrofit2.http.*

interface OpenApiMainService {

    @GET("/3/movie/550?api_key=ce24e0a3555e9e64857064838c644fd5")
    fun test(): LiveData<GenericApiResponse<MovieResponse>>




}
