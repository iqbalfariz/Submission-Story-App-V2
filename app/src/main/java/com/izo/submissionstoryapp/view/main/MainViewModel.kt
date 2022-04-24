package com.izo.submissionstoryapp.view.main

import android.util.Log
import androidx.lifecycle.*
import com.izo.submissionstoryapp.data.ListStoryItem
import com.izo.submissionstoryapp.data.StoriesResponse
import com.izo.submissionstoryapp.data.StoryRepository
import com.izo.submissionstoryapp.data.local.UserModel
import com.izo.submissionstoryapp.data.local.UserPreference
import com.izo.submissionstoryapp.data.remote.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _story = MutableLiveData<List<ListStoryItem>>()
    var story: LiveData<List<ListStoryItem>> = _story

    fun getUser() = storyRepository.getUser().asLiveData()

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }

//    fun getStories(auth: String, loc: Int) = storyRepository.getStories(auth, loc)

    fun getStoriesPaging(auth: String) {
        viewModelScope.launch {
            _story.postValue(storyRepository.getStoriesPaging(auth).listStory)
        }
    }


}