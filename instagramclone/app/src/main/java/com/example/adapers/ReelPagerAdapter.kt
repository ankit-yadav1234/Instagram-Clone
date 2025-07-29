package com.example.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.MediaController
import androidx.recyclerview.widget.RecyclerView
import com.example.Models.Reel
import com.example.instagramclone.databinding.ReelPagerItemBinding

class ReelPagerAdapter(
    private val context: Context,
    private val reels: List<Reel>
) : RecyclerView.Adapter<ReelPagerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ReelPagerItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReelPagerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reel = reels[position]
        val videoUri = Uri.parse(reel.videoUrl)

        holder.binding.videoView.setVideoURI(videoUri)
        holder.binding.videoView.setMediaController(MediaController(context))
        holder.binding.videoView.setOnPreparedListener { it.isLooping = true }
        holder.binding.videoView.start()
    }

    override fun getItemCount(): Int = reels.size
}
