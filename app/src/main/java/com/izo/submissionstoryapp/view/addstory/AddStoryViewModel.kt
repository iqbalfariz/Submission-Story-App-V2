package com.izo.submissionstoryapp.view.addstory

import android.util.Log
import androidx.lifecycle.*
import com.izo.submissionstoryapp.data.ListStoryItem
import com.izo.submissionstoryapp.data.StoriesResponse
import com.izo.submissionstoryapp.data.local.UserModel
import com.izo.submissionstoryapp.data.local.UserPreference
import com.izo.submissionstoryapp.data.remote.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel (private val pref: UserPreference): ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

}