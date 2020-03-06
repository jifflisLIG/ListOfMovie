package com.codingwithmitch.openapi.di.main

import com.example.movielist.api.OpenApiMainService
import com.example.movielist.repository.MovieRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideTest(retrofitBuilder: Retrofit.Builder):OpenApiMainService{
        return retrofitBuilder.build().create(OpenApiMainService::class.java)
    }



//    @MainScope
//    @Provides
//    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder): OpenApiMainService {
//        return retrofitBuilder
//            .build()
//            .create(OpenApiMainService::class.java)
//    }
//
    @MainScope
    @Provides
    fun providemovieRepository(
        openApiMainService: OpenApiMainService
    ): MovieRepository {
        return MovieRepository(openApiMainService)
    }

//
//    @MainScope
//    @Provides
//    fun provideBlogPostDao(db: AppDatabase): BlogPostDao {
//        return db.getBlogPostDao()
//    }
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

















