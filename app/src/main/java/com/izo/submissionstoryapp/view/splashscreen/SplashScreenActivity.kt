package com.izo.submissionstoryapp.view.splashscreen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.izo.submissionstoryapp.R
import com.izo.submissionstoryapp.data.local.UserPreference
import com.izo.submissionstoryapp.databinding.ActivityRegisterBinding
import com.izo.submissionstoryapp.view.ViewModelFactory
import com.izo.submissionstoryapp.view.main.MainActivity
import com.izo.submissionstoryapp.view.register.RegisterViewModel
import com.izo.submissionstoryapp.view.welcome.WelcomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        setUpView()

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val splashScreenViewModel: SplashScreenViewModel by viewModels {
            factory
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
//            splashScreenViewModel = ViewModelProvider(
//                this@SplashScreenActivity,
//                ViewModelFactory(UserPreference.getInstance(dataStore))
//            )[SplashScreenViewModel::class.java]
//
            splashScreenViewModel.getUser().observe(this@SplashScreenActivity) { user ->
                if (user.isLogin) {
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@SplashScreenActivity, WelcomeActivity::class.java))
                    finish()
                }
            }


        }

    }

    private fun setUpView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}