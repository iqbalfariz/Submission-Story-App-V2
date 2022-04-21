package com.izo.submissionstoryapp.view

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.izo.submissionstoryapp.data.StoryRepository
import com.izo.submissionstoryapp.di.Injection
import com.izo.submissionstoryapp.view.addstory.AddStoryViewModel
import com.izo.submissionstoryapp.view.login.LoginViewModel
import com.izo.submissionstoryapp.view.main.MainViewModel
import com.izo.submissionstoryapp.view.register.RegisterViewModel
import com.izo.submissionstoryapp.view.splashscreen.SplashScreenViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "application")

class ViewModelFactory private constructor(private val storyRepository: StoryRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(SplashScreenViewModel::class.java) -> {
                SplashScreenViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(
                        context,
                        context.dataStore
                    )
                )
            }.also { instance = it }
    }
}