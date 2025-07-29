package com.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.Models.Post
import com.example.adapers.MyPostRvAdapter
import com.example.instagramclone.databinding.FragmentMyPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyPostFragment : Fragment() {

    private lateinit var binding: FragmentMyPostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPostBinding.inflate(inflater, container, false)

        // RecyclerView setup
        val postList = ArrayList<Post>()
        val adapter = MyPostRvAdapter(requireContext(), postList)
        binding.rv.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter = adapter

        // Get current user ID
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        // Fetch only current user's posts from "Posts" collection
        Firebase.firestore.collection("Posts")
            .whereEqualTo("uploaderId", uid)
            .get()
            .addOnSuccessListener { result ->
                val tempList = arrayListOf<Post>()
                for (document in result.documents) {
                    val post = document.toObject(Post::class.java)
                    if (post != null) {
                        tempList.add(post)
                    }
                }
                postList.addAll(tempList)
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }

        return binding.root
    }
}
