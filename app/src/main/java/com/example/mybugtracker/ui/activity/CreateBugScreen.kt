package com.example.mybugtracker.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.mybugtracker.R
import com.example.mybugtracker.utils.AppConstant.PLACE_HOLDER_IMAGE
import com.example.mybugtracker.utils.ImageType
import com.example.mybugtracker.viewModel.BugListViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects


@Composable
fun InsertBugScreen(bugListViewModel: BugListViewModel) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val galleryImageUri = remember { mutableStateOf<Uri?>(null) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            Log.i("saikat", "ImageUri $uri")
            galleryImageUri.value = uri
        }
    val file = context.createFileImage()
    val cameraUri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )
    val cameraImageUri = remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            cameraImageUri.value = cameraUri
            Log.i("saikat", "captureImageUri ${cameraImageUri.value}")
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show()
                cameraLauncher.launch(cameraUri)
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.bug),
            contentDescription = "",
            modifier = Modifier
                .height(200.dp)
                .width(200.dp)
        )

        Text(text = "Create Bug", fontWeight = FontWeight.Bold)
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
        }, modifier = Modifier
            .height(80.dp)
            .padding(start = 10.dp, end = 10.dp)
        )
        Spacer(modifier = Modifier.padding(top = 6.dp))
        Image(
            rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .crossfade(false)
                    .data(
                        if (bugListViewModel.imageType == ImageType.IMAGE_GALLERY) {
                            galleryImageUri.value ?: PLACE_HOLDER_IMAGE
                        } else if (bugListViewModel.imageType == ImageType.IMAGE_CAMERA) {
                            cameraImageUri.value ?: PLACE_HOLDER_IMAGE
                        } else {
                            PLACE_HOLDER_IMAGE
                        }
                    ).build(),
                filterQuality = FilterQuality.High
            ),
            contentDescription = "", contentScale = ContentScale.Crop, modifier = Modifier
                .width(200.dp)
                .size(120.dp)
                .padding(4.dp)
        )

        Row {
            Button(onClick = {
                bugListViewModel.onPickImageClick()
            }) {
                Text(text = "Pick Image")
            }
            Spacer(modifier = Modifier.padding(6.dp))
            Button(onClick = {
                when (bugListViewModel.imageType) {
                    ImageType.IMAGE_GALLERY -> {
                        galleryImageUri.value?.let { bugListViewModel.uploadImageToFireStorage(it) }
                            ?: kotlin.run {
                                Toast.makeText(context, "Pick any image", Toast.LENGTH_SHORT).show()
                            }
                    }

                    ImageType.IMAGE_CAMERA -> {
                        cameraImageUri.value.let { bugListViewModel.uploadImageToFireStorage(it) }
                    }

                    ImageType.NONE -> {
                        Toast.makeText(context, "Pick any image", Toast.LENGTH_SHORT).show()
                    }
                }

                Log.i("saikat", "ImageUrl ${galleryImageUri.value}")
            }) {
                Text(text = "Upload Image")
            }
        }

        Spacer(modifier = Modifier.padding(top = 6.dp))
        Button(
            onClick = {
                val url = bugListViewModel.getUploadedImage()
                if (title.isEmpty()) {
                    Toast.makeText(context, "Add title", Toast.LENGTH_SHORT).show()
                } else if (description.isEmpty()) {
                    Toast.makeText(context, "Add description", Toast.LENGTH_SHORT).show()
                } else if (url.isEmpty()) {
                    Toast.makeText(context, "Pick any image", Toast.LENGTH_SHORT).show()
                } else {
                    bugListViewModel.insertBugItems(title, description, url)
                    Log.i("saikat", "getImageUrl $url")
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = "Add")
        }

        if (bugListViewModel.isDialogShow) {
            CustomDialog(onCameraClick = {
                bugListViewModel.imageType = ImageType.IMAGE_CAMERA
                bugListViewModel.onDismissDialog()
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(cameraUri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
                onGalleryClick = {
                    bugListViewModel.imageType = ImageType.IMAGE_GALLERY
                    bugListViewModel.onDismissDialog()
                    launcher.launch("image/*")
                },
                dismissDialog = {
                    bugListViewModel.onDismissDialog()
                }
            )
        }
        if (bugListViewModel.isProgressDialogShow) {
            ProgressDialog(onDismissDialog = {
                bugListViewModel.dismissProgressDialog()
            })
        }
    }


}

@SuppressLint("SimpleDateFormat")
fun Context.createFileImage(): File {
    val timeStamp = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}