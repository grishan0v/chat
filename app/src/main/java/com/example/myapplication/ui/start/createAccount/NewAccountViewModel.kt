package com.example.myapplication.ui.start.createAccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.Event
import com.example.myapplication.data.dto.User
import com.example.myapplication.ui.BaseViewModel
import com.example.myapplication.utils.isEmailValid
import com.example.myapplication.utils.isTextValid
import com.google.firebase.auth.FirebaseUser

class NewAccountViewModel: BaseViewModel() {
//    private val dbRepository = DatabaseRepository()
//    private val authRepository = AuthRepository()
    private val mIsCreatedEvent = MutableLiveData<Event<FirebaseUser>>()

    val isCreatedEvent: LiveData<Event<FirebaseUser>> = mIsCreatedEvent
    val displayNameText = MutableLiveData<String>()
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val isCreatingAccount = MutableLiveData<Boolean>()

    private fun createAccount() {
        isCreatingAccount.value = true
        val createUser = User(
            displayNameText.value!!,
            emailText.value!!,
            passwordText.value!!
        )

//        authRepository.createUser(createUser) {}
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