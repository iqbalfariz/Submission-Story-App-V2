package com.izo.submissionstoryapp.view.register

import androidx.lifecycle.ViewModel
import com.izo.submissionstoryapp.data.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun postDataRegis(name: String, email: String, password: String) =
        storyRepository.postDataRegis(name, email, password)

}