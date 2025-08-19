package com.team.todoktodok.presentation.core.component

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ViewCommonDialogBinding

class CommonDialog : DialogFragment(R.layout.view_common_dialog) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ViewCommonDialogBinding.bind(view)
        initView(binding)
    }

    private fun initView(binding: ViewCommonDialogBinding) {
        with(binding) {
            tvMessage.text = arguments?.getString(ARG_MESSAGE)
            btnSubmit.text = arguments?.getString(ARG_SUBMIT_BUTTON_TEXT)
            btnCancel.setOnClickListener {
                setFragmentResult(
                    REQUEST_KEY_COMMON_DIALOG,
                    bundleOf(RESULT_KEY_COMMON_DIALOG to false),
                )
                dismiss() }
            btnSubmit.setOnClickListener {
                setFragmentResult(
                    REQUEST_KEY_COMMON_DIALOG,
                    bundleOf(RESULT_KEY_COMMON_DIALOG to true),
                )
                dismiss()
            }
        }
    }

    override fun getTheme(): Int = R.style.cornerRadiusDialog

    companion object {
        fun newInstance(
            message: String,
            submitButtonText: String,
        ): CommonDialog =
            CommonDialog().apply {
                arguments =
                    bundleOf(
                        ARG_MESSAGE to message,
                        ARG_SUBMIT_BUTTON_TEXT to submitButtonText,
                    )
            }

        const val ARG_MESSAGE = "message"
        const val ARG_SUBMIT_BUTTON_TEXT = "submitButtonText"

        const val REQUEST_KEY_COMMON_DIALOG = "request_common_dialog"
        const val RESULT_KEY_COMMON_DIALOG = "result_common_dialog"
        const val TAG = "CommonDialog"
    }
}
