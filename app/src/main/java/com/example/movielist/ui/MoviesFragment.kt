package com.example.movielist.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codingwithmitch.openapi.viewmodels.ViewModelProviderFactory

import com.example.movielist.R
import com.example.movielist.model.Movie
import com.example.movielist.util.*
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_movies.*
import java.lang.Exception
import javax.inject.Inject

class MoviesFragment : DaggerFragment(),SwipeRefreshLayout.OnRefreshListener {
    val TAG: String = "AppDebug"

    private lateinit var  ctx: Context

    private lateinit var dependencyProvider: MainDependencyProvider
    private lateinit var recyclerAdapter: MoviesAdapter
    private lateinit var recyclerView:RecyclerView
    private lateinit var refresher:SwipeRefreshLayout;

    private lateinit var  mListener: MoviesAdapter.MoviesFragmentListener

    private lateinit var observer:Observer<Resource<ArrayList<Movie>?>>
    private lateinit var searchObserver:Observer<Resource<ArrayList<Movie>?>>

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: MainViewModel;


    override fun onRefresh() {
       initiazeMovie()
    }


    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        ctx = activity.applicationContext;

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view:View
        view = inflater.inflate(R.layout.fragment_movies, container, false)

        recyclerView = view.findViewById(R.id.recycler)
        refresher = view.findViewById(R.id.refresher)
        refresher.setOnRefreshListener(this)

        //initialize view model
        viewModel = activity?.run{
            ViewModelProvider(this,providerFactory).get(MainViewModel::class.java)
        }?:throw Exception("Invalid Activity")

        recyclerAdapter = MoviesAdapter(
            dependencyProvider.getGlideRequestManager(),
            mListener
        )

        initRecycler();
        initObserver()
        observeMovie()
        observeSearchMovie()

        loadData();


        return view
    }

    private fun loadData() {
        if(viewModel.is_initialize){
            viewModel.retrievePreviousData()
        }else{
            initiazeMovie()
        }
    }
    private fun initiazeMovie(){
        mListener.showProgress(true)
        viewModel.initializeMovies()
        mListener.onDataRefresh()
    }

    private fun initRecycler() {

        recyclerView.layoutManager=LinearLayoutManager(ctx)
        recyclerView.adapter = recyclerAdapter;

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == recyclerAdapter.itemCount.minus(1)) {

                    viewModel.nextPage()

                    if(viewModel.isQueryExhausted){
                        Toast.makeText(context,"No more data!",Toast.LENGTH_LONG).show();
                        recyclerAdapter.removeLoading()
                    }

                    if(viewModel.isQueryOnProgress && !viewModel.isQueryExhausted){
                        recyclerAdapter.displayLoading()
                    }



                }

            }
        })

    }

    private fun initObserver(){
        observer = Observer {resource->
            when(resource){

                is Resource.Success->{

                    recyclerAdapter.removeLoading()
                    recyclerAdapter.setMovies(resource.data)
                    refresher.isRefreshing = false
                    mListener.showProgress(false)

                }


                is Resource.Error -> {

                    if(resource.message.equals(ErrorHandling.ERROR_QUERY_EXHAUSTED)){
                        Toast.makeText(context,"No more data!",Toast.LENGTH_LONG).show();
                    }else{
                        if(!resource.message.equals("Job was cancelled")){
                            recyclerAdapter.removeLoading()
                            Toast.makeText(context, resource.message,Toast.LENGTH_LONG).show();
                        }

                    }

                    refresher.isRefreshing = false
                    mListener.showProgress(false)
                }


                is Resource.Loading -> {
                  //  recyclerAdapter.displayLoading()
                }

            }
        }


        searchObserver = Observer {resource->
            when(resource){
                is Resource.Success->{
                    recyclerAdapter.removeLoading()
                    recyclerAdapter.setMovies(resource.data)
                }
                is Resource.Error -> {
                    recyclerAdapter.removeLoading()
                    if(resource.message.equals(ErrorHandling.ERROR_QUERY_EXHAUSTED)){
                        Toast.makeText(context,"No more data!",Toast.LENGTH_LONG).show();
                    }else{
                        if(!resource.message.equals("Job was cancelled")){
                            Toast.makeText(context, resource.message,Toast.LENGTH_LONG).show();
                        }

                    }
                }

                is Resource.Loading -> {
                   // recyclerAdapter.displayLoading()
                }

            }
        }

    }

    private fun observeMovie(){
        viewModel.observeMovies().removeObserver(observer)
        viewModel.observeMovies().observe(viewLifecycleOwner, observer)
    }

    private fun observeSearchMovie(){
        viewModel.observeSearhMovies().removeObserver(searchObserver)
        viewModel.observeSearhMovies().observe(viewLifecycleOwner, searchObserver)
    }

    fun initializeSearch(query:String){
        viewModel.initializeSearch(query)
    }

    fun gotoFragmentDetails(id:String){
        var bundle = bundleOf("id" to id)
        findNavController().navigate(R.id.action_moviesFragment_to_movieDetailFragment,bundle)
    }


}
