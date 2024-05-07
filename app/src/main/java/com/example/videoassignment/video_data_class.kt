package com.example.videoassignment

import kotlinx.serialization.Serializable

@Serializable
data class video_data_class(
    val id:Int,
    val Video_URL:String,
    val Channel_Name:String,
    val Title:String,
    val Description:String,
    val Likes:Int
)
