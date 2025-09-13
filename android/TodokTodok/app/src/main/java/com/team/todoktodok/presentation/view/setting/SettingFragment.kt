package com.team.todoktodok.presentation.view.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.team.todoktodok.App
import com.team.todoktodok.BuildConfig
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentSettingBinding
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.core.component.CommonDialog
import com.team.todoktodok.presentation.view.auth.AuthActivity
import com.team.todoktodok.presentation.view.auth.login.GoogleCredentialManager
import com.team.todoktodok.presentation.view.setting.vm.SettingViewModel
import com.team.todoktodok.presentation.view.setting.vm.SettingViewModelFactory
import kotlinx.coroutines.launch

class SettingFragment : Fragment(R.layout.fragment_setting) {
    private val viewModel: SettingViewModel by activityViewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        SettingViewModelFactory(repositoryModule.tokenRepository)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSettingBinding.bind(view)
        initView(binding)
        setUpFragmentResultToLogout(binding)
        setupUiEvent()
    }

    private fun initView(binding: FragmentSettingBinding) {
        with(binding) {
            llModifyProfile.setOnClickListener {
                viewModel.changeScreen(SettingScreen.MODIFY_PROFILE)
            }

            llBlockMembers.setOnClickListener {
                viewModel.changeScreen(SettingScreen.MANAGE_BLOCKED_USERS)
            }

            llSendFeedback.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.FEEDBACK_URL))
                startActivity(intent)
            }

            llWithdraw.setOnClickListener {
                viewModel.changeScreen(SettingScreen.WITHDRAW)
            }

            llLogout.setOnClickListener {
                CommonDialog
                    .newInstance(
                        message = getString(R.string.logout_ask),
                        submitButtonText =
                            getString(
                                (R.string.text_logout),
                            ),
                    ).show(parentFragmentManager, CommonDialog.TAG)
            }
        }
    }

    private fun setUpFragmentResultToLogout(binding: FragmentSettingBinding) {
        parentFragmentManager.setFragmentResultListener(
            CommonDialog.REQUEST_KEY_COMMON_DIALOG,
            viewLifecycleOwner,
        ) { _, bundle ->
            val isConfirmed = bundle.getBoolean(CommonDialog.RESULT_KEY_COMMON_DIALOG, false)
            if (isConfirmed) logout(binding)
        }
    }

    private fun logout(binding: FragmentSettingBinding) {
        val credentialManager =
            GoogleCredentialManager(requireActivity(), BuildConfig.GOOGLE_CLIENT_ID)

        viewLifecycleOwner.lifecycleScope.launch {
            credentialManager
                .logOut()
                .onSuccess { viewModel.logout() }
                .onFailure {
                    AlertSnackBar(
                        view = binding.root,
                        messageRes = R.string.logout_fail,
                    ).show()
                }
        }
    }

    private fun setupUiEvent() {
        viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                SettingUiEvent.NavigateToLogin -> moveToLogin()
            }
        }
    }

    private fun moveToLogin() {
        val intent =
            AuthActivity.Intent(requireActivity()).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        startActivity(intent)
    }
}
