package com.example.movielist.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movielist.model.Movie
import com.example.movielist.util.Constants

@Dao
interface MovieDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie): Long

    @Query("DELETE from movie where id =:id")
    suspend fun delete(id:Int)

    @Query("SELECT * FROM movie order by aID LIMIT(:page * :pageSize)")
    fun getAllMovies(page: Int,pageSize: Int = Constants.PAGINATION_PAGE_SIZE):LiveData<List<Movie>>

    @Query("SELECT * FROM movie where id LIKE '%' || :query|| '%'")
    fun getMovie(query:String):LiveData<Movie>


    @Query("SELECT * FROM movie" +
            " WHERE original_title LIKE '%' || :query || '%' " +
            " ORDER BY aID DESC LIMIT (:page * :pageSize)"
    )
    fun searchMovies(
        query: String?,
        page: Int,
        pageSize: Int = Constants.PAGINATION_PAGE_SIZE
    ): LiveData<List<Movie>>

}


















