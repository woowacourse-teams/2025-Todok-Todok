package com.team.todoktodok.presentation.view.discussiondetail.comments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.team.todoktodok.R
import com.team.todoktodok.databinding.DialogSupportBinding

class ReportCommentDialog : DialogFragment(R.layout.dialog_support) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DialogSupportBinding.bind(view)
        initView(binding)
    }

    private fun initView(binding: DialogSupportBinding) {
        with(binding) {
            setTitle(binding)
            setSubmitButton(binding)
            setCancelButton(binding)
        }
    }

    private fun setTitle(binding: DialogSupportBinding) {
        binding.tvMessage.text = getString(R.string.all_report_comment)
    }

    private fun setSubmitButton(binding: DialogSupportBinding) {
        with(binding) {
            btnSubmit.text =
                getString(R.string.all_report_action)

            btnSubmit.setOnClickListener {
                setFragmentResult()
                dismiss()
            }
        }
    }

    private fun setCancelButton(binding: DialogSupportBinding) {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun setFragmentResult() {
        setFragmentResult(
            RESULT_KEY_REPORT,
            Bundle().apply {
                putBoolean(RESULT_KEY_REPORT, true)
            },
        )
    }

    override fun getTheme(): Int = R.style.cornerRadiusDialog

    companion object {
        fun newInstance(): ReportCommentDialog = ReportCommentDialog()

        const val RESULT_KEY_REPORT = "result_report"
        const val TAG = "ReportCommentDialog"
    }
}
