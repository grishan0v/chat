package com.example.myapplication.ui.start.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.data.Event
import com.example.myapplication.data.dto.LoginData
import com.example.myapplication.ui.BaseViewModel
import com.example.myapplication.utils.isEmailValid
import com.example.myapplication.utils.isTextValid
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : BaseViewModel() {
//    private val authRepository = AuthRepository()
    private val _isLoggedInEvent = MutableLiveData<Event<FirebaseUser>>()

    val isLoggedInEvent: LiveData<Event<FirebaseUser>> = _isLoggedInEvent
    val emailText = MutableLiveData<String>()
    val passwordText = MutableLiveData<String>()
    val isLoggingIn = MutableLiveData<Boolean>()

    private fun login() {
        isLoggingIn.value = true
        val login = LoginData(emailText.value!!, passwordText.value!!)

//        authRepository.loginUser(login) { }
    }

    fun loginPressed() {
        if (!isEmailValid(emailText.value.toString())) {
            mSnackBarText.value = Event("Invalid email format")
            return
        }
        if (!isTextValid(6, passwordText.value)) {
            mSnackBarText.value = Event("Password is too short")
            return
        }

        login()
    }
}