package com.example.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.Models.Post
import com.example.instagramclone.databinding.PostPagerItemBinding
import com.squareup.picasso.Picasso

class PostPagerAdapter(
    private val context: Context,
    private val postList: List<Post>
) : RecyclerView.Adapter<PostPagerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: PostPagerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostPagerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = postList[position]
        Picasso.get()
            .load(post.imageUrl) // make sure imageUrl field is correct
            .into(holder.binding.postImageView)
    }

    override fun getItemCount(): Int = postList.size
}
