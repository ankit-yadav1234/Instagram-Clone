package com.example.instagramclone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.instagramclone.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.example.Models.User
import com.utils.USER_NODE
import com.utils.USER_PROFILE_FOLDER
import com.utils.uploadImage

class SignUpActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private var user = User()
    private var isEditMode = false

    // Image picker
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER) { imageUrl ->
                if (imageUrl != null) {
                    user.image = imageUrl
                    binding.profileImage.setImageURI(uri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Detect edit mode
        if (intent.hasExtra("MODE") && intent.getIntExtra("MODE", -1) == 1) {
            isEditMode = true
            binding.elevatedButton.text = "Update Profile"

            FirebaseFirestore.getInstance().collection(USER_NODE)
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val userData = documentSnapshot.toObject(User::class.java)
                    userData?.let {
                        user = it
                        if (!it.image.isNullOrEmpty()) {
                            Picasso.get().load(it.image).into(binding.profileImage)
                        }
                        binding.usernameEditText.editText?.setText(it.name)
                        binding.emailEditText.editText?.setText(it.email)
                        binding.passwordEditText.editText?.setText(it.password)
                    }
                }
        }

        // Handle submit button
        binding.elevatedButton.setOnClickListener {
            val name = binding.usernameEditText.editText?.text.toString()
            val email = binding.emailEditText.editText?.text.toString()
            val password = binding.passwordEditText.editText?.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the information", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isEditMode) {
                // Update existing profile
                user.name = name
                user.email = email
                user.password = password

                FirebaseFirestore.getInstance().collection(USER_NODE)
                    .document(FirebaseAuth.getInstance().currentUser!!.uid)
                    .set(user)
                    .addOnSuccessListener {
                        startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))
                        finish()
                    }
            } else {
                // New registration
                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { result ->
                        if (result.isSuccessful) {
                            user.name = name
                            user.email = email
                            user.password = password

                            FirebaseFirestore.getInstance().collection(USER_NODE)
                                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                                .set(user)
                                .addOnSuccessListener {
                                    startActivity(Intent(this@SignUpActivity, HomeActivity::class.java))
                                    finish()
                                }
                        } else {
                            Toast.makeText(this, result.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        // Upload profile image
        binding.addImage.setOnClickListener {
            launcher.launch("image/*")
        }

        // Navigate to login
        binding.login.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
            finish()
        }
    }
}
