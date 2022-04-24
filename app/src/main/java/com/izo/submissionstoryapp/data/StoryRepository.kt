package com.izo.submissionstoryapp.data

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.izo.submissionstoryapp.data.local.StoryDatabase
import com.izo.submissionstoryapp.data.local.UserModel
import com.izo.submissionstoryapp.data.local.UserPreference
import com.izo.submissionstoryapp.data.remote.ApiConfig
import com.izo.submissionstoryapp.data.remote.ApiService
import com.izo.submissionstoryapp.view.login.LoginActivity
import com.izo.submissionstoryapp.view.main.MainActivity
import com.izo.submissionstoryapp.view.register.RegisterActivity
import com.izo.submissionstoryapp.view.utils.AppExecutors
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val appExecutors: AppExecutors,
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase

) {


    // get list stories
    fun getStories(auth: String, page: Int): LiveData<Result<List<ListStoryItem>>> {
        val result = MutableLiveData<Result<List<ListStoryItem>>>()
        val client = apiService.getStories(auth, page)
        result.value = Result.Loading
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    result.value = Result.Success(responseBody.listStory)
                } else {
                    result.value = Result.Error(response.message().toString())
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }

        })
        return result
    }

    // get list story use paging 3

    suspend fun getStoriesPaging(auth: String): StoriesResponse {
        return apiService.getStoriesPaging(auth, 1, 5, 1)
    }

    // Get List

    // post data regis user
    fun postDataRegis(name: String, email: String, password: String):  LiveData<Result<RegisterResponse>>{
        val result = MutableLiveData<Result<RegisterResponse>>()
        val client = apiService.postRegis(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    result.value = Result.Success(responseBody)
                } else {
                    result.value = Result.Error(response.message().toString())
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }

        })
        return result
    }

    // Upload gambar dan desk
    fun uploadImage(auth: String, text: String, file: File): LiveData<Result<RegisterResponse>>{
            val description = text.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            val result = MutableLiveData<Result<RegisterResponse>>()
            result.value = Result.Loading
            val service = apiService.addStories(auth, imageMultipart, description)
            service.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            result.value = Result.Success(responseBody)
                        }
                    } else {
                        result.value = Result.Error(response.message().toString())
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    result.value = Result.Error(t.message.toString())
                }
            })

        return result
    }

    // Post Data Login

    fun postDataLogin(email: String, password: String): LiveData<Result<LoginResponse>> {
        val result = MutableLiveData<Result<LoginResponse>>()
        val client = apiService.postLogin(email, password)
        result.value = Result.Loading
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    result.value = Result.Success(responseBody)
                } else {
                    result.value = Result.Error(response.message().toString())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }

        })
        return result
    }


    // Mendapatkan user
    fun getUser(): Flow<UserModel> = userPreference.getUser()


    // Memasukkan data login user
    suspend fun loginUser(user: UserModel){
        userPreference.loginUser(user)
    }

    // logout user
    suspend fun logout(){
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            appExecutors: AppExecutors,
            userPreference: UserPreference,
            storyDatabase: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, appExecutors, userPreference,storyDatabase)
            }.also { instance = it }
    }
}