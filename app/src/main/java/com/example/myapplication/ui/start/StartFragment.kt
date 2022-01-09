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
import com.example.myapplication.utils.SharedPreferencesUtil
import com.example.myapplication.utils.viewBinding

class StartFragment : Fragment(R.layout.fragment_start) {
    private val viewModel by viewModels<StartViewModel>()
    private val binding by viewBinding(FragmentStartBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { viewmodel = viewModel }
        binding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(false)
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