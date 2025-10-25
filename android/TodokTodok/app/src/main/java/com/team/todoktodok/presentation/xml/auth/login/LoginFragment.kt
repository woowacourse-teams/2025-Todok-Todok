package com.team.todoktodok.presentation.xml.auth.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.team.todoktodok.BuildConfig
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentLoginBinding
import com.team.todoktodok.presentation.compose.main.MainActivity
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.core.ext.repeatOnStarted
import com.team.todoktodok.presentation.core.ext.repeatOnViewStarted
import com.team.todoktodok.presentation.view.auth.login.GoogleCredentialManager
import com.team.todoktodok.presentation.xml.auth.signup.SignUpFragment
import com.team.todoktodok.presentation.xml.auth.vm.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var googleLoginManager: GoogleCredentialManager
    private lateinit var messageConverter: ExceptionMessageConverter
    private val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLoginBinding.bind(view)
        googleLoginManager = GoogleCredentialManager(requireContext(), BuildConfig.GOOGLE_CLIENT_ID)
        messageConverter = ExceptionMessageConverter()

        setupLoading(binding)
        setUpRestoringState(binding)
        setUpUiEvent(binding)
        initView(binding)
    }

    private fun initView(binding: FragmentLoginBinding) {
        binding.tvLogin.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                when (val result = googleLoginManager.getGoogleCredentialResult()) {
                    is GoogleCredentialResult.Success -> {
                        viewModel.login(result.idToken)
                    }

                    is GoogleCredentialResult.Failure -> {
                        AlertSnackBar(binding.root, R.string.login_fail_to_login).show()
                    }

                    GoogleCredentialResult.Cancel -> {
                        AlertSnackBar(binding.root, R.string.login_cancelled).show()
                    }

                    GoogleCredentialResult.Suspending -> Unit
                }
            }
        }
    }

    private fun setupLoading(binding: FragmentLoginBinding) {
        repeatOnStarted {
            viewModel.isLoading.collect { isLoading ->
                if (isLoading) {
                    binding.progressBar.show()
                } else {
                    binding.progressBar.hide()
                }
            }
        }
    }

    private fun setUpRestoringState(binding: FragmentLoginBinding) {
        repeatOnViewStarted {
            viewModel.isRestoring.collect { isRestoring ->
                AlertSnackBar(binding.root, R.string.network_try_connection).show()
            }
        }
    }

    private fun setUpUiEvent(binding: FragmentLoginBinding) {
        repeatOnViewStarted {
            viewModel.uiEvent.collect {
                when (it) {
                    LoginUiEvent.NavigateToMain -> moveToMain()
                    LoginUiEvent.NavigateToSignUp -> moveToSignUp()
                    LoginUiEvent.ShowLoginButton -> showLoginButton(binding)
                    is LoginUiEvent.ShowErrorMessage -> {
                        AlertSnackBar(binding.root, messageConverter(it.exception)).show()
                    }
                }
            }
        }
    }

    private fun moveToMain() {
        val intent = MainActivity.Intent(requireContext())
        startActivity(intent)
        requireActivity().finish()
    }

    private fun moveToSignUp() {
        parentFragmentManager.commit {
            replace(R.id.fcv_container_auth, SignUpFragment())
        }
    }

    private fun showLoginButton(binding: FragmentLoginBinding) {
        binding.tvLogin.visibility = View.VISIBLE
    }
}
