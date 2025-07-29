package com.example.Reels

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.Models.Reel
import com.example.instagramclone.MainActivity
import com.example.instagramclone.databinding.ActivityReelsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ReelsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReelsBinding
    private var selectedVideoUri: Uri? = null
    private var uploadedVideoUrl: String? = null

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // Video picker launcher
    private val selectVideoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedVideoUri = uri
            binding.selectImage.setImageURI(uri)
            uploadVideoToFirebase(uri)
        } else {
            Toast.makeText(this, "No video selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Select reel button
        binding.selectReel.setOnClickListener {
            selectVideoLauncher.launch("video/*")
        }

        // Upload/post button
        binding.postButton.setOnClickListener {
            val caption = binding.caption.editText?.text.toString().trim()

            when {
                uploadedVideoUrl.isNullOrEmpty() -> {
                    Toast.makeText(this, "Please upload a video first", Toast.LENGTH_SHORT).show()
                }
                caption.isEmpty() -> {
                    Toast.makeText(this, "Please enter a caption", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val reel = Reel(
                        videoUrl = uploadedVideoUrl!!,
                        caption = caption,
                        uploaderId = auth.currentUser?.uid ?: "",
                        timestamp = System.currentTimeMillis()
                    )

                    firestore.collection("reels")
                        .add(reel)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Reel posted successfully!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to post reel: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }

        // Cancel button
        binding.cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun uploadVideoToFirebase(uri: Uri) {
        val reelId = UUID.randomUUID().toString()
        val filePath = "Reels/$reelId.mp4"
        val storageRef = storage.reference.child(filePath)

        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    uploadedVideoUrl = downloadUri.toString()
                    Toast.makeText(this, "Video uploaded successfully", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Video upload failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
