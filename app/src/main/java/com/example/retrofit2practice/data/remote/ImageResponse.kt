package com.example.retrofit2practice.data.remote

data class ImageResponse (
    val total: Int,
    val totalHits: Int,
    val hits: List<ImageResult>,
        )