package com.example.retrofit2practice.favorite

import android.app.Application
import androidx.lifecycle.*
import com.example.retrofit2practice.others.Event
import com.example.retrofit2practice.others.Response
import com.example.retrofit2practice.repositories.DefaultImageRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val imageRepository = DefaultImageRepository.getRepository(application)

    val fabImages = imageRepository.observeAllImageItems()

    private val _navigateToImagePick = MutableLiveData<Event<Response<String>>>()
    val navigateToImagePick: LiveData<Event<Response<String>>> = _navigateToImagePick

    var inputKeyWord = MutableLiveData<String>()

    private val _deleteStatus = MutableLiveData<Event<Response<String>>>()
    val deleteStatus: LiveData<Event<Response<String>>> = _deleteStatus

    fun navigateToImagePick() {
        if (inputKeyWord.value.isNullOrEmpty()) {
            _navigateToImagePick.value = Event(Response.error("入力にエラーがあります。"))
        } else {
            _navigateToImagePick.value = Event(Response.success(inputKeyWord.value))
            inputKeyWord.value = ""
        }
    }

    fun deleteItemsFromDb() {
        viewModelScope.launch {
            imageRepository.deleteImageItem()
            _deleteStatus.value = Event(Response.success("削除が完了しました。"))
        }
    }
}
