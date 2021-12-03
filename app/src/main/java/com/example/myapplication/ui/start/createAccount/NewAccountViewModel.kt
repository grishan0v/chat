package com.example.myapplication.ui.start.createAccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.Event
import com.example.myapplication.data.dto.UserData
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.ui.BaseViewModel
import com.example.myapplication.utils.isEmailValid
import com.example.myapplication.utils.isTextValid
import com.google.firebase.auth.FirebaseUser
import com.example.myapplication.data.MyResult
import com.example.myapplication.data.entity.User
import com.example.myapplication.data.repository.DatabaseRepository

class NewAccountViewModel : BaseViewModel() {
    private val dbRepository = DatabaseRepository()
    private val authRepository = AuthRepository()
    private val _isCreatedEvent = MutableLiveData<Event<FirebaseUser>>()

    val isCreatedEvent: LiveData<Event<FirebaseUser>> = _isCreatedEvent
    val displayNameText = MutableLiveData<String>()
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val isCreatingAccount = MutableLiveData<Boolean>()

    private fun createAccount() {
        isCreatingAccount.value = true
        val createUser = UserData(displayNameText.value!!, emailText.value!!, passwordText.value!!)

        authRepository.createUser(createUser) { myResult: MyResult<FirebaseUser> ->
            onResult(null, myResult)
            if (myResult is MyResult.Success) {
                _isCreatedEvent.value = Event(myResult.data!!)
                dbRepository.updateNewUser(User().apply {
                    info.id = myResult.data.uid
                    info.displayName = createUser.displayName
                })
            }
            if (myResult is MyResult.Success || myResult is MyResult.Error) isCreatingAccount.value = false
        }
    }

    fun createAccountPressed() {
        if (!isTextValid(2, displayNameText.value)) {
            mSnackBarText.value = Event("Display name is too short")
            return
        }

        if (!isEmailValid(emailText.value.toString())) {
            mSnackBarText.value = Event("Invalid email format")
            return
        }
        if (!isTextValid(6, passwordText.value)) {
            mSnackBarText.value = Event("Password is too short")
            return
        }
        createAccount()
    }
}