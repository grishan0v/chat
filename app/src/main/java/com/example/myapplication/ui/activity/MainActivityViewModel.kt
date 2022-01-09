package com.example.myapplication.ui.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.App
import com.example.myapplication.data.MyResult
import com.example.myapplication.data.entity.UserInfo
import com.example.myapplication.data.firebase.FirebaseReferenceValueObserver
import com.example.myapplication.data.repository.DatabaseRepository
import com.example.myapplication.ui.BaseViewModel

class MainActivityViewModel : BaseViewModel() {

    private val dbRepository = DatabaseRepository()

    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData()
    val userInfo: LiveData<UserInfo> = _userInfo
    private var userID = App.myUserID

    private val firebaseReferenceObserver = FirebaseReferenceValueObserver()

    init {
        loadAndObserveUserInfo()
    }

    private fun loadAndObserveUserInfo() {
        dbRepository.loadAndObserveUserInfo(
            userID,
            firebaseReferenceObserver
        ) { myResult: MyResult<UserInfo> ->
            onResult(_userInfo, myResult)
        }
    }
}