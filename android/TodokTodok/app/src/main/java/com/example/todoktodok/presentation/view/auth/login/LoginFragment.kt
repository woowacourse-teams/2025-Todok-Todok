package com.example.todoktodok.presentation.view.auth.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.todoktodok.App
import com.example.todoktodok.R
import com.example.todoktodok.databinding.FragmentLoginBinding
import com.example.todoktodok.presentation.view.MainActivity
import com.example.todoktodok.presentation.view.auth.vm.AuthViewModel
import com.example.todoktodok.presentation.view.auth.vm.AuthViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var googleLoginManager: GoogleLoginManager
    private val viewModel: AuthViewModel by viewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        AuthViewModelFactory(repositoryModule.memberRepository)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLoginBinding.bind(view)
        googleLoginManager = GoogleLoginManager(requireContext())
        initView(binding)
        setUpUiEvent()
    }

    private fun initView(binding: FragmentLoginBinding) {
        binding.tvLogin.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                googleLoginManager.startLogin(
                    onSuccessLogin = { email, nickname, profilePictureUri ->
                        viewModel.login(email, nickname, profilePictureUri)
                    },
                    onFailLogin = {
                        showSnackBar(getString(R.string.login_fail_to_login), binding.tvLogin)
                    },
                )
            }
        }
    }

    private fun setUpUiEvent() {
        viewModel.uiEvent.observe(viewLifecycleOwner) {
            when (it) {
                LoginUiEvent.NavigateToMain -> {
                    val intent = MainActivity.Intent(requireContext())
                    startActivity(intent)
                    requireActivity().finish()
                }
                LoginUiEvent.NavigateToSignUp -> TODO()
            }
        }
    }

    private fun showSnackBar(
        message: String,
        view: View,
    ) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }
}
