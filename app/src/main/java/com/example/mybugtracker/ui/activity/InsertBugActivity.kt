package com.example.mybugtracker.ui.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mybugtracker.BugTrackerApplication
import com.example.mybugtracker.R
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
                    InsertBugScreen()
                    SetObserver()
                }
            }
        }

    }

    companion object {
        const val GALLERY_IMAGE_REQ_CODE = 102;
    }


    @Preview
    @Composable
    fun InsertBugScreen() {
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var imageUri = remember { mutableStateOf<Uri?>(null) }
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            Log.i("saikat","ImageUri $uri")
            imageUri.value = uri
        }
        val context = LocalContext.current
        val placeHolderImage = "https://craftsnippets.com/articles_images/placeholder/placeholder.jpg"

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.bug),
                contentDescription ="",
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp))

            Text(text = "Insert Bug", fontWeight = FontWeight.Bold)
            OutlinedTextField(value = title, onValueChange = {
                title = it
            }, label = {
                Text(text = "Title")
            })
            Spacer(modifier = Modifier.padding(top = 6.dp))

            OutlinedTextField(value = description, onValueChange = {
                description = it
            }, label = {
                Text(text = "Description")
            }, modifier = Modifier.height(80.dp)
                )
            Spacer(modifier = Modifier.padding(top = 6.dp))
            Image(rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .crossfade(false)
                    .data(imageUri.value?:placeHolderImage).build(), filterQuality = FilterQuality.High ),
                contentDescription = "", contentScale = ContentScale.Crop, modifier = Modifier
                    .width(200.dp)
                    .size(120.dp)
                    .padding(4.dp))
            Button(onClick = {
                bugListViewModel.onPickImageClick()
            }) {
                Text(text = "Pick Image")
            }
            Spacer(modifier = Modifier.padding(top = 6.dp))
            Button(onClick = {
                Log.i("saikat","ImageUrl ${imageUri.value}")
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
            ) {
                Text(text = "Add")
            }

            if(bugListViewModel.isDialogShow){
                CustomDialog(onCameraClick = {
                    bugListViewModel.onDismissDialog()
                },
                    onGalleryClick = {
                        bugListViewModel.onDismissDialog()
                        launcher.launch("iamges/*")
                    }
                    )
            }


        }
    }

    @Composable
    private fun SetObserver(){
        val insertBugResponse : State<Resource<BugInsertResponse>> = bugListViewModel.insertBug.collectAsState()
        when(insertBugResponse.value.status){
            Status.SUCCESS -> {
                val status = insertBugResponse.value.data?.status
                if(status == 200){
                    finish()
                }
            }

            Status.ERROR -> {
                Toast.makeText(this@InsertBugActivity, insertBugResponse.value.message, Toast.LENGTH_LONG)
                    .show()
            }
            Status.LOADING -> {

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