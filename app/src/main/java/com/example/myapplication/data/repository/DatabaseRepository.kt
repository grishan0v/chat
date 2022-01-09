package com.example.myapplication.data.repository

import com.example.myapplication.data.MyResult
import com.example.myapplication.data.entity.*
import com.example.myapplication.data.firebase.FirebaseDataSource
import com.example.myapplication.data.firebase.FirebaseReferenceChildObserver
import com.example.myapplication.data.firebase.FirebaseReferenceValueObserver
import com.example.myapplication.utils.wrapSnapshotToArrayList
import com.example.myapplication.utils.wrapSnapshotToClass

class DatabaseRepository {
    private val firebaseDatabaseService = FirebaseDataSource()

    fun updateNewUser(user: User) {
        firebaseDatabaseService.updateNewUser(user)
    }

    fun loadAndObserveUserInfo(
        userID: String,
        observer: FirebaseReferenceValueObserver,
        b: ((MyResult<UserInfo>) -> Unit)
    ) {
        firebaseDatabaseService.attachUserInfoObserver(UserInfo::class.java, userID, observer, b)
    }

    fun updateUserStatus(userID: String, status: String) {
        firebaseDatabaseService.updateUserStatus(userID, status)
    }

    fun updateUserProfileImageUrl(userID: String, url: String) {
        firebaseDatabaseService.updateUserProfileImageUrl(userID, url)
    }

    fun updateChatLastMessage(chatID: String, message: Message) {
        firebaseDatabaseService.updateLastMessage(chatID, message)
    }

    fun updateNewMessage(messagesID: String, message: Message) {
        firebaseDatabaseService.pushNewMessage(messagesID, message)
    }

    fun updateNewSentRequest(userID: String, userRequest: UserRequest) {
        firebaseDatabaseService.updateNewSentRequest(userID, userRequest)
    }

    fun updateNewNotification(otherUserID: String, userNotification: UserNotification) {
        firebaseDatabaseService.updateNewNotification(otherUserID, userNotification)
    }

    fun updateNewFriend(myUser: UserFriend, otherUser: UserFriend) {
        firebaseDatabaseService.updateNewFriend(myUser, otherUser)
    }

    fun updateNewChat(chat: Chat){
        firebaseDatabaseService.updateNewChat(chat)
    }

    fun loadFriends(userID: String, b: ((MyResult<List<UserFriend>>) -> Unit)) {
        b.invoke(MyResult.Loading)
        firebaseDatabaseService.loadFriendsTask(userID).addOnSuccessListener {
            val friendsList = wrapSnapshotToArrayList(UserFriend::class.java, it)
            b.invoke(MyResult.Success(friendsList))
        }.addOnFailureListener { b.invoke(MyResult.Error(it.message)) }
    }

    fun loadUserInfo(userID: String, b: ((MyResult<UserInfo>) -> Unit)) {
        firebaseDatabaseService.loadUserInfoTask(userID).addOnSuccessListener {
            b.invoke(MyResult.Success(wrapSnapshotToClass(UserInfo::class.java, it)))
        }.addOnFailureListener { b.invoke(MyResult.Error(it.message)) }
    }

    fun loadAndObserveChat(chatID: String, observer: FirebaseReferenceValueObserver, b: ((MyResult<Chat>) -> Unit)) {
        firebaseDatabaseService.attachChatObserver(Chat::class.java, chatID, observer, b)
    }

    fun loadAndObserveMessagesAdded(messagesID: String, observer: FirebaseReferenceChildObserver, b: ((MyResult<Message>) -> Unit)) {
        firebaseDatabaseService.attachMessagesObserver(Message::class.java, messagesID, observer, b)
    }

    fun loadAndObserveUser(userID: String, observer: FirebaseReferenceValueObserver, b: ((MyResult<User>) -> Unit)) {
        firebaseDatabaseService.attachUserObserver(User::class.java, userID, observer, b)
    }

    fun loadChat(chatID: String, b: ((MyResult<Chat>) -> Unit)) {
        firebaseDatabaseService.loadChatTask(chatID).addOnSuccessListener {
            b.invoke(MyResult.Success(wrapSnapshotToClass(Chat::class.java, it)))
        }.addOnFailureListener { b.invoke(MyResult.Error(it.message)) }
    }

    fun loadUsers(b: ((MyResult<MutableList<User>>) -> Unit)) {
        b.invoke(MyResult.Loading)
        firebaseDatabaseService.loadUsersTask().addOnSuccessListener {
            val usersList = wrapSnapshotToArrayList(User::class.java, it)
            b.invoke(MyResult.Success(usersList))
        }.addOnFailureListener { b.invoke(MyResult.Error(it.message)) }
    }

    fun loadUser(userID: String, b: ((MyResult<User>) -> Unit)) {
        firebaseDatabaseService.loadUserTask(userID).addOnSuccessListener {
            b.invoke(MyResult.Success(wrapSnapshotToClass(User::class.java, it)))
        }.addOnFailureListener { b.invoke(MyResult.Error(it.message)) }
    }

    fun removeFriend(userID: String, friendID: String) {
        firebaseDatabaseService.removeFriend(userID, friendID)
    }

    fun removeSentRequest(otherUserID: String, myUserID: String) {
        firebaseDatabaseService.removeSentRequest(otherUserID, myUserID)
    }

    fun removeChat(chatID: String) {
        firebaseDatabaseService.removeChat(chatID)
    }

    fun removeMessages(messagesID: String){
        firebaseDatabaseService.removeMessages(messagesID)
    }

    fun removeNotification(userID: String, notificationID: String) {
        firebaseDatabaseService.removeNotification(userID, notificationID)
    }
}