package com.team.ui_xml.setting.withdraw

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.ui_xml.R
import com.team.ui_xml.auth.AuthActivity
import com.team.ui_xml.component.CommonDialog
import com.team.ui_xml.databinding.FragmentWithdrawBinding
import com.team.ui_xml.setting.withdraw.vm.WithdrawViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WithdrawFragment : Fragment(R.layout.fragment_withdraw) {
    private val viewModel: WithdrawViewModel by viewModels()

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
