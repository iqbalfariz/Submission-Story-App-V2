package com.izo.submissionstoryapp.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.izo.submissionstoryapp.R
import com.izo.submissionstoryapp.data.ListStoryItem
import com.izo.submissionstoryapp.data.Result
import com.izo.submissionstoryapp.databinding.ActivityMainBinding
import com.izo.submissionstoryapp.view.ViewModelFactory
import com.izo.submissionstoryapp.view.addstory.AddStoryActivity
import com.izo.submissionstoryapp.view.maps.MapsActivity
import com.izo.submissionstoryapp.view.welcome.WelcomeActivity



class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    val mainViewModel: MainViewModel by viewModels {
        factory
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        supportActionBar?.title = "Dicoding Story App"

        mainViewModel.getUser().observe(this) { user ->
            val auth = "Bearer ${user.token}"
            setUpStories(auth)
        }

        mainBinding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }


    }

    private fun setUpStories(auth: String) =
        mainViewModel.getStories(auth, 0).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        setUpRv(result.data)
//                        Toast.makeText(this, "Data tidak berhasil dimuat", Toast.LENGTH_SHORT).show()
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

    private fun setUpRv(listStory: List<ListStoryItem>) {
        val homeAdapter = MainAdapter(listStory)
        mainBinding.rvUser.adapter = homeAdapter
        val layoutManager = LinearLayoutManager(this)
        mainBinding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        mainBinding.rvUser.addItemDecoration(itemDecoration)
        mainBinding.rvUser.setHasFixedSize(true)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_maps -> {
                val intentToMaps = Intent(this, MapsActivity::class.java)
                startActivity(intentToMaps)
            }
            R.id.menu_logout -> {
                mainViewModel.logout()
                val moveToWelcome = Intent(this, WelcomeActivity::class.java)
                startActivity(moveToWelcome)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        mainBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG = "MainActivity"
    }
}