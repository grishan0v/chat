package com.example.myapplication.ui.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.App
import com.example.myapplication.R
import com.example.myapplication.data.EventObserver
import com.example.myapplication.databinding.FragmentUsersBinding
import com.example.myapplication.ui.profile.ProfileFragment
import com.example.myapplication.utils.viewBinding


class UsersFragment : Fragment(R.layout.fragment_users) {

    private val viewModel: UsersViewModel by viewModels { UsersViewModelFactory(App.myUserID) }
    private val binding by viewBinding(FragmentUsersBinding::bind)
    private lateinit var listAdapter: UsersListAdapter

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
            listAdapter = UsersListAdapter(viewModel)
            binding.usersRecyclerView.adapter = listAdapter
        } else {
            throw Exception("The viewmodel is not initialized")
        }
    }

    private fun setupObservers() {
        viewModel.selectedUser.observe(viewLifecycleOwner, EventObserver { navigateToProfile(it.info.id) })
    }

    private fun navigateToProfile(userID: String) {
        val bundle = bundleOf(ProfileFragment.ARGS_KEY_USER_ID to userID)
        findNavController().navigate(R.id.action_users_to_profile, bundle)
    }
}