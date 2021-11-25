package com.example.myapplication.data.repository

import com.example.myapplication.data.dto.LoginData
import com.example.myapplication.data.firebase.FirebaseAuthSource
import com.example.myapplication.data.firebase.FirebaseAuthStateObserver
import com.google.firebase.auth.FirebaseUser
import com.example.myapplication.data.Result
import com.example.myapplication.data.dto.UserData

class AuthRepository {
    private val firebaseAuthService = FirebaseAuthSource()

    fun observeAuthState(
        stateObserver: FirebaseAuthStateObserver,
        b: ((Result<FirebaseUser>) -> Unit)
    ) {
        firebaseAuthService.attachAuthStateObserver(stateObserver, b)
    }

    fun loginUser(loginData: LoginData, b: ((Result<FirebaseUser>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseAuthService.loginWithEmailAndPassword(loginData).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(Result.Error(message = it.message))
        }
    }

    fun createUser(userData: UserData, b: ((Result<FirebaseUser>) -> Unit)) {
        b.invoke(Result.Loading)
        firebaseAuthService.createUser(userData).addOnSuccessListener {
            b.invoke(Result.Success(it.user))
        }.addOnFailureListener {
            b.invoke(Result.Error(message = it.message))
        }
    }

    fun logoutUser() {
        firebaseAuthService.logout()
    }
}