package com.example.myapplication.data.repository

import android.net.Uri
import com.example.myapplication.data.firebase.FirebaseStorageSource
import com.example.myapplication.data.MyResult

class StorageRepository {
    private val firebaseStorageService = FirebaseStorageSource()

    fun updateUserProfileImage(userID: String, byteArray: ByteArray, b: (MyResult<Uri>) -> Unit) {
        b.invoke(MyResult.Loading)
        firebaseStorageService.uploadUserImage(userID, byteArray).addOnSuccessListener {
            b.invoke((MyResult.Success(it)))
        }.addOnFailureListener {
            b.invoke(MyResult.Error(it.message))
        }
    }
}