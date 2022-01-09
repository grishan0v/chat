package com.example.myapplication.ui.chats

import androidx.lifecycle.*
import com.example.myapplication.data.Event
import com.example.myapplication.data.MyResult
import com.example.myapplication.data.dto.ChatAndUserData
import com.example.myapplication.data.entity.Chat
import com.example.myapplication.data.entity.UserFriend
import com.example.myapplication.data.entity.UserInfo
import com.example.myapplication.data.firebase.FirebaseReferenceValueObserver
import com.example.myapplication.data.repository.DatabaseRepository
import com.example.myapplication.ui.BaseViewModel
import com.example.myapplication.utils.addNewItem
import com.example.myapplication.utils.convertTwoUserIDs
import com.example.myapplication.utils.updateItemAt

class ChatsViewModel(val myUserID: String) : BaseViewModel() {

    private val repository: DatabaseRepository = DatabaseRepository()
    private val firebaseReferenceObserverList = ArrayList<FirebaseReferenceValueObserver>()
    private val _updatedChatWithUserInfo = MutableLiveData<ChatAndUserData>()
    private val _selectedChat = MutableLiveData<Event<ChatAndUserData>>()

    var selectedChat: LiveData<Event<ChatAndUserData>> = _selectedChat
    val chatsList = MediatorLiveData<MutableList<ChatAndUserData>>()

    init {
        chatsList.addSource(_updatedChatWithUserInfo) { newChat ->
            val chat = chatsList.value?.find { it.chat.info.id == newChat.chat.info.id }
            if (chat == null) {
                chatsList.addNewItem(newChat)
            } else {
                chatsList.updateItemAt(newChat, chatsList.value!!.indexOf(chat))
            }
        }
        setupChats()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserverList.forEach { it.clear() }
    }

    private fun setupChats() {
        loadFriends()
    }

    private fun loadFriends() {
        repository.loadFriends(myUserID) { result: MyResult<List<UserFriend>> ->
            onResult(null, result)
            if (result is MyResult.Success) result.data?.forEach { loadUserInfo(it) }
        }
    }

    private fun loadUserInfo(userFriend: UserFriend) {
        repository.loadUserInfo(userFriend.userID) { result: MyResult<UserInfo> ->
            onResult(null, result)
            if (result is MyResult.Success) result.data?.let { loadAndObserveChat(it) }
        }
    }

    private fun loadAndObserveChat(userInfo: UserInfo) {
        val observer = FirebaseReferenceValueObserver()
        firebaseReferenceObserverList.add(observer)
        repository.loadAndObserveChat(convertTwoUserIDs(myUserID, userInfo.id), observer) { result: MyResult<Chat> ->
            if (result is MyResult.Success) {
                _updatedChatWithUserInfo.value = result.data?.let { ChatAndUserData(it, userInfo) }
            } else if (result is MyResult.Error) {
                chatsList.value?.let {
                    val newList = mutableListOf<ChatAndUserData>().apply { addAll(it) }
                    newList.removeIf { it2 -> result.message.toString().contains(it2.userInfo.id) }
                    chatsList.value = newList
                }
            }
        }
    }

    fun selectChatWithUserInfoPressed(chat: ChatAndUserData) {
        _selectedChat.value = Event(chat)
    }
}

class ChatsViewModelFactory(private val myUserID: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatsViewModel(myUserID) as T
    }
}