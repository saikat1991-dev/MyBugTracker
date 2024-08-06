package com.example.mybugtracker

import android.app.Application
import com.example.mybugtracker.di.component.ApplicationComponent
import com.example.mybugtracker.di.component.DaggerApplicationComponent
import com.example.mybugtracker.di.module.ApplicationModule

class BugTrackerApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }

    private fun injectDependencies(){
        applicationComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }

}