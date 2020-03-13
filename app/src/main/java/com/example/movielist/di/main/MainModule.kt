package com.codingwithmitch.openapi.di.main

import com.example.movielist.api.OpenApiMainService
import com.example.movielist.persistence.AppDatabase
import com.example.movielist.persistence.MovieDao
import com.example.movielist.repository.MovieRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideTest(retrofitBuilder: Retrofit.Builder):OpenApiMainService{
        return retrofitBuilder
            .build()
            .create(OpenApiMainService::class.java)
    }

//
    @MainScope
    @Provides
    fun providemovieRepository(
        openApiMainService: OpenApiMainService,
        movieDao:MovieDao
    ): MovieRepository {
        return MovieRepository(openApiMainService,movieDao)
    }


    @MainScope
    @Provides
    fun provideMovieDao(db: AppDatabase): MovieDao {
        return db.getMovieDao()
    }

//
//    @MainScope
//    @Provides
//    fun provideBlogRepository(
//        openApiMainService: OpenApiMainService,
//        blogPostDao: BlogPostDao,
//        sessionManager: SessionManager
//    ): BlogRepository {
//        return BlogRepository(openApiMainService, blogPostDao, sessionManager)
//    }
//
//    @MainScope
//    @Provides
//    fun provideCreateBlogRepository(
//        openApiMainService: OpenApiMainService,
//        blogPostDao: BlogPostDao,
//        sessionManager: SessionManager
//    ): CreateBlogRepository {
//        return CreateBlogRepository(openApiMainService, blogPostDao, sessionManager)
//    }
}

















