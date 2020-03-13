package com.example.movielist.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.movielist.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

abstract class NetworkBoundResource<ResponseObject, CacheObject>
    (
    isNetworkAvailable: Boolean, // is their a network connection?
    isNetworkRequest: Boolean, // is this a network request?
    shouldCancelIfNoInternet: Boolean, // should this job be cancelled if there is no network?
    shouldLoadFromCache: Boolean // should the cached data be loaded?
) {


    val TAG: String = "AppDebug"

    protected val result = MediatorLiveData<Resource<CacheObject>>()
    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope

    init {

        setJob(initNewJob())

        // update LiveData for loading status
        setValue(Resource.Loading(null))

        if(shouldLoadFromCache){

            // view cache to start
            val dbSource = loadFromCache()

            result.addSource(dbSource) { response->
                result.removeSource(dbSource)
                setValue(Resource.Success("cache",response))
            }

        }

        if(isNetworkRequest){

            if(isNetworkAvailable){
                doNetworkRequest()
            }

            else{

                if(shouldCancelIfNoInternet){
                    onErrorReturn(
                        ErrorHandling.UNABLE_TODO_OPERATION_WO_INTERNET)
                }
                else{
                    doCacheRequest()
                }

            }

        }
        else{
            doCacheRequest()
        }
    }

    fun doCacheRequest(){
        coroutineScope.launch {
            // View data from cache only and return
            createCacheRequestAndReturn()
        }
    }

    fun doNetworkRequest(){

        coroutineScope.launch {
            withContext(Main){
                // make network call

                delay(1000)
                val apiResponse = createCall()

                result.addSource(apiResponse) { response->
                    result.removeSource(apiResponse)
                    coroutineScope.launch {

                        handleNetworkCall(response)
                    }
                }


            }
        }

//        GlobalScope.launch(IO){
//
//            delay(NETWORK_TIMEOUT)
//
//            if(!job.isCompleted){
//                Log.e(TAG, "NetworkBoundResource: JOB NETWORK TIMEOUT." )
//                job.cancel(CancellationException("Unable to resolve host"))
//            }
//        }
    }

    suspend fun handleNetworkCall(response: GenericApiResponse<ResponseObject>){

        when(response){
            is ApiSuccessResponse ->{
                handleApiSuccessResponse(response)
            }
            is ApiErrorResponse ->{
                Log.e(TAG, "NetworkBoundResource: ${response.errorMessage}")
                onErrorReturn(response.errorMessage)
            }
            is ApiEmptyResponse ->{
                Log.e(TAG, "NetworkBoundResource: Request returned NOTHING (HTTP 204).")
                onErrorReturn("HTTP 204. Returned NOTHING.")
            }
        }
    }

    fun onCompleteJob(newValue: Resource<CacheObject>){
        GlobalScope.launch(Main) {
            job.complete()
            setValue(newValue)
        }
    }

    fun onErrorReturn(errorMessage: String?){
        var msg = errorMessage
        if(msg == null){
            msg = "Unknown Error"
        }
        else if(ErrorHandling.isNetworkError(msg)){
            msg = "Error Check Network Connection"
        }

        onCompleteJob(Resource.Error(msg,null))
    }

    fun setValue(newValue: Resource<CacheObject>) {
        if (result.getValue() !== newValue) {
            result.setValue(newValue)
        }
    }


    @UseExperimental(InternalCoroutinesApi::class)
    private fun initNewJob(): Job{
        Log.d(TAG, "initNewJob: called.")
        job = Job() // create new job
        job.invokeOnCompletion(onCancelling = true, invokeImmediately = true, handler = object: CompletionHandler{
            override fun invoke(cause: Throwable?) {
                if(job.isCancelled){
                    Log.e(TAG, "NetworkBoundResource: Job has been cancelled.")
                    cause?.let{
                        onErrorReturn(it.message)
                    }?: onErrorReturn("Unknown error.")
                }
                else if(job.isCompleted){
                    Log.e(TAG, "NetworkBoundResource: Job has been completed.")
                    // Do nothing? Should be handled already
                }
            }
        })
        coroutineScope = CoroutineScope(IO + job)
        return job
    }

    fun asLiveData() = result as LiveData<Resource<CacheObject>>

    abstract suspend fun createCacheRequestAndReturn()

    abstract suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<ResponseObject>)

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    abstract fun loadFromCache(): LiveData<CacheObject>

    abstract suspend fun updateLocalDb(cacheObject: CacheObject?)

    abstract fun setJob(job: Job)

}















