package com.example.mybugtracker.data.respository

import com.example.mybugtracker.data.api.NetworkService
import com.example.mybugtracker.data.model.BugInsertResponse
import com.example.mybugtracker.data.model.BugItemResponse
import com.example.mybugtracker.utils.AppConstant.CREATE
import com.example.mybugtracker.utils.AppConstant.GET
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BugTrackerListRepo @Inject constructor(private val networkService: NetworkService) {

    suspend fun fetchBugTrackerList() : Flow<List<BugItemResponse>>{
        return flow {
            emit(networkService.fetchSheetData(GET))
        }.map {
            it.bugItems
        }
    }

    suspend fun insertBug(title: String,description : String,imageUrl : String) : Flow<BugInsertResponse>{
        return flow {
            emit(networkService.insertDataToGSheet(CREATE,title,description,imageUrl))
        }.map {
            it
        }
    }

}