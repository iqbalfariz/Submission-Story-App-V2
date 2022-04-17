package com.izo.submissionstoryapp.data.remote

import com.izo.submissionstoryapp.data.LoginResponse
import com.izo.submissionstoryapp.data.RegisterResponse
import com.izo.submissionstoryapp.data.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun postRegis(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getStories(
        @Header("AUTHORIZATION") value: String
    ): Call<StoriesResponse>

    @Multipart
    @POST("stories")
    fun addStories(
        @Header("AUTHORIZATION") value: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<RegisterResponse>


}