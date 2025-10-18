package com.team.todoktodok.presentation.core.component

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ViewReportDialogBinding

class ReportDialog : DialogFragment(R.layout.view_report_dialog) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ViewReportDialogBinding.bind(view)
        with(binding) {
            binding.btnSubmit.setOnClickListener {
                val selectedReportReason =
                    when (binding.rgReasons.checkedRadioButtonId) {
                        R.id.rb_off_topic -> binding.rbOffTopic.text.toString()
                        R.id.rb_abuse -> binding.rbAbuse.text.toString()
                        R.id.rb_hate_speech -> binding.rbHateSpeech.text.toString()
                        R.id.rb_spam -> binding.rbSpam.text.toString()
                        else -> ""
                    }
                setFragmentResult(
                    requestKey,
                    bundleOf(RESULT_KEY_REPORT to selectedReportReason),
                )
                dismiss()
            }
            binding.btnCancel.setOnClickListener { dismiss() }
        }
    }

    private val requestKey: String by lazy {
        requireArguments()
            .getString(ARG_REQUEST_KEY)
            .orEmpty()
            .ifBlank { REQUEST_KEY_REPORT_DIALOG }
    }

    companion object {
        const val TAG = "ReportDialog"
        const val REQUEST_KEY_REPORT_DIALOG = "request_key_report_dialog"
        const val RESULT_KEY_REPORT = "result_key_report"

        fun newInstance(requestKey: String = REQUEST_KEY_REPORT_DIALOG): ReportDialog =
            ReportDialog()
                .apply {
                    arguments = bundleOf(ARG_REQUEST_KEY to requestKey)
                }

        private const val ARG_REQUEST_KEY = "arg_request_key"
    }
}
