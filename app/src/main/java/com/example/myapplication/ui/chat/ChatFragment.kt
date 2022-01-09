package com.example.myapplication.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentChatBinding
import com.example.myapplication.databinding.ToolbarAddonChatBinding
import com.example.myapplication.utils.viewBinding

class ChatFragment : Fragment() {
    companion object {
        const val ARGS_KEY_USER_ID = "bundle_user_id"
        const val ARGS_KEY_OTHER_USER_ID = "bundle_other_user_id"
        const val ARGS_KEY_CHAT_ID = "bundle_other_chat_id"
    }

    private val viewModel: ChatViewModel by viewModels {
        ChatViewModelFactory(
            requireArguments().getString(ARGS_KEY_USER_ID)!!,
            requireArguments().getString(ARGS_KEY_OTHER_USER_ID)!!,
            requireArguments().getString(ARGS_KEY_CHAT_ID)!!
        )
    }

    private lateinit var binding: FragmentChatBinding
    private lateinit var listAdapter: MessagesListAdapter
    private lateinit var listAdapterObserver: RecyclerView.AdapterDataObserver
    private lateinit var toolbarAddonChatBinding: ToolbarAddonChatBinding

    override fun onDestroy() {
        super.onDestroy()
        removeCustomToolbar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentChatBinding.inflate(inflater, container, false).apply { viewmodel = viewModel }
        binding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)

        toolbarAddonChatBinding =
            ToolbarAddonChatBinding.inflate(inflater, container, false)
                .apply { viewmodel = viewModel }
        toolbarAddonChatBinding.lifecycleOwner = this.viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCustomToolbar()
        setupListAdapter()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun removeCustomToolbar() {
        val supportActionBar = (activity as AppCompatActivity?)!!.supportActionBar
        supportActionBar!!.setDisplayShowCustomEnabled(false)
        supportActionBar.customView = null
    }

    private fun setupCustomToolbar() {
        val supportActionBar = (activity as AppCompatActivity?)!!.supportActionBar
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar.customView = toolbarAddonChatBinding.root
    }

    private fun setupListAdapter() {
        val viewModel = binding.viewmodel
        if (viewModel != null) {
            listAdapterObserver = (object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    binding.messagesRecyclerView.scrollToPosition(positionStart)
                }
            })
            listAdapter =
                MessagesListAdapter(viewModel, requireArguments().getString(ARGS_KEY_USER_ID)!!)
            listAdapter.registerAdapterDataObserver(listAdapterObserver)
            binding.messagesRecyclerView.adapter = listAdapter
        } else {
            throw Exception("The viewmodel is not initialized")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listAdapter.unregisterAdapterDataObserver(listAdapterObserver)
    }
}