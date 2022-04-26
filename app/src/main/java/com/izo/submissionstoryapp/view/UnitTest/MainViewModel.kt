package com.izo.submissionstoryapp.view.UnitTest

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.izo.submissionstoryapp.data.ListStoryItem
import com.izo.submissionstoryapp.data.StoryRepository
import kotlinx.coroutines.launch

class MainViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getUser() = storyRepository.getUser().asLiveData()

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }

    fun getStoriesPaging(auth: String, loc: Int): LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStoriesPaging(auth, loc).cachedIn(viewModelScope)


}