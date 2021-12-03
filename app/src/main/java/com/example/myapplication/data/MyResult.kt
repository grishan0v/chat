package com.example.myapplication.data

sealed class  MyResult<out R> {
    data class Success<out T>(val data: T? = null, val message: String? = null) : MyResult<T>()
    class Error(val message: String? = null) : MyResult<Nothing>()
    object Loading : MyResult<Nothing>()
}