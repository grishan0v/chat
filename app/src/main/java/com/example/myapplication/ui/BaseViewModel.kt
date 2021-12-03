package com.example.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.Event
import com.example.myapplication.data.MyResult

abstract class BaseViewModel: ViewModel() {
    protected val mSnackBarText = MutableLiveData<Event<String>>()
    val snackBarText: LiveData<Event<String>> = mSnackBarText

    private val mDataLoading = MutableLiveData<Event<Boolean>>()
    val dataLoading: LiveData<Event<Boolean>> = mDataLoading

    protected fun <T> onResult(mutableLiveData: MutableLiveData<T>? = null, myResult: MyResult<T>) {
        when (myResult) {
            is MyResult.Loading -> mDataLoading.value = Event(true)

            is MyResult.Error -> {
                mDataLoading.value = Event(false)
                myResult.message?.let { mSnackBarText.value = Event(it) }
            }

            is MyResult.Success -> {
                mDataLoading.value = Event(false)
                myResult.data?.let { mutableLiveData?.value = it }
                myResult.message?.let { mSnackBarText.value = Event(it) }
            }
        }
    }
}