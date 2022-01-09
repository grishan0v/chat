package com.example.myapplication.data.firebase

import com.google.firebase.database.*
import com.example.myapplication.data.MyResult
import com.example.myapplication.data.entity.*
import com.example.myapplication.utils.wrapSnapshotToClass
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource


class FirebaseDataSource {
    companion object {
        val dbInstance = FirebaseDatabase.getInstance()
    }

    private fun refToPath(path: String): DatabaseReference {
        return dbInstance.reference.child(path)
    }

    private fun <T> attachValueListenerToBlock(
        resultClassName: Class<T>,
        b: ((MyResult<T>) -> Unit)
    ): ValueEventListener {
        return (object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                b.invoke(MyResult.Error(error.message))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (wrapSnapshotToClass(resultClassName, snapshot) == null) {
                    b.invoke(MyResult.Error(message = snapshot.key))
                } else {
                    b.invoke(MyResult.Success(wrapSnapshotToClass(resultClassName, snapshot)))
                }
            }
        })
    }

    private fun attachValueListenerToTaskCompletion(src: TaskCompletionSource<DataSnapshot>): ValueEventListener {
        return (object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                src.setException(Exception(error.message))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                src.setResult(snapshot)
            }
        })
    }

    private fun <T> attachChildListenerToBlock(
        resultClassName: Class<T>,
        b: ((MyResult<T>) -> Unit)
    ): ChildEventListener {
        return (object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                b.invoke(MyResult.Success(wrapSnapshotToClass(resultClassName, snapshot)))
            }

            override fun onCancelled(error: DatabaseError) {
                b.invoke(MyResult.Error(error.message))
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}
        })
    }

    fun <T> attachUserInfoObserver(
        resultClassName: Class<T>,
        userID: String,
        refObs: FirebaseReferenceValueObserver,
        b: ((MyResult<T>) -> Unit)
    ) {
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

    fun updateLastMessage(chatID: String, message: Message) {
        refToPath("chats/$chatID/lastMessage").setValue(message)
    }

    fun pushNewMessage(messagesID: String, message: Message) {
        refToPath("messages/$messagesID").push().setValue(message)
    }

    fun updateNewSentRequest(userID: String, userRequest: UserRequest) {
        refToPath("users/${userID}/sentRequests/${userRequest.userID}").setValue(userRequest)
    }

    fun updateNewNotification(otherUserID: String, userNotification: UserNotification) {
        refToPath("users/${otherUserID}/notifications/${userNotification.userID}").setValue(userNotification)
    }

    fun updateNewChat(chat: Chat) {
        refToPath("chats/${chat.info.id}").setValue(chat)
    }

    fun updateNewFriend(myUser: UserFriend, otherUser: UserFriend) {
        refToPath("users/${myUser.userID}/friends/${otherUser.userID}").setValue(otherUser)
        refToPath("users/${otherUser.userID}/friends/${myUser.userID}").setValue(myUser)
    }

    fun loadFriendsTask(userID: String): Task<DataSnapshot> {
        val src = TaskCompletionSource<DataSnapshot>()
        val listener = attachValueListenerToTaskCompletion(src)
        refToPath("users/$userID/friends").addListenerForSingleValueEvent(listener)
        return src.task
    }

    fun loadUserInfoTask(userID: String): Task<DataSnapshot> {
        val src = TaskCompletionSource<DataSnapshot>()
        val listener = attachValueListenerToTaskCompletion(src)
        refToPath("users/$userID/info").addListenerForSingleValueEvent(listener)
        return src.task
    }

    fun loadChatTask(chatID: String): Task<DataSnapshot> {
        val src = TaskCompletionSource<DataSnapshot>()
        val listener = attachValueListenerToTaskCompletion(src)
        refToPath("chats/$chatID").addListenerForSingleValueEvent(listener)
        return src.task
    }

    fun loadUsersTask(): Task<DataSnapshot> {
        val src = TaskCompletionSource<DataSnapshot>()
        val listener = attachValueListenerToTaskCompletion(src)
        refToPath("users").addListenerForSingleValueEvent(listener)
        return src.task
    }

    fun loadUserTask(userID: String): Task<DataSnapshot> {
        val src = TaskCompletionSource<DataSnapshot>()
        val listener = attachValueListenerToTaskCompletion(src)
        refToPath("users/$userID").addListenerForSingleValueEvent(listener)
        return src.task
    }

    fun <T> attachChatObserver(
        resultClassName: Class<T>,
        chatID: String,
        refObs: FirebaseReferenceValueObserver,
        b: ((MyResult<T>) -> Unit)
    ) {
        val listener = attachValueListenerToBlock(resultClassName, b)
        refObs.start(listener, refToPath("chats/$chatID"))
    }

    fun removeFriend(userID: String, friendID: String) {
        refToPath("users/${userID}/friends/$friendID").setValue(null)
        refToPath("users/${friendID}/friends/$userID").setValue(null)
    }

    fun removeSentRequest(userID: String, sentRequestID: String) {
        refToPath("users/${userID}/sentRequests/$sentRequestID").setValue(null)
    }

    fun removeChat(chatID: String) {
        refToPath("chats/$chatID").setValue(null)
    }

    fun removeMessages(messagesID: String) {
        refToPath("messages/$messagesID").setValue(null)
    }

    fun removeNotification(userID: String, notificationID: String) {
        refToPath("users/${userID}/notifications/$notificationID").setValue(null)
    }

    fun <T> attachMessagesObserver(
        resultClassName: Class<T>,
        messagesID: String,
        refObs: FirebaseReferenceChildObserver,
        b: ((MyResult<T>) -> Unit)
    ) {
        val listener = attachChildListenerToBlock(resultClassName, b)
        refObs.start(listener, refToPath("messages/$messagesID"))
    }

    fun <T> attachUserObserver(
        resultClassName: Class<T>,
        userID: String,
        refObs: FirebaseReferenceValueObserver,
        b: ((MyResult<T>) -> Unit)
    ) {
        val listener = attachValueListenerToBlock(resultClassName, b)
        refObs.start(listener, refToPath("users/$userID"))
    }
}