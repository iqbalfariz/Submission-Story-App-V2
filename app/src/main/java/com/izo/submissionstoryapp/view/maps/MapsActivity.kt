package com.izo.submissionstoryapp.view.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.izo.submissionstoryapp.R
import com.izo.submissionstoryapp.data.ListStoryItem
import com.izo.submissionstoryapp.databinding.ActivityMapsBinding
import com.izo.submissionstoryapp.view.ViewModelFactory
import com.izo.submissionstoryapp.data.Result

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    val mapsViewModel: MapsViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        // Get user token
        mapsViewModel.getUser().observe(this) {user ->
            val auth = "Bearer ${user.token}"
            setUpDataSpace(auth, 1)
        }

    }

    private fun setUpDataSpace(auth: String, loc: Int) {
        mapsViewModel.getStories(auth, loc).observe(this) {result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(this, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
                        Log.e("MapsActivity", "hasil maps : ${result.data}")
                        // add marker story
                        markerMaps(result.data)
                    }
                    is Result.Error -> {
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }

    private fun markerMaps(dataSpace: List<ListStoryItem>) {
        Log.e("MapsActivity", "cek marker : ${dataSpace.size}")
        val datafirst = dataSpace[0]
        val dataFirstSpace = LatLng(datafirst.lat, datafirst.lon)
        for (i in 0 until dataSpace.size) {
            val dataNow = dataSpace[i]
            val storySpace = LatLng(dataNow.lat, dataNow.lon)
            mMap.addMarker(
                MarkerOptions()
                    .position(storySpace)
                    .title(dataNow.name)
                    .snippet(dataNow.description)
            )
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dataFirstSpace, 15f))
    }
}