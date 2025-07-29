package com.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Models.Reel
import com.example.adapters.ReelPagerAdapter
import com.example.instagramclone.databinding.FragmentReelBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReelFragment : Fragment() {

    private lateinit var binding: FragmentReelBinding
    private lateinit var adapter: ReelPagerAdapter
    private var reelList = ArrayList<Reel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReelBinding.inflate(inflater, container, false)

        adapter = ReelPagerAdapter(requireContext(), reelList)
        binding.reelRv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.reelRv.adapter = adapter

        // Load from Firebase
        Firebase.firestore.collection("Reels")
            .get()
            .addOnSuccessListener { snapshot ->
                reelList.clear()
                for (doc in snapshot.documents) {
                    val reel = doc.toObject(Reel::class.java)
                    if (reel != null) {
                        reelList.add(reel)
                    }
                }
                adapter.notifyDataSetChanged()
            }

        return binding.root
    }
}
