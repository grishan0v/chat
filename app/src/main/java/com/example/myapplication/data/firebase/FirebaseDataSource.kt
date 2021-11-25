package com.example.myapplication.data.firebase

import com.example.myapplication.data.entity.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class FirebaseDataSource {
    companion object {
        val dbInstance = FirebaseDatabase.getInstance()
    }

    private fun refToPath(path: String): DatabaseReference {
        return dbInstance.reference.child(path)
    }

    fun updateNewUser(user: User) {
        refToPath("users/${user.info.id}").setValue(user)
    }
}