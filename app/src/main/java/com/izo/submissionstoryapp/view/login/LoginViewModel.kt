package com.izo.submissionstoryapp.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.izo.submissionstoryapp.data.StoryRepository
import com.izo.submissionstoryapp.data.local.UserModel
import com.izo.submissionstoryapp.data.local.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun loginUser(user: UserModel) {
        viewModelScope.launch {
            storyRepository.loginUser(user)
        }
    }

    fun postDataLogin(email: String, password: String) = storyRepository.postDataLogin(email, password)


}