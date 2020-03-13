package com.example.movielist.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "movie")
data class Movie(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "aID")
    var aID: Int,

    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "original_title")
    var original_title: String?,

    @ColumnInfo(name = "overview")
    var overview: String?,

    @ColumnInfo(name = "description")
    var description: String?,

    @ColumnInfo(name = "name")
    var name: String?,

    @ColumnInfo(name = "poster_path")
    var poster_path: String?,

    @ColumnInfo(name = "director")
    var director: String?,

    @ColumnInfo(name = "runtime")
    var runtime: Long,

    @ColumnInfo(name = "vote_average")
    var vote_average: Double,

    @ColumnInfo(name = "release_date")
    var release_date: String?,

    @ColumnInfo(name = "vote_count")
    var vote_count: String?,

    @ColumnInfo(name = "tagline")
    var tagline: String?,

    @ColumnInfo(name = "backdrop_path")
    var backdrop_path: String?

) : Parcelable {




    override fun toString(): String {
        return "Movie(id=$id, original_title=$original_title, overview=$overview, description=$description, name=$name, poster_path=$poster_path, director=$director, runtime=$runtime, vote_average=$vote_average, release_date=$release_date, vote_count=$vote_count, tagline=$tagline, backdrop_path=$backdrop_path)"
    }
}