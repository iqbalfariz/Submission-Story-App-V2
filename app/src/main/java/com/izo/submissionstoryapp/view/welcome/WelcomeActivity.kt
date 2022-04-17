package com.izo.submissionstoryapp.view.welcome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        welcomeViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        welcomeBinding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        welcomeBinding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}