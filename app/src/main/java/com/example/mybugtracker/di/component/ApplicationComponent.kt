package com.example.mybugtracker.di.component

import android.content.Context
import com.example.mybugtracker.BugTrackerApplication
import com.example.mybugtracker.data.respository.BugTrackerListRepo
import com.example.mybugtracker.di.module.ApplicationModule
import com.example.mybugtracker.ui.ApplicationContext
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(applicationModule: BugTrackerApplication)

    @ApplicationContext
    fun getContext(): Context

    fun getBugTrackerRepo(): BugTrackerListRepo
}