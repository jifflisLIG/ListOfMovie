package com.example.movielist.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movielist.model.Movie

@Database(entities = [Movie::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    companion object{
        val DATABASE_NAME: String = "app_db"
    }


}








