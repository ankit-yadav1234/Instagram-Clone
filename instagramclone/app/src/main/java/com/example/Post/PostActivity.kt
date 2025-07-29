package com.example.Post

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.instagramclone.R
import com.example.instagramclone.databinding.ActivityPostBinding
import com.example.Models.User
import com.example.Models.Post
import com.example.instagramclone.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.utils.uploadImage

class PostActivity : AppCompatActivity() {

    private val POST_FOLDER = "PostImages"  // Folder for uploaded images
    private val POST_COLLECTION = "posts"   // Firestore collection name

    private val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }

    private var imageUrl: String? = null  // 🔧 Store the uploaded image URL
    private var user = User()  // Example user, replace with actual user logic

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            uploadImage(uri, POST_FOLDER) { url: String? ->
                if (url != null) {
                    imageUrl = url
                    binding.selectImage.setImageURI(uri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@PostActivity, MainActivity::class.java))
            finish()
        }

        binding.selectImage.setOnClickListener {
            launcher.launch("image/*")
            binding.cancelButton.setOnClickListener {
                startActivity(Intent(this@PostActivity, MainActivity::class.java))
                finish()
            }
        }

        binding.postButton.setOnClickListener {
            val caption = binding.caption.editText?.text.toString()

            if (!imageUrl.isNullOrEmpty() && caption.isNotEmpty()) {
                val post = Post(imageUrl!!, caption)

                val firestore = FirebaseFirestore.getInstance()
                val userId = FirebaseAuth.getInstance().currentUser?.uid

                if (userId != null) {
                    firestore.collection(POST_COLLECTION)
                        .document()
                        .set(post)
                        .addOnSuccessListener {
                            // Optional: Add to user's own posts
                            firestore.collection(userId)
                                .document()
                                .set(post)
                                .addOnSuccessListener {
                                    startActivity(Intent(this@PostActivity, MainActivity::class.java))
                                    finish()
                                }
                        }
                }
            }
        }
    }
}
