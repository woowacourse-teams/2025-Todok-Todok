package com.team.ui_xml.auth.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.team.core.ExceptionMessageConverter
import com.team.core.extension.repeatOnStarted
import com.team.core.extension.repeatOnViewStarted
import com.team.core.navigation.MainRoute
import com.team.ui_xml.BuildConfig
import com.team.ui_xml.R
import com.team.ui_xml.auth.vm.AuthViewModel
import com.team.ui_xml.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.ui_xml.databinding.FragmentLoginBinding
import com.team.ui_xml.signup.SignUpFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var googleLoginManager: GoogleCredentialManager

    @Inject
    lateinit var messageConverter: ExceptionMessageConverter

    @Inject
    lateinit var mainRoute: MainRoute

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLoginBinding.bind(view)
        googleLoginManager = GoogleCredentialManager(requireContext(), BuildConfig.GOOGLE_CLIENT_ID)

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
        mainRoute.navigateToMain(requireActivity())
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
