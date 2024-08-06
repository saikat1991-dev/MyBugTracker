package com.example.mybugtracker.data.api

import com.example.mybugtracker.data.model.BugInsertResponse
import com.example.mybugtracker.data.model.BugListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {

    @GET("exec")
    suspend fun insertDataToGSheet(@Query("action") action : String,
                                   @Query("title") title : String,
                                   @Query("description") description: String,
                                   @Query("stdImageUrl") stdImageUrl : String
                                   ) : BugInsertResponse

    @GET("exec")
    suspend fun fetchSheetData(@Query("action")action: String) : BugListResponse

}