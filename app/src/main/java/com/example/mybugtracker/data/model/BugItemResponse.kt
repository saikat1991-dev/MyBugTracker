package com.example.mybugtracker.data.model

import com.google.gson.annotations.SerializedName

data class BugItemResponse(
    @SerializedName("title")
    val title : String,
    @SerializedName("description")
    val description : String,
    @SerializedName("stdImageUrl")
    val imageUrl : String = ""
)
