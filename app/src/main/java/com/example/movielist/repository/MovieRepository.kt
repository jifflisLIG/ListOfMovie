package com.example.movielist.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.movielist.api.MovieListResponse
import com.example.movielist.api.MovieResponse
import com.example.movielist.api.OpenApiMainService
import com.example.movielist.model.Movie
import com.example.movielist.persistence.MovieDao
import com.example.movielist.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieRepository(
    val openApiMainService:OpenApiMainService,
    val movieDao:MovieDao
):JobManager("MovieRepository") {

    fun testRequest():LiveData<GenericApiResponse<MovieResponse>>{
        return openApiMainService.getMovie(
            auth = Constants.API_KEY,
            id="454626"
        );
    }


    fun getMovie(context: Context,id:String):LiveData<Resource<Movie>>{
        return object :NetworkBoundResource<MovieResponse,Movie>(
            Network.isConnectedToTheInternet(context),
            true,
            false,
            true
        ){
            override suspend fun createCacheRequestAndReturn() {
                withContext(Dispatchers.Main){
                    // finishing by viewing db cache
                    var fromCache = loadFromCache()
                    result.addSource(fromCache){ movie->
                        result.removeSource(fromCache)
                        onCompleteJob(Resource.Success(data = movie))
                    }
                }
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<MovieResponse>) {
                var movieResponse:MovieResponse = response.body

               var mov= Movie(
                    id=movieResponse.id,
                    original_title = movieResponse.original_title,
                    overview = movieResponse.overview,
                    description = movieResponse.description,
                    name = movieResponse.name,
                    poster_path = movieResponse.poster_path,
                    director = movieResponse.director,
                    runtime = movieResponse.runtime,
                    vote_average = movieResponse.vote_average,
                    release_date = movieResponse.release_date,
                    vote_count = movieResponse.vote_count,
                    tagline = movieResponse.tagline,
                   backdrop_path = movieResponse.backdrop_path,
                   aID = 0
                )

                updateLocalDb(mov)
                createCacheRequestAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<MovieResponse>> {
                return openApiMainService.getMovie(
                    auth = Constants.API_KEY,
                    id = id
                )
            }

            override fun loadFromCache(): LiveData<Movie> {
                return movieDao.getMovie(id)
            }

            override suspend fun updateLocalDb(cacheObject: Movie?) {
                if(cacheObject != null){
                    withContext(Dispatchers.IO) {
                        try{
                            launch {
                                movieDao.delete(cacheObject.id)
                                movieDao.insert(cacheObject)
                            }
                        }catch (e: Exception){
                            Log.e(TAG, "updateLocalDb: error updating cache data on movie with id: ${cacheObject.id}. " +
                                    "${e.message}")
                        }
                    }
                }
                else{
                    Log.d(TAG, "updateLocalDb: movie list is null")
                }
            }

            override fun setJob(job: Job) {
                addJob("searchMovies", job)
            }

        }.asLiveData()
    }



    fun getMovies(context:Context,page:Int):LiveData<Resource<List<Movie>>>{

        return object: NetworkBoundResource<MovieListResponse,List<Movie>>(
            Network.isConnectedToTheInternet(context),
            true,
            false,
            true

        ){
            override suspend fun createCacheRequestAndReturn() {

                withContext(Dispatchers.Main){
                    // finishing by viewing db cache
                    var fromCache = loadFromCache()

                    result.addSource(fromCache){ movies->
                       result.removeSource(fromCache)
                        onCompleteJob(Resource.Success("network",movies))
                    }
                }
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<MovieListResponse>) {
                val movieList: ArrayList<Movie> = ArrayList()
                for(movieResponse in response.body.results){
                    movieList.add(
                        Movie(
                            id=movieResponse.id,
                            original_title = movieResponse.original_title,
                            overview = movieResponse.overview,
                            description = movieResponse.description,
                            name = movieResponse.name,
                            poster_path = movieResponse.poster_path,
                            director = movieResponse.director,
                            runtime = movieResponse.runtime,
                            vote_average = movieResponse.vote_average,
                            release_date = movieResponse.release_date,
                            vote_count = movieResponse.vote_count,
                            tagline = movieResponse.tagline,
                            backdrop_path = movieResponse.backdrop_path,
                            aID = 0
                        )
                    )
                }
                updateLocalDb(movieList)
                createCacheRequestAndReturn()
            }
            //

            override fun createCall(): LiveData<GenericApiResponse<MovieListResponse>> {

                return openApiMainService.getMovies(
                    auth = Constants.API_KEY,
                    page = page
                )
            }


            override fun loadFromCache(): LiveData<List<Movie>> {
                return movieDao.getAllMovies(page)
            }

            override suspend fun updateLocalDb(cacheObject: List<Movie>?) {
                // loop through list and update the local db
                if(cacheObject != null){
                    withContext(Dispatchers.IO) {
                        for(movie in cacheObject){
                            try{
                                // Launch each insert as a separate job to be executed in parallel
                                launch {
                                    movieDao.delete(movie.id)
                                    movieDao.insert(movie)
                                }
                            }catch (e: Exception){
                                Log.e(TAG, "updateLocalDb: error updating cache data on movie with id: ${movie.id}. " +
                                        "${e.message}")
                            }
                        }
                    }
                }
                else{
                    Log.d(TAG, "updateLocalDb: movie list is null")
                }

            }

            override fun setJob(job: Job) {
                addJob("searchMovies", job)
            }

        }.asLiveData()
    }



    fun searchMovies(context:Context,query:String,page:Int):LiveData<Resource<List<Movie>>>{

        return object:NetworkBoundResource<MovieListResponse,List<Movie>>(
            Network.isConnectedToTheInternet(context),
            true,
            false,
            true
        ){
            override suspend fun createCacheRequestAndReturn() {

                withContext(Dispatchers.Main){
                    // finishing by viewing db cache
                    var fromCache = loadFromCache()
                    result.addSource(fromCache){ movies->
                        result.removeSource(fromCache)
                        onCompleteJob(Resource.Success("network",movies))
                    }
                }
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<MovieListResponse>) {
                val movieList: ArrayList<Movie> = ArrayList()
                for(movieResponse in response.body.results){
                    movieList.add(
                        Movie(
                            id=movieResponse.id,
                            original_title = movieResponse.original_title,
                            overview = movieResponse.overview,
                            description = movieResponse.description,
                            name = movieResponse.name,
                            poster_path = movieResponse.poster_path,
                            director = movieResponse.director,
                            runtime = movieResponse.runtime,
                            vote_average = movieResponse.vote_average,
                            release_date = movieResponse.release_date,
                            vote_count = movieResponse.vote_count,
                            tagline = movieResponse.tagline,
                            backdrop_path = movieResponse.backdrop_path,
                            aID = 0
                        )
                    )
                }
                updateLocalDb(movieList)
                createCacheRequestAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<MovieListResponse>> {

                return openApiMainService.searchMovie(
                    auth = Constants.API_KEY,
                    page = page,
                    query = query
                )
            }


            override fun loadFromCache(): LiveData<List<Movie>> {
                return movieDao.searchMovies(query = query,page = page);
            }


            override suspend fun updateLocalDb(cacheObject: List<Movie>?) {
                // loop through list and update the local db
                if(cacheObject != null){
                    withContext(Dispatchers.IO) {
                        for(movie in cacheObject){
                            try{
                                // Launch each insert as a separate job to be executed in parallel
                                launch {
                                    movieDao.delete(movie.id)
                                    movieDao.insert(movie)
                                }
                            }catch (e: Exception){
                                Log.e(TAG, "updateLocalDb: error updating cache data on movie with id: ${movie.id}. " +
                                        "${e.message}")
                            }
                        }
                    }
                }
                else{
                    Log.d(TAG, "updateLocalDb: movie list is null")
                }

            }

            override fun setJob(job: Job) {
                addJob("searchMovies", job)
            }

        }.asLiveData()
    }

}