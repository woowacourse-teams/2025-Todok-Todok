package com.team.todoktodok.presentation.view.setting.manage

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.team.todoktodok.R
import com.team.todoktodok.databinding.DialogUnblockBinding

class UnBlockingDialog : DialogFragment(R.layout.dialog_unblock) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DialogUnblockBinding.bind(view)
        initView(binding)
    }

    private fun initView(binding: DialogUnblockBinding) {
        with(binding) {
            btnCancel.setOnClickListener {
                dismiss()
            }
            btnSubmit.setOnClickListener {
                parentFragmentManager.setFragmentResult(
                    REQUEST_KEY_SUPPORT,
                    bundleOf(RESULT_KEY_SUPPORT to true),
                )
                dismiss()
            }
        }
    }

    override fun getTheme(): Int = R.style.cornerRadiusDialog

    companion object {
        const val REQUEST_KEY_SUPPORT = "request_unblocking"
        const val RESULT_KEY_SUPPORT = "result_unblocking"
        const val TAG = "unblocking_dialog"
    }
}
