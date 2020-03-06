package com.codingwithmitch.openapi.di.main

import androidx.lifecycle.ViewModel
import com.example.movielist.di.ViewModelKey
import com.example.movielist.ui.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {


//    @Binds
//    @IntoMap
//    @ViewModelKey(AccountViewModel::class)
//    abstract fun bindAccountViewModel(accoutViewModel: AccountViewModel): ViewModel
//

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindBlogViewModel(blogViewModel: MainViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @ViewModelKey(CreateBlogViewModel::class)
//    abstract fun bindCreateBlogViewModel(createBlogViewModel: CreateBlogViewModel): ViewModel
}








