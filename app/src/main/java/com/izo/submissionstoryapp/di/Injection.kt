package com.izo.submissionstoryapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.izo.submissionstoryapp.data.StoryRepository
import com.izo.submissionstoryapp.data.local.database.StoryDatabase
import com.izo.submissionstoryapp.data.local.UserPreference
import com.izo.submissionstoryapp.data.remote.ApiConfig
import com.izo.submissionstoryapp.view.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context, dataStore: DataStore<Preferences>): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val appExecutors = AppExecutors()
        val preferences = UserPreference.getInstance(dataStore)
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService, appExecutors, preferences, database)
    }
}