package com.example.retrofit2practice.repositories

import androidx.lifecycle.LiveData
import com.example.retrofit2practice.data.local.ImageItem
import com.example.retrofit2practice.data.remote.ImageResponse
import com.example.retrofit2practice.others.Response


interface ImageRepository {

    suspend fun insertImageItem(imageItem: ImageItem)

    suspend fun deleteImageItem()

    fun observeAllImageItems(): LiveData<List<ImageItem>>

    suspend fun searchForImage(imageQuery: String): Response<ImageResponse>
}