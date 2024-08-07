package com.example.mybugtracker.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.mybugtracker.BugTrackerApplication
import com.example.mybugtracker.data.model.BugInsertResponse
import com.example.mybugtracker.di.component.DaggerActivityComponent
import com.example.mybugtracker.di.module.ActivityModule
import com.example.mybugtracker.ui.theme.MyBugTrackerTheme
import com.example.mybugtracker.utils.Resource
import com.example.mybugtracker.utils.Status
import com.example.mybugtracker.viewModel.BugListViewModel
import javax.inject.Inject

class InsertBugActivity : ComponentActivity() {

    @Inject
    lateinit var bugListViewModel: BugListViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependency()
        super.onCreate(savedInstanceState)
        setContent {
            MyBugTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InsertBugScreen(bugListViewModel)
                    SetObserver()
                }
            }
        }

    }

    @Composable
    private fun SetObserver() {
        val insertBugResponse: State<Resource<BugInsertResponse>> =
            bugListViewModel.insertBug.collectAsState()
        when (insertBugResponse.value.status) {
            Status.SUCCESS -> {
                bugListViewModel.dismissProgressDialog()
                val status = insertBugResponse.value.data?.status
                if (status == 200) {
                    finish()
                }
            }

            Status.ERROR -> {
                Toast.makeText(
                    this@InsertBugActivity,
                    insertBugResponse.value.message,
                    Toast.LENGTH_LONG
                )
                    .show()
            }

            Status.LOADING -> {
                bugListViewModel.showProgressDialog()
            }

            Status.DO_NOTHING -> {

            }
        }

        val uploadImageResponse: State<Resource<String>> =
            bugListViewModel.uploadImage.collectAsState()
        when (uploadImageResponse.value.status) {
            Status.SUCCESS -> {
                bugListViewModel.dismissProgressDialog()
                val url = uploadImageResponse.value.data
                url?.let {
                    bugListViewModel.setUploadedImageUrl(it)
                }

                Log.i("saikat", "success url $url")
            }

            Status.ERROR -> {
                Toast.makeText(
                    this@InsertBugActivity,
                    insertBugResponse.value.message,
                    Toast.LENGTH_LONG
                )
                    .show()
            }

            Status.LOADING -> {
                bugListViewModel.showProgressDialog()
            }

            Status.DO_NOTHING -> {

            }
        }
    }

    private fun injectDependency() {
        DaggerActivityComponent
            .builder()
            .applicationComponent((application as BugTrackerApplication).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)

    }
}