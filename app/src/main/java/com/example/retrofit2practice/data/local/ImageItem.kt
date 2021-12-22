package com.example.retrofit2practice.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
data class ImageItem (
    var imageUrl: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null )