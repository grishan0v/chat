package com.example.myapplication.data.firebase

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference

class FirebaseReferenceChildObserver {
    private var valueEventListener: ChildEventListener? = null
    private var dbRef: DatabaseReference? = null
    private var isObserving: Boolean = false

    fun start(valueEventListener: ChildEventListener, reference: DatabaseReference) {
        isObserving = true
        reference.addChildEventListener(valueEventListener)
        this.valueEventListener = valueEventListener
        this.dbRef = reference
    }

    fun clear() {
        valueEventListener?.let { dbRef?.removeEventListener(it) }
        isObserving = false
        valueEventListener = null
        dbRef = null
    }

    fun isObserving(): Boolean {
        return isObserving
    }
}