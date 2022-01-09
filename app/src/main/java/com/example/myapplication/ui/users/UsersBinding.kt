package com.example.myapplication.ui.users

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.entity.User

@BindingAdapter("bind_users_list")
fun bindUsersList(listView: RecyclerView, items: List<User>?) {
    items?.let { (listView.adapter as UsersListAdapter).submitList(items) }
}