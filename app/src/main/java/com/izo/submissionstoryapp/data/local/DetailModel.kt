package com.izo.submissionstoryapp.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailModel(
    val name: String,
    val photoUrl: String,
    val description: String
): Parcelable
