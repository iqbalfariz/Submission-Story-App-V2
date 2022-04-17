package com.izo.submissionstoryapp.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.izo.submissionstoryapp.data.local.UserPreference
import com.izo.submissionstoryapp.databinding.ActivityWelcomeBinding
import com.izo.submissionstoryapp.view.ViewModelFactory
import com.izo.submissionstoryapp.view.home.MainActivity
import com.izo.submissionstoryapp.view.login.LoginActivity
import com.izo.submissionstoryapp.view.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class WelcomeActivity : AppCompatActivity() {

    private lateinit var welcomeBinding: ActivityWelcomeBinding
    private lateinit var welcomeViewModel: WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        welcomeBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(welcomeBinding.root)

        welcomeViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[WelcomeViewModel::class.java]

//        welcomeViewModel.getUser().observe(this) { user ->
//            if (user.isLogin) {
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//            }
//        }

        welcomeBinding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        welcomeBinding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(welcomeBinding.imageView2, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(welcomeBinding.btnLogin, View.ALPHA, 1f).setDuration(2000)
        val signup = ObjectAnimator.ofFloat(welcomeBinding.btnSignUp, View.ALPHA, 1f).setDuration(2000)
        val welcome1 = ObjectAnimator.ofFloat(welcomeBinding.tvWelcome, View.ALPHA, 1f).setDuration(2000)
        val welcome2 = ObjectAnimator.ofFloat(welcomeBinding.tvWelcome2, View.ALPHA, 1f).setDuration(2000)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(welcome1, welcome2, together)
            start()
        }
    }
}