package com.example.retrofit2practice.imagePick

import android.app.Application
import androidx.lifecycle.*
import com.example.retrofit2practice.data.local.ImageItem
import com.example.retrofit2practice.data.remote.ImageResponse
import com.example.retrofit2practice.others.Event
import com.example.retrofit2practice.others.Response
import com.example.retrofit2practice.repositories.DefaultImageRepository
import kotlinx.coroutines.launch

class ImagePickViewModel(application: Application) : AndroidViewModel(application) {
    private val imageRepository = DefaultImageRepository.getRepository(application)

    private val _imageResponse = MutableLiveData<Event<Response<ImageResponse>>>()
    val imageResponse: LiveData<Event<Response<ImageResponse>>> = _imageResponse

    var keyword = MutableLiveData<String>()

    private val _insertStatus = MutableLiveData<Event<Response<String>>>()
    val insertStatus: LiveData<Event<Response<String>>> = _insertStatus

    fun start(searchQuery: String) {
        searchForImages(searchQuery)
        keyword.value = searchQuery
    }

    private fun searchForImages(searchQuery: String) {
        viewModelScope.launch {
            _imageResponse.value = Event(imageRepository.searchForImage(searchQuery))
        }
    }

    fun insertImageUrlToDb(imgUrl: String){
        viewModelScope.launch {
            imageRepository.insertImageItem(ImageItem(imgUrl))
            _insertStatus.value = Event(Response.success(imgUrl))
        }
    }



}