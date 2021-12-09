package com.example.myapplication.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.data.EventObserver
import com.example.myapplication.databinding.FragmentStartBinding
import com.example.myapplication.core.utils.SharedPreferencesUtil

class StartFragment : Fragment() {
    private val viewModel by viewModels<StartViewModel>()
    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartBinding.inflate(inflater, container, false)
            .apply { viewmodel = viewModel }
        binding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()

        if (userIsAlreadyLoggedIn()) {
            navigateDirectlyToChats()
        }
    }

    private fun userIsAlreadyLoggedIn(): Boolean {
        return SharedPreferencesUtil.getUserID(requireContext()) != null
    }

    private fun setupObservers() {
        viewModel.loginEvent.observe(viewLifecycleOwner, EventObserver { navigateToLogin() })
        viewModel.createAccountEvent.observe(
            viewLifecycleOwner, EventObserver { navigateToCreateAccount() })
    }

    private fun navigateDirectlyToChats() {
        findNavController().navigate(R.id.action_start_to_chats)
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_start_to_login)
    }

    private fun navigateToCreateAccount() {
        findNavController().navigate(R.id.action_start_to_new_account)
    }
}