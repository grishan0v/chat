package com.example.myapplication.data.repository

import com.example.myapplication.core.dto.LoginData
import com.example.myapplication.data.firebase.FirebaseAuthSource
import com.example.myapplication.data.firebase.FirebaseAuthStateObserver
import com.google.firebase.auth.FirebaseUser
import com.example.myapplication.data.MyResult
import com.example.myapplication.core.dto.UserData

class AuthRepository {
    private val firebaseAuthService = FirebaseAuthSource()

    fun observeAuthState(
        stateObserver: FirebaseAuthStateObserver,
        b: ((MyResult<FirebaseUser>) -> Unit)
    ) {
        firebaseAuthService.attachAuthStateObserver(stateObserver, b)
    }

    fun loginUser(loginData: LoginData, b: ((MyResult<FirebaseUser>) -> Unit)) {
        b.invoke(MyResult.Loading)
        firebaseAuthService.loginWithEmailAndPassword(loginData).addOnSuccessListener {
            b.invoke(MyResult.Success(it.user))
        }.addOnFailureListener {
            b.invoke(MyResult.Error(message = it.message))
        }
    }

    fun createUser(userData: UserData, b: ((MyResult<FirebaseUser>) -> Unit)) {
        b.invoke(MyResult.Loading)
        firebaseAuthService.createUser(userData).addOnSuccessListener {
            b.invoke(MyResult.Success(it.user))
        }.addOnFailureListener {
            b.invoke(MyResult.Error(message = it.message))
        }
    }

    fun logoutUser() {
        firebaseAuthService.logout()
    }
}