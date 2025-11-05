package com.team.ui_xml.component

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.team.ui_xml.R
import com.team.ui_xml.databinding.ViewReportUserDialogBinding

class ReportUserDialog : DialogFragment(R.layout.view_report_user_dialog) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ViewReportUserDialogBinding.bind(view)
        with(binding) {
            binding.btnSubmit.setOnClickListener {
                val selectedReportReason =
                    when (binding.rgReasons.checkedRadioButtonId) {
                        R.id.rb_inappropriate_profile -> binding.rbInappropriateProfile.text.toString()
                        R.id.rb_spam_activity -> binding.rbSpamActivity.text.toString()
                        R.id.rb_harassment -> binding.rbHarassment.text.toString()
                        R.id.rb_misconduct -> binding.rbMisconduct.text.toString()
                        else -> ""
                    }
                setFragmentResult(
                    requestKey,
                    bundleOf(RESULT_KEY_REPORT_USER to selectedReportReason),
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
            .ifBlank { REQUEST_KEY_REPORT_USER_DIALOG }
    }

    companion object {
        const val TAG = "ReportUserDialog"
        const val REQUEST_KEY_REPORT_USER_DIALOG = "request_key_report_dialog"
        const val RESULT_KEY_REPORT_USER = "result_key_report_user"

        fun newInstance(requestKey: String = REQUEST_KEY_REPORT_USER_DIALOG): ReportUserDialog =
            ReportUserDialog()
                .apply {
                    arguments = bundleOf(ARG_REQUEST_KEY to requestKey)
                }

        private const val ARG_REQUEST_KEY = "arg_request_key"
    }
}
