package com.example.myapplication.data.repository

import com.example.myapplication.data.MyResult
import com.example.myapplication.data.entity.User
import com.example.myapplication.data.entity.UserInfo
import com.example.myapplication.data.firebase.FirebaseDataSource
import com.example.myapplication.data.firebase.FirebaseReferenceValueObserver

class DatabaseRepository {
    private val firebaseDatabaseService = FirebaseDataSource()

    fun updateNewUser(user: User) {
        firebaseDatabaseService.updateNewUser(user)
    }

    fun loadAndObserveUserInfo(userID: String, observer: FirebaseReferenceValueObserver, b: ((MyResult<UserInfo>) -> Unit)) {
        firebaseDatabaseService.attachUserInfoObserver(UserInfo::class.java, userID, observer, b)
    }

    fun updateUserStatus(userID: String, status: String) {
        firebaseDatabaseService.updateUserStatus(userID, status)
    }

    fun updateUserProfileImageUrl(userID: String, url: String){
        firebaseDatabaseService.updateUserProfileImageUrl(userID, url)
    }

}