package com.example.movielist.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.RequestManager
import com.codingwithmitch.openapi.viewmodels.ViewModelProviderFactory
import com.example.movielist.R
import com.example.movielist.repository.MovieRepository
import com.example.movielist.util.Resource
import com.facebook.stetho.Stetho
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.fragment_movies.view.*
import java.lang.Exception
import java.lang.NullPointerException
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() ,MainDependencyProvider,MoviesAdapter.MoviesFragmentListener{

    private var TAG:String = "appdebug"

    @Inject
    lateinit var movieRepository: MovieRepository;
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: MainViewModel;

    @Inject
    lateinit var requestManager: RequestManager

    lateinit var searchView:SearchView


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu);
        var searchMenu:MenuItem = menu!!.findItem(R.id.search)

        searchView = searchMenu.actionView as SearchView
        searchView.queryHint="Search Movie"

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var f:MoviesFragment = getCurrentFragment() as MoviesFragment
                f.initializeSearch(newText!!)
              return false
            }

        })
        return super.onCreateOptionsMenu(menu)

    }

    override fun getVMProviderFactory(): ViewModelProviderFactory {
        return providerFactory
    }

    override fun getGlideRequestManager(): RequestManager {
        return requestManager
    }

    override fun onMovieClick(id: String,position:Int) {
        var frag:MoviesFragment = getCurrentFragment() as MoviesFragment;
        frag.gotoFragmentDetails(id)

        viewModel.last_click_position = position
    }

    override fun onDataRefresh() {
        try{
            searchView.isIconified = true
            searchView.isIconified = true
        }catch (ex:Exception) {
            Log.d(TAG, "Null Error!")
        }
    }

    override fun showProgress(flag: Boolean) {
       showProgressBar(flag)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Stetho.initializeWithDefaults(this);

        viewModel = this.run{
            ViewModelProvider(this,providerFactory).get(MainViewModel::class.java)
        }


    }

    fun getCurrentFragment():Fragment{
        val navHostFragment: NavHostFragment? =
            supportFragmentManager.findFragmentById(R.id.auth_nav_host_fragment) as NavHostFragment
        return navHostFragment!!.childFragmentManager.fragments[0]
    }

    fun showProgressBar(flag:Boolean){
        var progressBar:ProgressBar =findViewById(R.id.progress)
        if(flag){
            progressBar.visibility = ProgressBar.VISIBLE
        }else{
            progressBar.visibility = ProgressBar.GONE
        }
    }

}
