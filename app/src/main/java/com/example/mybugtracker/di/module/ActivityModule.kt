package com.example.mybugtracker.di.module

import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mybugtracker.data.respository.BugTrackerListRepo
import com.example.mybugtracker.ui.base.ViewModelProviderFactory
import com.example.mybugtracker.viewModel.BugListViewModel
import dagger.Module
import dagger.Provides


@Module
class ActivityModule(private val activity : ComponentActivity) {

    @Provides
    fun provideBugListViewModel(bugTrackerListRepo: BugTrackerListRepo) :BugListViewModel{
        return ViewModelProvider(activity,
            ViewModelProviderFactory(BugListViewModel::class){
                BugListViewModel(bugTrackerListRepo)
            }
            )[BugListViewModel::class.java]
    }
}