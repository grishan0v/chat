package com.example.myapplication.data.firebase

import com.example.myapplication.data.dto.UserData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.myapplication.data.MyResult
import com.example.myapplication.data.dto.LoginData

class FirebaseAuthSource {
    companion object {
        val authInstance = FirebaseAuth.getInstance()
    }

    private fun attachAuthObserver(b: ((MyResult<FirebaseUser>) -> Unit)): FirebaseAuth.AuthStateListener {
        return FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                b.invoke(MyResult.Error("No user"))
            } else {
                b.invoke(MyResult.Success(it.currentUser))
            }
        }
    }

    fun loginWithEmailAndPassword(loginData: LoginData): Task<AuthResult> {
        return authInstance.signInWithEmailAndPassword(loginData.email, loginData.password)
    }

    fun createUser(userData: UserData): Task<AuthResult> {
        return authInstance.createUserWithEmailAndPassword(userData.email, userData.password)
    }

    fun logout() {
        authInstance.signOut()
    }

    fun attachAuthStateObserver(
        firebaseAuthStateObserver: FirebaseAuthStateObserver,
        b: ((MyResult<FirebaseUser>) -> Unit)
    ) {
        val listener = attachAuthObserver(b)
        firebaseAuthStateObserver.start(listener, authInstance)
    }
}