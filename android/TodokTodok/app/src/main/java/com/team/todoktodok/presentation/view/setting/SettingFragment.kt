package com.team.todoktodok.presentation.view.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentSettingBinding
import com.team.todoktodok.presentation.view.setting.vm.SettingViewModel
import com.team.todoktodok.presentation.view.setting.vm.SettingViewModelFactory

class SettingFragment : Fragment(R.layout.fragment_setting) {
    private val viewModel: SettingViewModel by activityViewModels { SettingViewModelFactory() }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSettingBinding.bind(view)
        initView(binding)
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
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/"))
                startActivity(intent)
            }
        }
    }
}
