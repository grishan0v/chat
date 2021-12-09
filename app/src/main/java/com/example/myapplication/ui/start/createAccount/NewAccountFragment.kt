package com.example.myapplication.ui.start.createAccount

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.data.EventObserver
import com.example.myapplication.databinding.FragmentNewAccountBinding
import com.example.myapplication.core.utils.SharedPreferencesUtil
import com.example.myapplication.core.forceHideKeyboard
import com.example.myapplication.core.showSnackBar
import com.example.myapplication.core.utils.viewBinding

class NewAccountFragment : Fragment(R.layout.fragment_new_account) {
    private val viewModel by viewModels<NewAccountViewModel>()
    private val binding by viewBinding(FragmentNewAccountBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply { viewmodel = viewModel }
        binding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)
        setupObservers()
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

    private fun setupObservers() {
        viewModel.dataLoading.observe(viewLifecycleOwner,
            EventObserver { (activity as MainActivity).showGlobalProgressBar(it) })

        viewModel.snackBarText.observe(viewLifecycleOwner,
            EventObserver { text ->
                view?.showSnackBar(text)
                view?.forceHideKeyboard()
            })

        viewModel.isCreatedEvent.observe(viewLifecycleOwner, EventObserver {
            SharedPreferencesUtil.saveUserID(requireContext(), it.uid)
            navigateToChats()
        })
    }

    private fun navigateToChats() {
        findNavController().navigate(R.id.action_new_account_to_chats)
    }
}