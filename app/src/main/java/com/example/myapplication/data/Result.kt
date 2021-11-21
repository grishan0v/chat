package com.example.myapplication.data

sealed class  Result<out R> {
    data class Success<out T>(val data: T? = null, val message: String? = null) : Result<T>()
    class Error(val message: String? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}