package com.izo.submissionstoryapp.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.izo.submissionstoryapp.databinding.ActivityWelcomeBinding
import com.izo.submissionstoryapp.view.login.LoginActivity
import com.izo.submissionstoryapp.view.register.RegisterActivity


class WelcomeActivity : AppCompatActivity() {

    private lateinit var welcomeBinding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        welcomeBinding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(welcomeBinding.root)

        setUpView()

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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(welcomeBinding.imageView2, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login =
            ObjectAnimator.ofFloat(welcomeBinding.btnLogin, View.ALPHA, 1f).setDuration(1000)
        val signup =
            ObjectAnimator.ofFloat(welcomeBinding.btnSignUp, View.ALPHA, 1f).setDuration(1000)
        val welcome1 =
            ObjectAnimator.ofFloat(welcomeBinding.tvWelcome, View.ALPHA, 1f).setDuration(1000)
        val welcome2 =
            ObjectAnimator.ofFloat(welcomeBinding.tvWelcome2, View.ALPHA, 1f).setDuration(1000)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(welcome1, welcome2, together)
            start()
        }
    }
}