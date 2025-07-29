package com.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.Models.Reel
import com.example.adapers.MyReelRvAdapter
import com.example.instagramclone.databinding.FragmentMyReelsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyReelsFragment : Fragment() {

    private lateinit var binding: FragmentMyReelsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyReelsBinding.inflate(inflater, container, false)

        val reelList = ArrayList<Reel>()
        val adapter = MyReelRvAdapter(requireContext(), reelList)
        binding.rv.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rv.adapter = adapter

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        Firebase.firestore.collection("Reels")
            .whereEqualTo("uploaderId", uid)
            .get()
            .addOnSuccessListener { result ->
                val tempList = arrayListOf<Reel>()
                for (document in result.documents) {
                    val reel = document.toObject(Reel::class.java)
                    if (reel != null) {
                        tempList.add(reel)
                    }
                }
                reelList.addAll(tempList)
                adapter.notifyDataSetChanged()
            }

        return binding.root
    }
}
