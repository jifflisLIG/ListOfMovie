package com.example.movielist.api

import androidx.room.ColumnInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MovieResponse(

    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("original_title")
    @Expose
    var original_title:String,

    @SerializedName("overview")
    @Expose
    var overview:String,

    @SerializedName("description")
    @Expose
    var description: String,

    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("poster_path")
    @Expose
    var poster_path: String,

    @SerializedName("director")
    @Expose
    var director: String,

    @SerializedName("runtime")
    @Expose
    var runtime: Long,

    @SerializedName("vote_average")
    @Expose
    var vote_average:Double,

    @SerializedName("release_date")
    @Expose
    var release_date:String,

    @SerializedName("vote_count")
    @Expose
    var vote_count:String,

    @SerializedName("tagline")
    @Expose
    var tagline:String,

    @SerializedName("backdrop_path")
    @Expose
    var backdrop_path:String

){

    override fun toString(): String {
        return "MovieResponse(id=$id, original_title='$original_title', overview='$overview', description='$description', name='$name', poster_path='$poster_path', director='$director', runtime=$runtime, vote_average=$vote_average, release_date='$release_date', vote_count='$vote_count', tagline='$tagline', backdrop_path='$backdrop_path')"
    }
}