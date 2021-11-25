package com.example.myapplication.data.repository

import com.example.myapplication.data.entity.User
import com.example.myapplication.data.firebase.FirebaseDataSource

class DatabaseRepository {
    private val firebaseDatabaseService = FirebaseDataSource()

    fun updateNewUser(user: User) {
        firebaseDatabaseService.updateNewUser(user)
    }
}