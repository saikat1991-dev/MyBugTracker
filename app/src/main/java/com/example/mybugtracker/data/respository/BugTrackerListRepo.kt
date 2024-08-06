package com.example.mybugtracker.data.respository

import android.icu.text.CaseMap.Title
import com.example.mybugtracker.data.api.NetworkService
import com.example.mybugtracker.data.model.BugInsertResponse
import com.example.mybugtracker.data.model.BugItemResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BugTrackerListRepo @Inject constructor(private val networkService: NetworkService) {

    suspend fun fetchBugTrackerList() : Flow<List<BugItemResponse>>{
        return flow {
            emit(networkService.fetchSheetData("get"))
        }.map {
            it.bugItems
        }
    }

    suspend fun insertBug(title: String,description : String) : Flow<BugInsertResponse>{
        return flow {
            emit(networkService.insertDataToGSheet("create",title,description,"https://dummyimage.com/550x350/3399ff/000"))
        }.map {
            it
        }
    }

}