package com.team.ui_xml.signup

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.core.ExceptionMessageConverter
import com.team.core.extension.repeatOnViewStarted
import com.team.core.navigation.MainRoute
import com.team.domain.model.member.NickNameException
import com.team.ui_xml.R
import com.team.ui_xml.auth.signup.SignUpUiEvent
import com.team.ui_xml.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.ui_xml.databinding.FragmentSignupBinding
import com.team.ui_xml.signup.vm.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_signup) {
    private val viewModel: SignUpViewModel by viewModels()

    @Inject
    lateinit var messageConverter: ExceptionMessageConverter

    @Inject
    lateinit var mainNavigation: MainRoute

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSignupBinding.bind(view)

        setupLoading(binding)
        setUpRestoringState(binding)
        initView(binding)
        setUpObserveUiEvent(binding)
    }

    private fun setupLoading(binding: FragmentSignupBinding) {
        repeatOnViewStarted {
            viewModel.isLoading.collect { isLoading ->
                if (isLoading) {
                    binding.progressBar.show()
                } else {
                    binding.progressBar.hide()
                }
            }
        }
    }

    private fun initView(binding: FragmentSignupBinding) {
        with(binding) {
            etNickname.onFocusChangeListener =
                View.OnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) etNicknameLayout.hint = null
                }

            btnCreate.setOnClickListener {
                val nickname = etNickname.text.toString()
                viewModel.submitNickname(nickname)
                hideKeyBoard()
            }

            etNickname.doAfterTextChanged {
                etNicknameLayout.error = null
            }
        }
    }

    private fun setUpObserveUiEvent(binding: FragmentSignupBinding) {
        repeatOnViewStarted {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    SignUpUiEvent.NavigateToMain -> moveToMain()
                    is SignUpUiEvent.ShowInvalidNickNameMessage -> {
                        handleNickNameErrorEvent(event.exception, binding)
                    }

                    is SignUpUiEvent.ShowErrorMessage -> {
                        AlertSnackBar(
                            binding.root,
                            messageConverter(event.exception),
                        ).show()
                    }
                }
            }
        }
    }

    private fun setUpRestoringState(binding: FragmentSignupBinding) {
        repeatOnViewStarted {
            viewModel.isRestoring.collect { isRestoring ->
                AlertSnackBar(binding.root, R.string.network_try_connection).show()
            }
        }
    }

    private fun hideKeyBoard() {
        val inputMethodManager: InputMethodManager =
            requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun moveToMain() {
        mainNavigation.navigateToMain(requireActivity())
    }

    private fun handleNickNameErrorEvent(
        exception: NickNameException,
        binding: FragmentSignupBinding,
    ) {
        val resourceId =
            when (exception) {
                is NickNameException.InvalidWhiteSpace -> R.string.signup_invalid_nickname_message_white_space
                is NickNameException.InvalidCharacters -> R.string.signup_invalid_nickname_message_character
                is NickNameException.InvalidLength -> R.string.signup_invalid_nickname_message_length
                NickNameException.SameNicknameModification -> return
            }
        val message = getString(resourceId)
        binding.etNicknameLayout.error = message
    }
}
