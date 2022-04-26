package com.izo.submissionstoryapp.view.addstory

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest.create
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.izo.submissionstoryapp.data.Result
import com.izo.submissionstoryapp.databinding.ActivityAddStoryBinding
import com.izo.submissionstoryapp.view.ViewModelFactory
import com.izo.submissionstoryapp.view.UnitTest.MainActivity
import java.io.File
import java.util.concurrent.TimeUnit


class AddStoryActivity : AppCompatActivity() {

    private lateinit var addStoryBinding: ActivityAddStoryBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    private var latitude: Float = 0.1F
    private var longitude: Float = 0.1F
    private var getFile: File? = null
    private var auth = "token"
    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            addStoryBinding.ivPreview.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            addStoryBinding.ivPreview.setImageURI(selectedImg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addStoryBinding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(addStoryBinding.root)
        supportActionBar?.elevation = 0f
        supportActionBar?.title = "Add Story"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val addStoryViewModel: AddStoryViewModel by viewModels {
            factory
        }

        addStoryViewModel.getUser().observe(this) { user ->
            auth = "Bearer ${user.token}"
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        addStoryBinding.btnCamera.setOnClickListener {
            takePhoto()
        }

        addStoryBinding.btnGalery.setOnClickListener {
            startGalery()
        }

        addStoryBinding.btnUpload.setOnClickListener {
            val descriptionText = addStoryBinding.edDesc.text.toString()
            if (getFile != null) {
                val file = reduceFileImage(getFile as File)
                addStoryViewModel.uploadImage(auth, descriptionText, file, latitude, longitude).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }
                            is Result.Success -> {
                                showLoading(false)
                                Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
//                                Toast.makeText(this, "lat : $latitude, lon : $longitude", Toast.LENGTH_SHORT).show()
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
            } else {
                Toast.makeText(this, "Silakan pilih gambar dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        createLocationRequest()

        addStoryBinding.btnLocation.setOnClickListener {
            createLocationRequest()
        }

    }

    private fun createLocationRequest() {
        locationRequest = create().apply {
            interval = TimeUnit.SECONDS.toMillis(1)
            maxWaitTime = TimeUnit.SECONDS.toMillis(1)
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        client.checkLocationSettings(builder.build())
            .addOnSuccessListener {
                fetchLocation()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        resolutionLauncher.launch(
                            IntentSenderRequest.Builder(exception.resolution).build()
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        Toast.makeText(this, sendEx.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private val resolutionLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            when (result.resultCode) {
                RESULT_OK ->
                    Log.i("MapsActivity", "onActivityResult: All location settings are satisfied.")
                RESULT_CANCELED ->
                    Toast.makeText(
                        this,
                        "Anda harus mengaktifkan GPS untuk menggunakan aplikasi ini!",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    fetchLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    fetchLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun fetchLocation() {

        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
//                    Toast.makeText(
//                        this,
//                        "lat: ${location.latitude}, lon : ${location.longitude}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    latitude = location.latitude.toFloat()
                    longitude = location.longitude.toFloat()
                } else {
                    Toast.makeText(
                        this,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    private fun startGalery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "com.izo.submissionstoryapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        addStoryBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


//    private val requestPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissions ->
//            when {
//                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
//                    // Precise location access granted.
//                    getMyLocation()
//                }
//                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
//                    // Only approximate location access granted.
//                    getMyLocation()
//                }
//                else -> {
//                    // No location access granted.
//                }
//            }
//        }
//
//    private fun checkPermission(permission: String): Boolean {
//        return ContextCompat.checkSelfPermission(
//            this,
//            permission
//        ) == PackageManager.PERMISSION_GRANTED
//    }
//
//
//    private fun getMyLocation() {
//        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
//            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
//        ){
//            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//                if (location != null) {
//                    Toast.makeText(this, "lat : ${location.latitude}, long : ${location.longitude}", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(
//                        this,
//                        "Location is not found. Try Again",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        } else {
//            requestPermissionLauncher.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            )
//        }
//    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}