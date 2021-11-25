package com.example.myapplication.data.firebase

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthStateObserver {
    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var instance: FirebaseAuth? = null

    fun start(valueEventListener: FirebaseAuth.AuthStateListener, instance: FirebaseAuth) {
        this.authListener = valueEventListener
        this.instance = instance
        this.instance!!.addAuthStateListener(authListener!!)
    }

    fun clear() {
        authListener?.let { instance?.removeAuthStateListener(it) }
    }
}