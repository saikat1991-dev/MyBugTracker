package com.example.mybugtracker.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mybugtracker.BugTrackerApplication
import com.example.mybugtracker.data.model.BugItemResponse
import com.example.mybugtracker.di.component.DaggerActivityComponent
import com.example.mybugtracker.di.module.ActivityModule
import com.example.mybugtracker.ui.theme.MyBugTrackerTheme
import com.example.mybugtracker.utils.Resource
import com.example.mybugtracker.utils.Status
import com.example.mybugtracker.viewModel.BugListViewModel
import javax.inject.Inject

class BugItemListActivity : ComponentActivity() {
    @Inject
    lateinit var bugTrackerViewModel: BugListViewModel


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependency()
        super.onCreate(savedInstanceState)
        setContent {

            MyBugTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                        TopAppBar(
                            colors = TopAppBarColors(Color.Gray,Color.White,Color.White,Color.White,Color.Unspecified),
                            title = {
                                Text(
                                    text = "Grid View",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )
                            },
                            actions = {
                                IconButton(
                                    onClick = {
                                       startActivity(Intent(this@BugItemListActivity,InsertBugActivity::class.java))
                                    }
                                ) {
                                    Icon(Icons.Filled.Add,
                                        contentDescription = null,
                                        tint = Color.White
                                        )
                                }
                            }
                            )
                    }, content = {
                        Column(modifier = Modifier
                            .padding(it)
                            .fillMaxSize()) {
                            SetObserver()
                        }

                        })
                }
            }
        }
    }



    @SuppressLint("SuspiciousIndentation")
    @Composable
    private fun SetObserver(){
        val bugItems : State<Resource<List<BugItemResponse>>> = bugTrackerViewModel.bugItemList.collectAsState()
                    when(bugItems.value.status){
                        Status.SUCCESS -> {
                            bugItems.value.data?.let { bugItemList ->
                                Log.i("saikat","BugItemList $bugItemList")
                                ShowButItemScree(bugItemList)
                            }
                        }

                        Status.ERROR -> {
                            Toast.makeText(this@BugItemListActivity, bugItems.value.message, Toast.LENGTH_LONG)
                                .show()
                        }
                        Status.LOADING -> {
                            ShowProgressBar()
                        }
                    }
    }

    @Composable
    private fun ShowProgressBar(){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
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
