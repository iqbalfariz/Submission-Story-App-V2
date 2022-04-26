package com.izo.submissionstoryapp.view.register

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
import com.izo.submissionstoryapp.data.Result
import com.izo.submissionstoryapp.databinding.ActivityRegisterBinding
import com.izo.submissionstoryapp.view.ViewModelFactory
import com.izo.submissionstoryapp.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding
    val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    val registerViewModel: RegisterViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        setUpView()

        registerBinding.btnSignUp.setOnClickListener { view ->
//            postDataRegis(
//                registerBinding.edName.text.toString(),
//                registerBinding.edEmail.text.toString(),
//                registerBinding.edPassword.text.toString()
//            )

            registerViewModel.postDataRegis(
                registerBinding.edName.text.toString(),
                registerBinding.edEmail.text.toString(),
                registerBinding.edPassword.text.toString()
            ).observe(this) {result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
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

        registerBinding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
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

//    private fun postDataRegis(name: String, email: String, password: String) {
//        showLoading(true)
//        val client = ApiConfig.getApiService().postRegis(name, email, password)
//        client.enqueue(object : Callback<RegisterResponse> {
//            override fun onResponse(
//                call: Call<RegisterResponse>,
//                response: Response<RegisterResponse>
//            ) {
//                showLoading(false)
//                val responseBody = response.body()
//                if (response.isSuccessful && responseBody != null) {
//                    Toast.makeText(this@RegisterActivity, responseBody.message, Toast.LENGTH_SHORT)
//                        .show()
//                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
//                } else {
//                    Toast.makeText(
//                        this@RegisterActivity,
//                        "Email is already taken",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    Log.e(TAG, "onFailure1: ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
//                showLoading(false)
//                Log.e(TAG, "onFailure2: ${t.message}")
//            }
//
//        })
//    }

    private fun showLoading(isLoading: Boolean) {
        registerBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    companion object {
        const val TAG = "RegisterActivity"
    }
}