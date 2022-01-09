package com.example.myapplication.ui.users

import androidx.lifecycle.*
import com.example.myapplication.data.Event
import com.example.myapplication.data.MyResult
import com.example.myapplication.data.entity.User
import com.example.myapplication.data.repository.DatabaseRepository
import com.example.myapplication.ui.BaseViewModel

class UsersViewModel(private val myUserID: String) : BaseViewModel() {
    private val repository: DatabaseRepository = DatabaseRepository()

    private val _selectedUser = MutableLiveData<Event<User>>()
    var selectedUser: LiveData<Event<User>> = _selectedUser
    private val updatedUsersList = MutableLiveData<MutableList<User>>()
    val usersList = MediatorLiveData<List<User>>()

    init {
        usersList.addSource(updatedUsersList) { mutableList ->
            usersList.value = updatedUsersList.value?.filter { it.info.id != myUserID }
        }
        loadUsers()
    }

    private fun loadUsers() {
        repository.loadUsers { result: MyResult<MutableList<User>> ->
            onResult(updatedUsersList, result)
        }
    }

    fun selectUser(user: User) {
        _selectedUser.value = Event(user)
    }
}

class UsersViewModelFactory(private val myUserID: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UsersViewModel(myUserID) as T
    }
}