package com.example.myapplication.data.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class FirebaseReferenceValueObserver {
    private var valueEventListener: ValueEventListener? = null
    private var dbRef: DatabaseReference? = null

    fun start(valueEventListener: ValueEventListener, reference: DatabaseReference) {
        reference.addValueEventListener(valueEventListener)
        this.valueEventListener = valueEventListener
        this.dbRef = reference
    }

    fun clear() {
        valueEventListener?.let { dbRef?.removeEventListener(it) }
        valueEventListener = null
        dbRef = null
    }
}