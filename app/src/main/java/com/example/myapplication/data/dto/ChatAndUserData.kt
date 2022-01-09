package com.example.myapplication.data.dto

import com.example.myapplication.data.entity.Chat
import com.example.myapplication.data.entity.UserInfo

data class ChatAndUserData(
    var chat: Chat,
    var userInfo: UserInfo
)
