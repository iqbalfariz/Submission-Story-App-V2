package com.izo.submissionstoryapp.view.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.izo.submissionstoryapp.data.ListStoryItem
import com.izo.submissionstoryapp.databinding.ItemRowRvBinding
import com.izo.submissionstoryapp.view.detail.DetailActivity
import com.izo.submissionstoryapp.view.detail.DetailActivity.Companion.EXTRA_DETAIL

class MainAdapter(private val listStories: List<ListStoryItem>): RecyclerView.Adapter<MainAdapter.ViewHolder>() {

//    private lateinit var onItemClickCallback: OnItemClickCallback
//
//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        val binding = ItemRowRvBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
//        val data = listStories[position]
//        Glide.with(holder.itemView.context)
//            .load(data.photoUrl)
//            .into(holder.binding.ivPhoto)
//
//        holder.binding.tvName.text = data.name

//        holder.itemView.setOnClickListener {
//            onItemClickCallback.onItemClicked(listStories[holder.adapterPosition])
//        }
        holder.bind(listStories[position])

    }

    override fun getItemCount(): Int = listStories.size

    inner class ViewHolder(var binding: ItemRowRvBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(listStoryItem: ListStoryItem) {
            Glide.with(itemView.context)
                .load(listStoryItem.photoUrl)
                .into(binding.ivPhoto)

            binding.tvName.text = listStoryItem.name
            binding.tvDescription.text = listStoryItem.description

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(EXTRA_DETAIL, listStoryItem)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivPhoto, "photo"),
                        Pair(binding.tvName, "name"),
                        Pair(binding.tvDescription, "description"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())

            }
        }
    }

//    interface OnItemClickCallback {
//        fun onItemClicked(data: ListStoryItem)
//    }
}

