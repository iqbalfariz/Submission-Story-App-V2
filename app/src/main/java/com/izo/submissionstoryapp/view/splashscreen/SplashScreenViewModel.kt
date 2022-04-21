package com.izo.submissionstoryapp.view.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.izo.submissionstoryapp.data.StoryRepository
import com.izo.submissionstoryapp.data.local.UserModel
import com.izo.submissionstoryapp.data.local.UserPreference

class SplashScreenViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getUser() = storyRepository.getUser().asLiveData()
}