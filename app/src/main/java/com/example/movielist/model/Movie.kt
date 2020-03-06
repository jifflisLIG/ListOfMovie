package com.example.movielist.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "movie")
data class Movie(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "release_date")
    var release_date: String,

    @ColumnInfo(name = "vote_average")
    var vote_average: String,

    @ColumnInfo(name = "director")
    var director: String,

    @ColumnInfo(name = "time")
    var time: Long

): Parcelable{
    override fun toString(): String {
        return "Movie(id=$id, title='$title', release_date='$release_date', vote_average='$vote_average', director='$director', time=$time)"
    }
}