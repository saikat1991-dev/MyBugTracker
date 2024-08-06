package com.example.mybugtracker.data.model

import com.google.gson.annotations.SerializedName

data class BugListResponse(
    @SerializedName("status")
    val status : Int,
    @SerializedName("totalResults")
    val totalResults : Int,
    @SerializedName("item")
    val bugItems : List<BugItemResponse> =ArrayList()
)