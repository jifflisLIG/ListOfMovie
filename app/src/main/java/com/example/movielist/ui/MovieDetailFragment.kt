package com.example.movielist.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.codingwithmitch.openapi.viewmodels.ViewModelProviderFactory

import com.example.movielist.R
import com.example.movielist.model.Movie
import com.example.movielist.util.Constants
import dagger.android.support.DaggerFragment
import java.lang.Exception
import javax.inject.Inject


class MovieDetailFragment : DaggerFragment() {
    private val TAG: String = "AppDebug"
    private lateinit var dependencyProvider: MainDependencyProvider

    private lateinit var movieID:String;
    private lateinit var ctx: Context;

    private lateinit var coveredPhoto:ImageView
    private lateinit var txtTitle:TextView
    private lateinit var txtRunTime:TextView
    private lateinit var txtTagline:TextView
    private lateinit var txtVoteAverage:TextView
    private lateinit var txtVoteCount:TextView
    private lateinit var txtOverview:TextView

    private lateinit var  mListener: MoviesAdapter.MoviesFragmentListener

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: MainViewModel;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        var view:View =inflater.inflate(R.layout.fragment_movie_detail, container, false);
        coveredPhoto = view.findViewById(R.id.image);
        txtTitle = view.findViewById(R.id.title);
        txtTagline = view.findViewById(R.id.tagline);
        txtVoteAverage = view.findViewById(R.id.vote_average);
        txtVoteCount = view.findViewById(R.id.vote_count);
        txtRunTime = view.findViewById(R.id.runtime);
        txtOverview = view.findViewById(R.id.overview);
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context;
        try{
            dependencyProvider = context as MainDependencyProvider
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement DependencyProvider" )
        }

        try{
            mListener = activity as MoviesAdapter.MoviesFragmentListener
        }catch (e: java.lang.ClassCastException){
            Log.e(TAG, "$context must implement MoviesFragmentListener" )
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        movieID = arguments?.getString("id").toString();

        //initialize view model
        viewModel = activity?.run{
            ViewModelProvider(this,providerFactory).get(MainViewModel::class.java)
        }?:throw Exception("Invalid Activity")

        movieObserver();

    }


    private fun movieObserver() {
        viewModel.getMovie(movieID).observe(viewLifecycleOwner, Observer {resource->
            when(resource){


                is com.example.movielist.util.Resource.Success->{
                    var movie:Movie = resource.data as Movie
                    setDetails(movie)
                    mListener.showProgress(false)
                }

                is com.example.movielist.util.Resource.Error -> {
                    mListener.showProgress(false)
                }

                is com.example.movielist.util.Resource.Loading -> {
                    println("request is loading")
                    mListener.showProgress(true)
                }

            }
        })
    }

    fun setDetails(movie:Movie){
        dependencyProvider.getGlideRequestManager()
            .load(Constants.IMAGE_BASE_URL+movie.backdrop_path)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(coveredPhoto)

        txtTitle.text = movie.original_title;
        txtTagline.text = movie.tagline;
        txtVoteAverage.text = movie.vote_average.toString();
        txtVoteCount.text = movie.vote_count.toString();
        txtRunTime.text = movie.runtime.toString();
        txtOverview.text = movie.overview;

    }

}
