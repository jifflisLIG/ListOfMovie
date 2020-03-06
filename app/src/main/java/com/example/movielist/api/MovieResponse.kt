package com.example.movielist.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieResponse(

    @SerializedName("adult")
    @Expose
    var adult: String,

    @SerializedName("original_title")
    @Expose
    var original_title: String,

    @SerializedName("overview")
    @Expose
    var overview: String,

    @SerializedName("release_date")
    @Expose
    var release_date: String,

    @SerializedName("revenue")
    @Expose
    var revenue: String


){
    override fun toString(): String {
        return "MovieResponse(adult='$adult', original_title='$original_title', overview='$overview', release_date='$release_date', revenue='$revenue')"
    }
}