package com.example.myapplication.ui.chat

import androidx.lifecycle.*
import com.example.myapplication.data.MyResult
import com.example.myapplication.data.entity.Chat
import com.example.myapplication.data.entity.Message
import com.example.myapplication.data.entity.UserInfo
import com.example.myapplication.data.firebase.FirebaseReferenceChildObserver
import com.example.myapplication.data.firebase.FirebaseReferenceValueObserver
import com.example.myapplication.data.repository.DatabaseRepository
import com.example.myapplication.ui.BaseViewModel
import com.example.myapplication.utils.addNewItem

class ChatViewModel(
    private val myUserID: String,
    private val otherUserID: String,
    private val chatID: String
) : BaseViewModel() {

    private val repository: DatabaseRepository = DatabaseRepository()

    private val _otherUser: MutableLiveData<UserInfo> = MutableLiveData()
    private val _addedMessage = MutableLiveData<Message>()

    private val fbRefMessagesChildObserver = FirebaseReferenceChildObserver()
    private val fbRefUserInfoObserver = FirebaseReferenceValueObserver()

    val messagesList = MediatorLiveData<MutableList<Message>>()
    val newMessageText = MutableLiveData<String>()
    val otherUser: LiveData<UserInfo> = _otherUser

    init {
        setupChat()
        checkAndUpdateLastMessageSeen()
    }

    override fun onCleared() {
        super.onCleared()
        fbRefMessagesChildObserver.clear()
        fbRefUserInfoObserver.clear()
    }

    private fun checkAndUpdateLastMessageSeen() {
        repository.loadChat(chatID) { result: MyResult<Chat> ->
            if (result is MyResult.Success && result.data != null) {
                result.data.lastMessage.let {
                    if (!it.seen && it.senderID != myUserID) {
                        it.seen = true
                        repository.updateChatLastMessage(chatID, it)
                    }
                }
            }
        }
    }

    private fun setupChat() {
        repository.loadAndObserveUserInfo(
            otherUserID,
            fbRefUserInfoObserver
        ) { result: MyResult<UserInfo> ->
            onResult(_otherUser, result)
            if (result is MyResult.Success && !fbRefMessagesChildObserver.isObserving()) {
                loadAndObserveNewMessages()
            }
        }
    }

    private fun loadAndObserveNewMessages() {
        messagesList.addSource(_addedMessage) { messagesList.addNewItem(it) }
        repository.loadAndObserveMessagesAdded(
            chatID,
            fbRefMessagesChildObserver
        ) { result: MyResult<Message> ->
            onResult(_addedMessage, result)
        }
    }

    fun sendMessagePressed() {
        if (!newMessageText.value.isNullOrBlank()) {
            val newMsg = Message(myUserID, newMessageText.value!!)
            repository.updateNewMessage(chatID, newMsg)
            repository.updateChatLastMessage(chatID, newMsg)
            newMessageText.value = ""
        }
    }
}

class ChatViewModelFactory(
    private val myUserID: String,
    private val otherUserID: String,
    private val chatID: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(myUserID, otherUserID, chatID) as T
    }
}
