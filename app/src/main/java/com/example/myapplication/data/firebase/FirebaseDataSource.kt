package com.example.myapplication.data.firebase

import com.example.myapplication.data.entity.User
import com.google.firebase.database.*
import com.example.myapplication.data.MyResult
import com.example.myapplication.core.utils.wrapSnapshotToClass


class FirebaseDataSource {
    companion object {
        val dbInstance = FirebaseDatabase.getInstance()
    }

    private fun refToPath(path: String): DatabaseReference {
        return dbInstance.reference.child(path)
    }

    private fun <T> attachValueListenerToBlock(resultClassName: Class<T>, b: ((MyResult<T>) -> Unit)): ValueEventListener {
        return (object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) { b.invoke(MyResult.Error(error.message)) }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (wrapSnapshotToClass(resultClassName, snapshot) == null) {
                    b.invoke(MyResult.Error(message = snapshot.key))
                } else {
                    b.invoke(MyResult.Success(
                        wrapSnapshotToClass(
                            resultClassName,
                            snapshot
                        )
                    ))
                }
            }
        })
    }

    fun <T> attachUserInfoObserver(resultClassName: Class<T>, userID: String, refObs: FirebaseReferenceValueObserver, b: ((MyResult<T>) -> Unit)) {
        val listener = attachValueListenerToBlock(resultClassName, b)
        refObs.start(listener, refToPath("users/$userID/info"))
    }

    fun updateNewUser(user: User) {
        refToPath("users/${user.info.id}").setValue(user)
    }

    fun updateUserProfileImageUrl(userID: String, url: String) {
        refToPath("users/$userID/info/profileImageUrl").setValue(url)
    }

    fun updateUserStatus(userID: String, status: String) {
        refToPath("users/$userID/info/status").setValue(status)
    }

}