package com.team.todoktodok.presentation.view.setting.modify

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.team.domain.model.member.NickNameException
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentModifyProfileBinding
import com.team.todoktodok.presentation.view.setting.SettingScreen
import com.team.todoktodok.presentation.view.setting.modify.vm.ModifyProfileViewModel
import com.team.todoktodok.presentation.view.setting.modify.vm.ModifyProfileViewModelFactory
import com.team.todoktodok.presentation.view.setting.vm.SettingViewModel
import com.team.todoktodok.presentation.view.setting.vm.SettingViewModelFactory
import kotlin.getValue

class ModifyProfileFragment : Fragment(R.layout.fragment_modify_profile) {
    private val viewModel: ModifyProfileViewModel by viewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        ModifyProfileViewModelFactory(repositoryModule.memberRepository)
    }

    private val settingViewModel: SettingViewModel by activityViewModels { SettingViewModelFactory() }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentModifyProfileBinding.bind(view)

        initView(binding)
        setUpUiEvent(binding)
        setUpUiState(binding)
    }

    private fun initView(binding: FragmentModifyProfileBinding) {
        with(binding) {
            setUpNicknameEditText(binding)
            setUpMessageEditText(binding)
            setUpModifyButton(binding)
        }
    }

    private fun setUpNicknameEditText(binding: FragmentModifyProfileBinding) {
        with(binding.etNickname) {
            onFocusChangeListener =
                View.OnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) binding.etNicknameLayout.hint = null
                }

            doAfterTextChanged {
                binding.etNicknameLayout.error = null
            }

            setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
                ) {
                    binding.etMessage.requestFocus()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setUpMessageEditText(binding: FragmentModifyProfileBinding) {
        with(binding) {
            etMessage.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
                ) {
                    btnModify.performClick()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setUpModifyButton(binding: FragmentModifyProfileBinding) {
        with(binding) {
            btnModify.setOnClickListener {
                val nickname = etNickname.text.toString()
                val message = etMessage.text.toString()
                viewModel.modifyProfile(nickname, message)
            }
        }
    }

    private fun setUpUiState(binding: FragmentModifyProfileBinding) {
        viewModel.profile.observe(viewLifecycleOwner) { value ->
            binding.etNickname.setText(value.nickname)
            binding.etMessage.setText(value.message)
        }
    }

    private fun setUpUiEvent(binding: FragmentModifyProfileBinding) {
        viewModel.uiEvent.observe(viewLifecycleOwner) { value ->
            when (value) {
                ModifyProfileUiEvent.OnCompleteModification -> {
                    settingViewModel.changeScreen(SettingScreen.SETTING_MAIN)
                }

                is ModifyProfileUiEvent.ShowInvalidNickNameMessage -> {
                    handleNickNameErrorEvent(value.exception, binding)
                }
            }
        }
    }

    private fun handleNickNameErrorEvent(
        exception: NickNameException,
        binding: FragmentModifyProfileBinding,
    ) {
        val resourceId =
            when (exception) {
                is NickNameException.InvalidWhiteSpace -> R.string.signup_invalid_nickname_message_white_space
                is NickNameException.InvalidCharacters -> R.string.signup_invalid_nickname_message_character
                is NickNameException.InvalidLength -> R.string.signup_invalid_nickname_message_length
            }
        val message = getString(resourceId)
        binding.etNicknameLayout.error = message
    }
}
