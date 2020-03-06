package com.codingwithmitch.openapi.di.main
import com.example.movielist.ui.MovieDetailFragment
import com.example.movielist.ui.MoviesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeMoviesFragment(): MoviesFragment

    @ContributesAndroidInjector
    abstract fun contributeMovieDetailFragment():MovieDetailFragment
}