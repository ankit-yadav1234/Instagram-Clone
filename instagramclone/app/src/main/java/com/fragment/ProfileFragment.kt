package com.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.Models.User
import com.example.adapters.ViewPagerAdapter
import com.example.instagramclone.R
import com.example.instagramclone.SignUpActivity
import com.example.instagramclone.databinding.FragmentProfileBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.utils.USER_NODE

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Set up click listener for editing the profile
        binding.editProfile.setOnClickListener {
            val intent = Intent(activity, SignUpActivity::class.java)
            intent.putExtra("MODE", 1) // Send 'MODE' to indicate update profile mode
            startActivity(intent)
            activity?.finish()  // Finish current activity to go to SignUpActivity
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        // Fetch current user's profile data from Firestore
        FirebaseFirestore.getInstance().collection(USER_NODE)
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                user?.let {
                    binding.name.text = it.name
                    binding.bio.text = it.email
                    if (!it.image.isNullOrEmpty()) {
                        Picasso.get().load(it.image).into(binding.profileImage)
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle error if needed, like showing a Toast
            }
        viewPagerAdapter = ViewPagerAdapter(requireActivity())
        viewPagerAdapter.addFragment(MyPostFragment(), "MY POST")
        viewPagerAdapter.addFragment(MyReelsFragment(), "MY REELS")

        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = viewPagerAdapter.getPageTitle(position)
        }.attach()
    }
}
