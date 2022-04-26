package com.izo.submissionstoryapp.view.utils

import com.izo.submissionstoryapp.data.ListStoryItem
import com.izo.submissionstoryapp.data.LoginResponse
import com.izo.submissionstoryapp.data.LoginResult
import com.izo.submissionstoryapp.data.RegisterResponse
import com.izo.submissionstoryapp.data.local.UserModel

object DataDummy {

    // Data dummy ketika user login

    fun generateDummyGetUser(): UserModel {
        val items = UserModel (
            "nama",
            "userId",
            "token",
            true
                )
        return items
    }

    // Data dummy response api get story
    fun generateDummyStoriesResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "name $i",
                "description $i",
                "photo $i",
                "createdAt $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        return items
    }

    // Data dummy ketika user register
    fun generateDummyRegisterResponse(): RegisterResponse{
        val result = RegisterResponse (
            false,
            "User created"
                )
        return result
    }

    // Data dummy ketika user login
    fun generateDummyLoginResponse(): LoginResponse {
        val loginResult = LoginResult(
            "name",
            "userId",
            "token"
        )
        val result = LoginResponse (
            false,
            "success",
            loginResult
                )
        return result
    }
}