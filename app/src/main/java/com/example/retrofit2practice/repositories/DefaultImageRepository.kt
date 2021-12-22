package com.example.retrofit2practice.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.retrofit2practice.data.remote.PixabayApi
import com.example.retrofit2practice.data.local.ImageItem
import com.example.retrofit2practice.data.local.ImageItemDao
import com.example.retrofit2practice.data.local.ImageItemDatabase
import com.example.retrofit2practice.data.remote.ImageResponse
import com.example.retrofit2practice.others.Response

class DefaultImageRepository private constructor(application: Application): ImageRepository {
    private var imageItemDao: ImageItemDao = Room.databaseBuilder(application.applicationContext,
    ImageItemDatabase::class.java, "image.tb")
        .build()
        .imageDao()

    companion object {
        @Volatile
        private var INSTANCE: DefaultImageRepository? = null

        fun getRepository(application: Application): DefaultImageRepository {
            return INSTANCE ?: synchronized(this){
                DefaultImageRepository(application).also {
                    INSTANCE = it
                }
            }
        }
    }

    override suspend fun insertImageItem(imageItem: ImageItem) {
        imageItemDao.insertImageItem(imageItem)
    }

    override suspend fun deleteImageItem() {
        imageItemDao.deleteImageItem()
    }

    override fun observeAllImageItems(): LiveData<List<ImageItem>> {
        return imageItemDao.observeAllImageItems()
    }

    override suspend fun searchForImage(imageQuery: String): Response<ImageResponse> {
        return try {
            val response = PixabayApi.retrofitService.searchForImage(imageQuery)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Response.success(response.body())
                } ?: Response.error("画像が取得できません。")
            } else {
                Response.error("画像が取得できません")
            }
        }catch (e: Exception){
                Response.error("インターネットに接続されていません")
            }
    }
}