package com.izo.submissionstoryapp.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.izo.submissionstoryapp.R
import com.izo.submissionstoryapp.data.ListStoryItem
import com.izo.submissionstoryapp.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        val detail = intent.getParcelableExtra<ListStoryItem>(EXTRA_DETAIL) as ListStoryItem
        Glide.with(this)
            .load(detail.photoUrl)
            .into(detailBinding.ivPhoto)

        detailBinding.tvName.text = detail.name
        detailBinding.tvDeskripsi.text = detail.description

    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}