package com.example.mybugtracker.di.component

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ComponentActivity
import com.example.mybugtracker.di.ActivityScope
import com.example.mybugtracker.di.module.ActivityModule
import com.example.mybugtracker.ui.ActivityContext
import com.example.mybugtracker.ui.activity.BugItemListActivity
import com.example.mybugtracker.ui.activity.InsertBugActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: BugItemListActivity)
    fun inject(activity: InsertBugActivity)
}