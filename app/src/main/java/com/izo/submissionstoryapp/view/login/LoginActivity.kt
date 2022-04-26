package com.izo.submissionstoryapp.view.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.izo.submissionstoryapp.data.LoginResult
import com.izo.submissionstoryapp.data.Result
import com.izo.submissionstoryapp.data.local.UserModel
import com.izo.submissionstoryapp.databinding.ActivityLoginBinding
import com.izo.submissionstoryapp.view.ViewModelFactory
import com.izo.submissionstoryapp.view.Main.MainActivity
import com.izo.submissionstoryapp.view.register.RegisterActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding

    val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    val loginViewModel: LoginViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        setUpView()



        loginBinding.btnLogin.setOnClickListener { view ->
//            postDataLogin(
//                loginBinding.edEmail.text.toString(),
//                loginBinding.edPassword.text.toString()
//            )
            loginViewModel.postDataLogin(
                loginBinding.edEmail.text.toString(),
                loginBinding.edPassword.text.toString()
            ).observe(this) {result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            setDataLogin(result.data.loginResult)
                            Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        }
                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(
                                this,
                                "Terjadi kesalahan" + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        loginBinding.btnSignUp.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
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


//    private fun postDataLogin(email: String, password: String) {
//        showLoading(true)
//        val client = ApiConfig.getApiService().postLogin(email, password)
//        client.enqueue(object : Callback<LoginResponse> {
//            override fun onResponse(
//                call: Call<LoginResponse>,
//                response: Response<LoginResponse>
//            ) {
//                showLoading(false)
//                val responseBody = response.body()
//                if (response.isSuccessful && responseBody != null) {
//                    Toast.makeText(this@LoginActivity, "login success", Toast.LENGTH_SHORT).show()
//                    setDataLogin(responseBody.loginResult)
//                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                    finish()
//                } else {
//                    Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
//                    Log.e(TAG, "onFailure1: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                showLoading(false)
//                Log.e(TAG, "onFailure2: ${t.message}")
//            }
//
//        })
//    }

    private fun setDataLogin(loginResult: LoginResult) {
        loginViewModel.loginUser(
            UserModel(
                loginResult.name,
                loginResult.userId,
                loginResult.token,
                true
            )
        )
    }

    private fun showLoading(isLoading: Boolean) {
        loginBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}