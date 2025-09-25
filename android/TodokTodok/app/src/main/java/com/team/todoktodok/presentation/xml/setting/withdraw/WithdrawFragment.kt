package com.team.todoktodok.presentation.xml.setting.withdraw

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentWithdrawBinding
import com.team.todoktodok.presentation.core.component.CommonDialog
import com.team.todoktodok.presentation.xml.auth.AuthActivity
import com.team.todoktodok.presentation.xml.setting.withdraw.vm.WithdrawViewModel

class WithdrawFragment : Fragment(R.layout.fragment_withdraw) {
    private val viewModel: WithdrawViewModel by lazy {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        WithdrawViewModel(
            repositoryModule.memberRepository,
            repositoryModule.notificationRepository,
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        val binding = FragmentWithdrawBinding.bind(view)
        binding.tvWithdraw.setOnClickListener {
            showDialog()
        }

        parentFragmentManager.setFragmentResultListener(
            CommonDialog.REQUEST_KEY_COMMON_DIALOG,
            viewLifecycleOwner,
        ) { _, bundle ->
            val isConfirmed = bundle.getBoolean(CommonDialog.RESULT_KEY_COMMON_DIALOG, false)
            if (isConfirmed) viewModel.withdraw()
        }

        viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                WithdrawUiEvent.NavigateToLogin -> moveToLogin()
            }
        }
    }

    private fun showDialog() {
        CommonDialog
            .newInstance(
                getString(R.string.text_ask_really_withdraw),
                getString(R.string.text_do_withdraw),
            ).show(parentFragmentManager, CommonDialog.TAG)
    }

    private fun moveToLogin() {
        val intent =
            AuthActivity.Intent(requireContext()).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        startActivity(intent)
    }

    companion object {
        fun newInstance(): Fragment = WithdrawFragment()
    }
}
