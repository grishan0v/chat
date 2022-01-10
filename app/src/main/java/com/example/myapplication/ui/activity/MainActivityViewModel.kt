package com.example.myapplication.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.App
import com.example.myapplication.data.MyResult
import com.example.myapplication.data.entity.UserInfo
import com.example.myapplication.data.firebase.FirebaseAuthStateObserver
import com.example.myapplication.data.firebase.FirebaseReferenceConnectedObserver
import com.example.myapplication.data.firebase.FirebaseReferenceValueObserver
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.DatabaseRepository
import com.example.myapplication.ui.BaseViewModel
import com.google.firebase.auth.FirebaseUser

class MainActivityViewModel : BaseViewModel() {
    private val dbRepository = DatabaseRepository()
    private val authRepository = AuthRepository()

    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData()
    val userInfo: LiveData<UserInfo> = _userInfo

    private val firebaseReferenceObserver = FirebaseReferenceValueObserver()
    private val firebaseReferenceConnectedObserver = FirebaseReferenceConnectedObserver()
    private val firebaseAuthStateObserver = FirebaseAuthStateObserver()
    private var userID = App.myUserID

    init {
        loadUserInfo()
        setupAuthObserver()
    }

    override fun onCleared() {
        super.onCleared()
        firebaseReferenceObserver.clear()
        firebaseReferenceConnectedObserver.clear()
        firebaseAuthStateObserver.clear()
    }

    private fun setupAuthObserver(){
        authRepository.observeAuthState(firebaseAuthStateObserver) { result: MyResult<FirebaseUser> ->
            if (result is MyResult.Success) {
                userID = result.data!!.uid
                loadUserInfo()
                firebaseReferenceConnectedObserver.start(userID)
            } else {
                firebaseReferenceConnectedObserver.clear()
            }
        }
    }

    private fun loadUserInfo() {
        dbRepository.loadUserInfo(userID) { result: MyResult<UserInfo> ->
            if (result is MyResult.Success) {
                _userInfo.value = result.data ?: UserInfo()
            }
        }
    }
}