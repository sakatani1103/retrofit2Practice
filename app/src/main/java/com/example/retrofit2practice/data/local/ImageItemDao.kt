package com.example.retrofit2practice.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ImageItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageItem(imageItem: ImageItem)

    @Query("DELETE FROM item")
    suspend fun deleteImageItem()

    @Query("SELECT * FROM item")
    fun observeAllImageItems(): LiveData<List<ImageItem>>
}