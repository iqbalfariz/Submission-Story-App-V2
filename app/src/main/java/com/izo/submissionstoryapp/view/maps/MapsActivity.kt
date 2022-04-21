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
import com.izo.submissionstoryapp.data.Result
import com.izo.submissionstoryapp.databinding.ActivityMapsBinding
import com.izo.submissionstoryapp.view.ViewModelFactory
import com.izo.submissionstoryapp.view.main.MainViewModel


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapsBinding: ActivityMapsBinding
    val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    val mapsViewModel: MapsViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mapsBinding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(mapsBinding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Get user token
        mapsViewModel.getUser().observe(this) {user ->
            val auth = "Bearer ${user.token}"
            setUpMaps(auth)
        }

    }

    private fun setUpMaps(auth: String) {
        // Get user story location
        mapsViewModel.getStories(auth, 1).observe(this) {result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(this, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
                        Log.e("MapsActivity", "hasil maps : ${result.data}")
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

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}