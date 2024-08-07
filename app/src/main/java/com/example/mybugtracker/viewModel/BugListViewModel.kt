package com.example.mybugtracker.viewModel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybugtracker.data.model.BugInsertResponse
import com.example.mybugtracker.data.model.BugItemResponse
import com.example.mybugtracker.data.respository.BugTrackerListRepo
import com.example.mybugtracker.utils.ImageType
import com.example.mybugtracker.utils.Resource
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.UUID

class BugListViewModel(private val bugTrackerListRepo: BugTrackerListRepo) : ViewModel() {

    private val _bugItemList = MutableStateFlow<Resource<List<BugItemResponse>>>(Resource.loading())

    val bugItemList: StateFlow<Resource<List<BugItemResponse>>> = _bugItemList

    private val _insertBug = MutableStateFlow<Resource<BugInsertResponse>>(Resource.doNothing())

    val insertBug : StateFlow<Resource<BugInsertResponse>> = _insertBug

    private val _uploadImge = MutableStateFlow<Resource<String>>(Resource.doNothing())

    val uploadImage : StateFlow<Resource<String>> = _uploadImge

    var _uplaodedImage by mutableStateOf("")
        private set

    fun getUploadedImage () : String {
        return _uplaodedImage
    }

    fun setUploadedImageUrl (imageUrl : String) {
        _uplaodedImage = imageUrl
    }

    var isDialogShow by mutableStateOf(false)
        private set

    var imageType by mutableStateOf(ImageType.NONE)

    fun onPickImageClick() {
        isDialogShow = true
    }

    fun onDismissDialog(){
        isDialogShow = false
    }

    var isProgressDialogShow by mutableStateOf(false)
        private set

    fun showProgressDialog(){
        isProgressDialogShow = true
    }

    fun dismissProgressDialog(){
        isProgressDialogShow = false
    }

    lateinit var storageRefernce  : StorageReference

    fun fetchBugItems() {
        viewModelScope.launch {
            bugTrackerListRepo.fetchBugTrackerList()
                .catch { e ->
                    _bugItemList.value = Resource.error(e.toString())
                }
                .collect {
                    _bugItemList.value = Resource.success(it)
                }
        }
    }

    fun insertBugItems(title: String,description : String,imageUrl : String) {
        _insertBug.value = Resource.loading()
        viewModelScope.launch {
            bugTrackerListRepo.insertBug(title,description,imageUrl)
                .catch { e ->
                    _insertBug.value = Resource.error(e.toString())
                }
                .collect {
                    _insertBug.value = Resource.success(it)
                }
        }
    }

    fun uploadImageToFireStorage(imageUri : Uri){
        _uploadImge.value = Resource.loading()
        storageRefernce = FirebaseStorage.getInstance().reference
        storageRefernce.child("Bug_no${UUID.randomUUID()}").putFile(imageUri)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { uri ->
                    _uploadImge.value = Resource.success(uri.toString())
                }
            }
            .addOnFailureListener{
                _uploadImge.value = Resource.error(it.message.toString())
            }

    }
}