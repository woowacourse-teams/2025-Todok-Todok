package com.team.todoktodok.presentation.xml.setting.modify

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.domain.model.member.NickNameException
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentModifyProfileBinding
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.xml.setting.modify.vm.ModifyProfileViewModel
import com.team.todoktodok.presentation.xml.setting.modify.vm.ModifyProfileViewModelFactory

class ModifyProfileFragment : Fragment(R.layout.fragment_modify_profile) {
    private val viewModel: ModifyProfileViewModel by viewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        ModifyProfileViewModelFactory(repositoryModule.memberRepository)
    }

    private lateinit var messageConverter: ExceptionMessageConverter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentModifyProfileBinding.bind(view)
        messageConverter = ExceptionMessageConverter()

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
        with(binding) {
            etNicknameLayout.setEndIconOnClickListener {
                binding.etNickname.text = null
            }

            with(etNickname) {
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
    }

    private fun setUpMessageEditText(binding: FragmentModifyProfileBinding) {
        with(binding) {
            etMessageLayout.setEndIconOnClickListener {
                binding.etMessage.text = null
            }

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
                viewModel.checkProfileValidation(nickname, message)
            }
        }
    }

    private fun setUpUiState(binding: FragmentModifyProfileBinding) {
        with(binding) {
            viewModel.uiState.observe(viewLifecycleOwner) { value ->
                etNickname.setText(value.profile.nickname)
                etMessage.setText(value.profile.message)

                setUpLoading(value.isLoading, binding)
            }
        }
    }

    private fun setUpLoading(
        isLoading: Boolean,
        binding: FragmentModifyProfileBinding,
    ) {
        with(binding) {
            if (isLoading) {
                progressBar.show()
            } else {
                progressBar.hide()
            }
        }
    }

    private fun setUpUiEvent(binding: FragmentModifyProfileBinding) {
        viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                ModifyProfileUiEvent.OnCompleteModification -> {
                    AlertSnackBar(
                        binding.root,
                        R.string.setting_modify_profile_complete,
                    ).show()
                }

                is ModifyProfileUiEvent.ShowInvalidNickNameMessage -> {
                    handleNickNameErrorEvent(event.exception, binding)
                }

                is ModifyProfileUiEvent.ShowErrorMessage -> {
                    AlertSnackBar(
                        binding.root,
                        messageConverter(event.exception),
                    ).show()
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
