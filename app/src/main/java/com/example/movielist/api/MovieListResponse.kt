package com.example.movielist.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieListResponse (

    @SerializedName("results")
    @Expose
    var results: List<MovieResponse>,

    @SerializedName("detail")
    @Expose
    var detail: String
){
    override fun toString(): String {
        return "MovieListResponse(results=$results, detail='$detail')"
    }
}