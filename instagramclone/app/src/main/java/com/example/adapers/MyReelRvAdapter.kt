package com.example.adapers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.Models.Reel
import com.example.instagramclone.databinding.MyPostRvDesignBinding
import com.squareup.picasso.Picasso

class MyReelRvAdapter(
    private val context: Context,
    private val reelList: ArrayList<Reel>
) : RecyclerView.Adapter<MyReelRvAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: MyPostRvDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get()
            .load(reelList[position].thumbnailUrl)  // 👈 Make sure Reel model has this field
            .into(holder.binding.postImage)
    }

    override fun getItemCount(): Int = reelList.size
}
