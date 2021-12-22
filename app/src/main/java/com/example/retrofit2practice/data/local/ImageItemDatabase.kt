package com.example.retrofit2practice.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ImageItem::class],
    version = 1,
    exportSchema = false
)
abstract class ImageItemDatabase: RoomDatabase() {
    abstract fun imageDao(): ImageItemDao
}