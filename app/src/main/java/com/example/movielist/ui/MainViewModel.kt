package com.example.movielist.ui

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.movielist.api.MovieResponse
import com.example.movielist.model.Movie
import com.example.movielist.repository.MovieRepository
import com.example.movielist.util.Constants
import com.example.movielist.util.ErrorHandling
import com.example.movielist.util.GenericApiResponse
import com.example.movielist.util.Resource
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Inject

class MainViewModel
@Inject
constructor(
    app:Application,
    val movieRepository: MovieRepository
): AndroidViewModel(app)
{

    companion object{
        var REG_TYPE:Int=0
        var SEARCH_TYPE:Int = 1

    }
    var isQueryExhausted:Boolean = false;
     var isQueryOnProgress:Boolean = false;
    var page:Int = 1;
    private var  context: Context = app.applicationContext;

    private var movies:MediatorLiveData<Resource<ArrayList<Movie>?>> = MediatorLiveData()
    private var searchMovies:MediatorLiveData<Resource<ArrayList<Movie>?>> = MediatorLiveData()

    private var movieSize:Int =0
    private var data_type:Int =0;
    private  var query:String ="";

    var last_click_position:Int = 0;
    var is_initialize:Boolean = false;





    fun getMovie(id:String):LiveData<Resource<Movie>>{
        return movieRepository.getMovie(context,id)
    }

    fun observeMovies():LiveData<Resource<ArrayList<Movie>?>>{
        return movies
    }

    fun observeSearhMovies():LiveData<Resource<ArrayList<Movie>?>>{
        return searchMovies
    }

    fun initializeMovies(){
        if (page == 0) {
            page = 1
        }
        is_initialize = true
        this.data_type = REG_TYPE
        this.page = page
        isQueryExhausted = false
        getMovies()
    }

    fun getMovies(){
        movies.value = Resource.Loading(null)
        isQueryOnProgress = true;
        var repositorySource = movieRepository.getMovies(context = context, page = page)
        movies.addSource(repositorySource) {moviesResult->
           when(moviesResult){

               is Resource.Error->{
                   movies.value = Resource.Error(moviesResult.message.toString(),null)
                   isQueryOnProgress = false;
               }

               is Resource.Success->{

                   if(page*Constants.PAGINATION_PAGE_SIZE>moviesResult.data!!.size
                       && moviesResult.message.equals("network")){
                       isQueryExhausted = true
                       movies.value = Resource.Error(ErrorHandling.ERROR_QUERY_EXHAUSTED)
                       isQueryOnProgress= false

                   }else if(movieSize == moviesResult.data!!.size && moviesResult.message=="cache"){
                       isQueryOnProgress = true;
                   }else{
                       movies.value = Resource.Success(data=moviesResult.data as ArrayList<Movie>)
                       movieSize = moviesResult.data.size;
                       isQueryOnProgress = false;
                   }


               }


           }


        }
    }

    fun initializeSearch(query:String){
        this.data_type = SEARCH_TYPE
        this.page = 1
        this.query = query;
        isQueryExhausted = false
        searchMovies(query,page)
    }



    fun searchMovies(query:String,page:Int){

        if(query.isEmpty()){
            return
        }

        searchMovies.value = Resource.Loading(null)
        isQueryOnProgress = true;
        var repositorySource = movieRepository.searchMovies(query = query,context = context, page = page)
        searchMovies.addSource(repositorySource) {moviesResult->
            when(moviesResult){

                is Resource.Error->{
                    searchMovies.value = Resource.Error(moviesResult.message.toString(),null)
                    isQueryOnProgress = false;
                }

                is Resource.Success->{

                    if(page*Constants.PAGINATION_PAGE_SIZE>moviesResult.data!!.size
                        && moviesResult.message.equals("network")){
                        isQueryExhausted = true
                        searchMovies.value = Resource.Error(ErrorHandling.ERROR_QUERY_EXHAUSTED)
                        isQueryOnProgress = false;
                    }else if(movieSize == moviesResult.data!!.size && moviesResult.message=="cache"){
                        isQueryOnProgress = false;
                    }else{
                        searchMovies.value = Resource.Success(data=moviesResult.data as ArrayList<Movie>)
                        movieSize = moviesResult.data.size;

                        isQueryOnProgress = false;
                    }

                }


            }

        }
    }

    fun nextPageSearch():Boolean{
        if(!isQueryExhausted && !isQueryOnProgress){
            page++
            searchMovies(query,page)
            return true
        }
        return false
    }

    fun nextPageReg():Boolean{
        if(!isQueryExhausted && !isQueryOnProgress){
            page++
            getMovies()
            return true

        }
        return false
    }


    fun nextPage():Boolean {
        return if(data_type.equals(REG_TYPE)){
            nextPageReg()
        }else{
            nextPageSearch()
        }
    }

    fun retrievePreviousData(){
        return if(data_type.equals(REG_TYPE)){
          movies.postValue(movies.value)
        }else{
            movies.postValue(searchMovies.value)
        }
    }
}