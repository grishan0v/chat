package com.example.myapplication.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.App
import com.example.myapplication.ui.activity.MainActivity
import com.example.myapplication.R
import com.example.myapplication.data.EventObserver
import com.example.myapplication.databinding.FragmentProfileBinding
import com.example.myapplication.utils.forceHideKeyboard
import com.example.myapplication.utils.showSnackBar
import com.example.myapplication.utils.viewBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    companion object {
        const val ARGS_KEY_USER_ID = "bundle_user_id"
    }

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(App.myUserID, requireArguments().getString(ARGS_KEY_USER_ID)!!)
    }

    private val binding by viewBinding(FragmentProfileBinding::bind)

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
    }
}