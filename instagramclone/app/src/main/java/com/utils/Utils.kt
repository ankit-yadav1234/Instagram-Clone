package com.utils

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

// Upload image
fun uploadImage(uri: Uri, folderName: String, callback: (String?) -> Unit) {
    val storageRef = Firebase.storage.reference.child(folderName).child(UUID.randomUUID().toString())

    storageRef.putFile(uri)
        .addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                callback(uri.toString())
            }.addOnFailureListener {
                callback(null)
            }
        }
        .addOnFailureListener {
            callback(null)
        }
}

// Upload video with progress dialog
fun uploadVideo(uri: Uri, folderName: String, context: Context, callback: (String?) -> Unit) {
    val storageRef = Firebase.storage.reference.child(folderName).child(UUID.randomUUID().toString())

    val progressDialog = ProgressDialog(context)
    progressDialog.setTitle("Uploading Video...")
    progressDialog.setCancelable(false)
    progressDialog.show()

    val uploadTask = storageRef.putFile(uri)

    uploadTask.addOnProgressListener {
        val progress = (100.0 * it.bytesTransferred / it.totalByteCount).toInt()
        progressDialog.setMessage("Uploaded $progress%")
    }

    uploadTask.addOnSuccessListener {
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            progressDialog.dismiss()
            callback(uri.toString())
        }.addOnFailureListener {
            progressDialog.dismiss()
            callback(null)
        }
    }.addOnFailureListener {
        progressDialog.dismiss()
        callback(null)
    }
}



