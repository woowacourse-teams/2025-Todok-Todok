package com.team.todoktodok.presentation.view.auth.signup

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.domain.model.member.NickNameException
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentSignupBinding
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.view.auth.signup.vm.SignUpViewModel
import com.team.todoktodok.presentation.view.auth.signup.vm.SignUpViewModelFactory
import com.team.todoktodok.presentation.view.discussions.DiscussionsActivity

class SignUpFragment : Fragment(R.layout.fragment_signup) {
    private val viewModel: SignUpViewModel by viewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        SignUpViewModelFactory(repositoryModule.memberRepository)
    }

    private lateinit var messageConverter: ExceptionMessageConverter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSignupBinding.bind(view)
        messageConverter = ExceptionMessageConverter()

        setupLoading(binding)
        initView(binding)
        setUpObserveUiEvent(binding)
    }

    private fun setupLoading(binding: FragmentSignupBinding) {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.show()
            } else {
                binding.progressBar.hide()
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
            }

            etNickname.doAfterTextChanged {
                etNicknameLayout.error = null
            }
        }
    }

    private fun setUpObserveUiEvent(binding: FragmentSignupBinding) {
        viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
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

    private fun moveToMain() {
        val intent = DiscussionsActivity.Intent(requireContext())
        startActivity(intent)
        requireActivity().finish()
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
            }
        val message = getString(resourceId)
        binding.etNicknameLayout.error = message
    }
}
