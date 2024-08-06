package com.example.mybugtracker.di.module

import android.content.Context
import com.example.mybugtracker.BugTrackerApplication
import com.example.mybugtracker.data.api.NetworkService
import com.example.mybugtracker.ui.ApplicationContext
import com.example.mybugtracker.ui.BaseUrl
import com.example.mybugtracker.utils.AppConstant.BASE_URL
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: BugTrackerApplication) {

    @BaseUrl
    @Provides
    fun provideBaseUrl(): String = BASE_URL

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @ApplicationContext
    @Provides
    fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideNetworkService(
        @BaseUrl baseUrl: String,
        gsonConverterFactory: GsonConverterFactory
    ): NetworkService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(NetworkService::class.java)

    }
}