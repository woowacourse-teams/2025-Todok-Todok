package com.team.todoktodok.presentation.xml.setting.modify

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.core.ExceptionMessageConverter
import com.team.domain.model.member.NickNameException
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.ProfileException
import com.team.domain.model.member.ProfileMessage
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentModifyProfileBinding
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.xml.setting.modify.vm.ModifyProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ModifyProfileFragment : Fragment(R.layout.fragment_modify_profile) {
    private val viewModel: ModifyProfileViewModel by viewModels()

    @Inject
    lateinit var messageConverter: ExceptionMessageConverter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentModifyProfileBinding.bind(view)

        initView(binding)
        observeUiState(binding)
        observeUiEvent(binding)
    }

    private fun initView(binding: FragmentModifyProfileBinding) =
        with(binding) {
            setUpNicknameEditText(this)
            setUpMessageEditText(this)
            btnModify.setOnClickListener {
                viewModel.checkProfileValidation(
                    etNickname.text.toString(),
                    etMessage.text.toString(),
                )
            }
        }

    private fun setUpNicknameEditText(binding: FragmentModifyProfileBinding) =
        with(binding) {
            etNicknameLayout.setEndIconOnClickListener { etNickname.text = null }

            etNickname.maxLines = Nickname.MAX_LENGTH

            etNickname.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) etNicknameLayout.hint = null
            }

            etNickname.doAfterTextChanged { etNicknameLayout.error = null }

            etNickname.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_NEXT ||
                    (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
                ) {
                    etMessage.requestFocus()
                    true
                } else {
                    false
                }
            }
        }

    private fun setUpMessageEditText(binding: FragmentModifyProfileBinding) =
        with(binding) {
            etMessageLayout.setEndIconOnClickListener { etMessage.text = null }

            etMessage.maxLines = ProfileMessage.MAX_LENGTH

            etMessage.doAfterTextChanged { etMessageLayout.error = null }

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

    private fun observeUiState(binding: FragmentModifyProfileBinding) =
        with(binding) {
            viewModel.uiState.observe(viewLifecycleOwner) { value ->
                etNickname.setText(value.profile.nickname)
                etMessage.setText(value.profile.message)
                progressBar.isVisible = value.isLoading
            }
        }

    private fun observeUiEvent(binding: FragmentModifyProfileBinding) {
        viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                ModifyProfileUiEvent.OnCompleteModification -> {
                    AlertSnackBar(binding.root, R.string.setting_modify_profile_complete).show()
                    hideKeyBoard(binding.root)
                }

                is ModifyProfileUiEvent.ShowInvalidNickNameMessage -> {
                    binding.etNicknameLayout.error =
                        when (event.exception) {
                            NickNameException.InvalidLength -> {
                                getString(R.string.profile_invalid_nickname_length)
                            }

                            NickNameException.InvalidWhiteSpace -> {
                                getString(R.string.profile_invalid_nickname_white_space)
                            }

                            NickNameException.InvalidCharacters -> {
                                getString(R.string.profile_invalid_nickname_character)
                            }

                            NickNameException.SameNicknameModification -> {
                                getString(R.string.profile_invalid_nickname_same)
                            }
                        }
                }

                is ModifyProfileUiEvent.ShowInvalidMessageMessage -> {
                    when (event.exception) {
                        ProfileException.SameMessageModification -> {
                            binding.etMessageLayout.error =
                                getString(R.string.profile_invalid_profile_message_same)
                        }
                    }
                }

                is ModifyProfileUiEvent.ShowErrorMessage -> {
                    AlertSnackBar(binding.root, messageConverter(event.exception)).show()
                }
            }
        }
    }

    private fun hideKeyBoard(view: View) {
        val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
