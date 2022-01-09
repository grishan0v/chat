package com.example.myapplication.ui.chats

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.dto.ChatAndUserData
import com.example.myapplication.databinding.ListItemChatBinding

class ChatsListAdapter internal constructor(private val viewModel: ChatsViewModel) :
    ListAdapter<(ChatAndUserData), ChatsListAdapter.ViewHolder>(ChatDiffCallback()) {

    class ViewHolder(private val binding: ListItemChatBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ChatsViewModel, item: ChatAndUserData) {
            binding.viewmodel = viewModel
            binding.chatanduserdata = item
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemChatBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }
}

class ChatDiffCallback : DiffUtil.ItemCallback<ChatAndUserData>() {
    override fun areItemsTheSame(oldItem: ChatAndUserData, itemWithUserInfo: ChatAndUserData): Boolean {
        return oldItem == itemWithUserInfo
    }

    override fun areContentsTheSame(oldItem: ChatAndUserData, itemWithUserInfo: ChatAndUserData): Boolean {
        return oldItem.chat.info.id == itemWithUserInfo.chat.info.id
    }
}