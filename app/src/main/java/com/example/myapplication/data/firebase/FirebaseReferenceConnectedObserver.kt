package com.example.myapplication.data.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class FirebaseReferenceConnectedObserver {

    private var valueEventListener: ValueEventListener? = null
    private var dbRef: DatabaseReference? = null
    private var userRef: DatabaseReference? = null

    fun start(userID: String) {
        this.userRef = FirebaseDataSource.dbInstance.reference.child("users/$userID/info/online")
        this.valueEventListener = getEventListener(userID)
        this.dbRef = FirebaseDataSource.dbInstance.getReference(".info/connected").apply { addValueEventListener(valueEventListener!!) }
    }

    private fun getEventListener(userID: String): ValueEventListener {
        return (object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    FirebaseDataSource.dbInstance.reference.child("users/$userID/info/online").setValue(true)
                    userRef?.onDisconnect()?.setValue(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun clear() {
        valueEventListener?.let { dbRef?.removeEventListener(it) }
        userRef?.setValue(false)
        valueEventListener = null
        dbRef = null
        userRef = null
    }
}