package com.example.mybugtracker.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybugtracker.data.model.BugInsertResponse
import com.example.mybugtracker.data.model.BugItemResponse
import com.example.mybugtracker.data.respository.BugTrackerListRepo
import com.example.mybugtracker.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class BugListViewModel(private val bugTrackerListRepo: BugTrackerListRepo) : ViewModel() {

    private val _bugItemList = MutableStateFlow<Resource<List<BugItemResponse>>>(Resource.loading())

    val bugItemList: StateFlow<Resource<List<BugItemResponse>>> = _bugItemList

    private val _insertBug = MutableStateFlow<Resource<BugInsertResponse>>(Resource.loading())

    val insertBug : StateFlow<Resource<BugInsertResponse>> = _insertBug

    var isDialogShow by mutableStateOf(false)
        private set

    fun onPickImageClick() {
        isDialogShow = true
    }

    fun onDismissDialog(){
        isDialogShow = false
    }


    init {
        fetchBugItems()
    }

    private fun fetchBugItems() {
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

    fun insertBugItems(title: String,description : String) {
        viewModelScope.launch {
            bugTrackerListRepo.insertBug(title,description)
                .catch { e ->
                    _insertBug.value = Resource.error(e.toString())
                }
                .collect {
                    _insertBug.value = Resource.success(it)
                }
        }
    }
}