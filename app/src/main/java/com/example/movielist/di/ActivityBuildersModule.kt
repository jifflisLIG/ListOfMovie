package com.example.movielist.di

import com.codingwithmitch.openapi.di.main.MainFragmentBuildersModule
import com.codingwithmitch.openapi.di.main.MainModule
import com.codingwithmitch.openapi.di.main.MainScope
import com.codingwithmitch.openapi.di.main.MainViewModelModule
import com.example.movielist.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {




    @MainScope
    @ContributesAndroidInjector(
        modules = [MainModule::class, MainFragmentBuildersModule::class, MainViewModelModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}