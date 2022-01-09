package com.example.myapplication.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.data.EventObserver
import com.example.myapplication.data.dto.ChatAndUserData
import com.example.myapplication.databinding.FragmentChatsBinding
import com.example.myapplication.ui.chat.ChatFragment
import com.example.myapplication.utils.convertTwoUserIDs
import com.example.myapplication.utils.viewBinding

class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private val viewModel: ChatsViewModel by viewModels { ChatsViewModelFactory(App.myUserID) }
    private val binding by viewBinding(FragmentChatsBinding::bind)
    private lateinit var listAdapter: ChatsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { viewmodel = viewModel }
        binding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()
        setupObservers()
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewmodel
        if (viewModel != null) {
            listAdapter = ChatsListAdapter(viewModel)
            binding.chatsRecyclerView.adapter = listAdapter
        } else {
            throw Exception("The viewmodel is not initialized")
        }
    }

    private fun setupObservers() {
        viewModel.selectedChat.observe(viewLifecycleOwner,
            EventObserver { navigateToChat(it) })
    }

    private fun navigateToChat(chatWithUserInfo: ChatAndUserData) {
        val bundle = bundleOf(
            ChatFragment.ARGS_KEY_USER_ID to App.myUserID,
            ChatFragment.ARGS_KEY_OTHER_USER_ID to chatWithUserInfo.userInfo.id,
            ChatFragment.ARGS_KEY_CHAT_ID to convertTwoUserIDs(
                App.myUserID,
                chatWithUserInfo.userInfo.id
            )
        )
        findNavController().navigate(R.id.action_chats_to_chat, bundle)
    }
}