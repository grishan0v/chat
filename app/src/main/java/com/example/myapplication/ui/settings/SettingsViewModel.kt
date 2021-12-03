package com.example.myapplication.ui.settings

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.Event
import com.example.myapplication.data.entity.UserInfo
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.DatabaseRepository
import com.example.myapplication.data.repository.StorageRepository
import com.example.myapplication.ui.BaseViewModel
import com.example.myapplication.data.MyResult
import com.example.myapplication.data.firebase.FirebaseReferenceValueObserver

class SettingsViewModel(private val userID: String) : BaseViewModel() {

    private val dbRepository: DatabaseRepository = DatabaseRepository()
    private val storageRepository = StorageRepository()
    private val authRepository = AuthRepository()

    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData()
    val userInfo: LiveData<UserInfo> = _userInfo

    private val _editStatusEvent = MutableLiveData<Event<Unit>>()
    val editStatusEvent: LiveData<Event<Unit>> = _editStatusEvent

    private val _editImageEvent = MutableLiveData<Event<Unit>>()
    val editImageEvent: LiveData<Event<Unit>> = _editImageEvent

    private val _logoutEvent = MutableLiveData<Event<Unit>>()
    val logoutEvent: LiveData<Event<Unit>> = _logoutEvent

    private val firebaseReferenceObserver = FirebaseReferenceValueObserver()

    init {
        loadAndObserveUserInfo()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserver.clear()
    }

    private fun loadAndObserveUserInfo() {
        dbRepository.loadAndObserveUserInfo(userID, firebaseReferenceObserver)
        { myResult: MyResult<UserInfo> -> onResult(_userInfo, myResult) }
    }

    fun changeUserStatus(status: String) {
        dbRepository.updateUserStatus(userID, status)
    }

    fun changeUserImage(byteArray: ByteArray) {
        storageRepository.updateUserProfileImage(userID, byteArray) { myResult: MyResult<Uri> ->
            onResult(null, myResult)
            if (myResult is MyResult.Success) {
                dbRepository.updateUserProfileImageUrl(userID, myResult.data.toString())
            }
        }
    }

    fun changeUserImagePressed() {
        _editImageEvent.value = Event(Unit)
    }

    fun changeUserStatusPressed() {
        _editStatusEvent.value = Event(Unit)
    }

    fun logoutUserPressed() {
        authRepository.logoutUser()
        _logoutEvent.value = Event(Unit)
    }
}

class SettingsViewModelFactory(private val userID: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(userID) as T
    }
}