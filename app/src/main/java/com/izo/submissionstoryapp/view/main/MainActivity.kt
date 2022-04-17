package com.izo.submissionstoryapp.view.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.izo.submissionstoryapp.R
import com.izo.submissionstoryapp.data.ListStoryItem
import com.izo.submissionstoryapp.data.local.UserPreference
import com.izo.submissionstoryapp.databinding.ActivityMainBinding
import com.izo.submissionstoryapp.view.ViewModelFactory
import com.izo.submissionstoryapp.view.addstory.AddStoryActivity
import com.izo.submissionstoryapp.view.welcome.WelcomeActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        supportActionBar?.title = "Dicoding Story App"

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
                var auth = "Bearer ${user.token}"
                setUpStories(auth)
        }

        mainBinding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }


    }

    private fun setUpStories(auth: String) {
        mainViewModel.getStories(auth).observe(this, { listStory ->
            setUpRv(listStory)
        })
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
        mainViewModel.logout()
        val moveToWelcome = Intent(this, WelcomeActivity::class.java)
        startActivity(moveToWelcome)
        finish()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val TAG = "MainActivity"
    }
}