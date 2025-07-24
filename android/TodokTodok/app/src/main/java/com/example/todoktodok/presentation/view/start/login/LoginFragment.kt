package com.example.todoktodok.presentation.view.start.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.todoktodok.App
import com.example.todoktodok.R
import com.example.todoktodok.databinding.FragmentLoginBinding
import com.example.todoktodok.presentation.view.start.vm.StartViewModel
import com.example.todoktodok.presentation.view.start.vm.StartViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var googleLoginManager: GoogleLoginManager
    private val viewModel: StartViewModel by activityViewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        StartViewModelFactory(repositoryModule.memberRepository)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLoginBinding.bind(view)
        googleLoginManager = GoogleLoginManager(requireContext())
        initView(binding)
    }

    private fun initView(binding: FragmentLoginBinding) {
        binding.tvLogin.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                googleLoginManager.startLogin(
                    onSuccessLogin = { email, nickname, profilePictureUri ->
                        viewModel.login(email, nickname, profilePictureUri)
                    },
                    onFailLogin = { showSnackBar(getString(R.string.login_fail_to_login)) },
                )
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }
}
