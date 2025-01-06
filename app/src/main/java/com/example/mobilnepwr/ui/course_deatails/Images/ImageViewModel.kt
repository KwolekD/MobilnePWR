package com.example.mobilnepwr.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilnepwr.data.AppDatabase
import com.example.mobilnepwr.data.images.Image
import com.example.mobilnepwr.data.images.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ImageViewModel(application: Application) : AndroidViewModel(application) {
    private val imageDao = AppDatabase.getDatabase(application).imageDao()
    private val repository = ImageRepository(imageDao)

    val allImages: Flow<List<Image>> = repository.getAllItemsStream()

    fun insertImage(image: Image) {
        viewModelScope.launch {
            repository.insertItem(image)
        }
    }

    fun deleteImage(image: Image) {
        viewModelScope.launch {
            repository.deleteItem(image)
        }
    }

    fun updateImage(image: Image) {
        viewModelScope.launch {
            repository.updateItem(image)
        }
    }
}

