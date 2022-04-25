package com.izo.submissionstoryapp.view.main

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.izo.submissionstoryapp.data.ListStoryItem
import com.izo.submissionstoryapp.data.StoriesResponse
import com.izo.submissionstoryapp.data.StoryRepository
import com.izo.submissionstoryapp.data.local.UserModel
import com.izo.submissionstoryapp.data.local.UserPreference
import com.izo.submissionstoryapp.data.remote.ApiConfig
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {



    fun getUser() = storyRepository.getUser().asLiveData()

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }






    fun getStoriesPaging(auth: String): LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStoriesPaging(auth).cachedIn(viewModelScope)


}