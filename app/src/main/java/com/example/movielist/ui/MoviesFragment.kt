package com.example.movielist.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.codingwithmitch.openapi.viewmodels.ViewModelProviderFactory

import com.example.movielist.R
import com.example.movielist.util.ApiEmptyResponse
import com.example.movielist.util.ApiErrorResponse
import com.example.movielist.util.ApiSuccessResponse
import com.example.movielist.util.GenericApiResponse
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_movies.*
import java.lang.Exception
import javax.inject.Inject

class MoviesFragment : DaggerFragment() {


    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: MainViewModel;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run{
            ViewModelProvider(this,providerFactory).get(MainViewModel::class.java)
        }?:throw Exception("Invalid Activity")


        btn.setOnClickListener{

            findNavController().navigate(R.id.action_moviesFragment_to_movieDetailFragment);


            viewModel.testRequest().observe(viewLifecycleOwner, Observer {

                Log.d("TAG","Ok called here!")
            })

        }






    }
}
