package com.izo.submissionstoryapp.view.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.izo.submissionstoryapp.data.StoryRepository
import kotlinx.coroutines.launch

class MapsViewModel (private val storyRepository: StoryRepository) : ViewModel() {


    fun getUser() = storyRepository.getUser().asLiveData()

    fun getStories(auth: String, loc: Int) = storyRepository.getStories(auth, loc)

}