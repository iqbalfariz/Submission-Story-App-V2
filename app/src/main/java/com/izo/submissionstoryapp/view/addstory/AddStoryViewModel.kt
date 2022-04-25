package com.izo.submissionstoryapp.view.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.izo.submissionstoryapp.data.StoryRepository
import com.izo.submissionstoryapp.data.local.UserModel
import com.izo.submissionstoryapp.data.local.UserPreference
import kotlinx.coroutines.launch
import java.io.File

class AddStoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getUser() = storyRepository.getUser().asLiveData()

    fun uploadImage(auth: String, text: String, file: File, latitude: Float, longitude: Float) = storyRepository.uploadImage(auth, text, file, latitude, longitude)
}