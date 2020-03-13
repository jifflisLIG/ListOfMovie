package com.example.movielist.api

import androidx.lifecycle.LiveData
import com.example.movielist.model.Movie
import com.example.movielist.util.GenericApiResponse
import retrofit2.http.*

interface OpenApiMainService {


    @GET("/4/movie/550?api_key=ce24e0a3555e9e64857064838c644fd5")
    fun test(): LiveData<GenericApiResponse<MovieResponse>>


    @GET("4/movie/5/lists")
    fun test2(
        @Header("Authorization") auth:String,
        @Query("Page")page:String
    ):LiveData<GenericApiResponse<MovieResponse>>

   // https://api.themoviedb.org/3/movie/now_playing
   // https://image.tmdb.org/t/p/original/aQvJ5WPzZgYVDrxLX4R6cLJCEaQ.jpg
    // https://image.tmdb.org/t/p/original/gL7TV2g9y9p3v7occ5bLrJ2p1qs.jpg
    //https://image.tmdb.org/t/p/original/2kNnf7BwRCEm4bcFkdiE0T4U25s.jpg


    @GET("3/movie/now_playing")
    fun getMovies(
        @Header("Authorization") auth:String,
        @Query("page")page:Int
    ):LiveData<GenericApiResponse<MovieListResponse>>


    @GET("3/movie/{id}")
    fun getMovie(
        @Header("Authorization")auth:String,
        @Path(value = "id")id:String
    ):LiveData<GenericApiResponse<MovieResponse>>


    @GET("3/search/movie?")
    fun searchMovie(
        @Header("Authorization")auth:String,
        @Query("page")page:Int,
        @Query("query")query:String
    ):LiveData<GenericApiResponse<MovieListResponse>>

}
