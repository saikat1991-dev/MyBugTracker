package com.example.mybugtracker.data.model

import com.google.gson.annotations.SerializedName

data class BugInsertResponse(
    @SerializedName("status")
    val status : Int,
    @SerializedName("message")
    val message : String
)