package com.example.movielist.util

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

class Network {

    companion object{
        fun isConnectedToTheInternet(context:Context): Boolean{
            val cm =    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            try{
                return cm.activeNetworkInfo.isConnected
            }catch (e: Exception){
                println(e.message)
            }
            return false
        }
    }

}