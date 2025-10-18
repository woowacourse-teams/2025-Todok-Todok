package com.team.todoktodok.presentation.xml.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.team.domain.model.Support
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ViewCommonDialogBinding
import com.team.todoktodok.presentation.core.ext.getSerializableCompat

class SupportMemberDialog : DialogFragment(R.layout.view_common_dialog) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ViewCommonDialogBinding.bind(view)
        initView(binding)
    }

    private fun initView(binding: ViewCommonDialogBinding) {
        val type = arguments?.getSerializableCompat<Support>(ARG_TYPE)
        requireNotNull(type) { INVALID_SUPPORT_TYPE }

        with(binding) {
            setTitle(binding, type)
            setSubmitButton(binding, type)
            setCancelButton(binding)
        }
    }

    private fun setTitle(
        binding: ViewCommonDialogBinding,
        type: Support,
    ) {
        binding.tvMessage.text =
            when (type) {
                Support.BLOCK -> getString(R.string.profile_block_dialog_title)
                Support.REPORT -> getString(R.string.profile_report_dialog_title)
            }
    }

    private fun setSubmitButton(
        binding: ViewCommonDialogBinding,
        type: Support,
    ) {
        with(binding) {
            btnSubmit.text =
                when (type) {
                    Support.BLOCK -> getString(R.string.profile_menu_block)
                    Support.REPORT -> getString(R.string.all_report_action)
                }

            btnSubmit.setOnClickListener {
                setFragmentResult(type)
                dismiss()
            }
        }
    }

    private fun setCancelButton(binding: ViewCommonDialogBinding) {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun setFragmentResult(type: Support) {
        setFragmentResult(
            REQUEST_KEY_SUPPORT,
            Bundle().apply {
                putSerializable(RESULT_KEY_SUPPORT, type)
            },
        )
    }

    override fun getTheme(): Int = R.style.cornerRadiusDialog

    companion object {
        fun newInstance(type: Support): SupportMemberDialog =
            SupportMemberDialog().apply {
                arguments =
                    Bundle().apply {
                        putSerializable(ARG_TYPE, type)
                    }
            }

        private const val ARG_TYPE = "type"
        private const val INVALID_SUPPORT_TYPE = "존재하지 않는 신고 타입입니다."

        const val REQUEST_KEY_SUPPORT = "request_support"
        const val RESULT_KEY_SUPPORT = "result_support"
        const val TAG = "SupportMemberDialog"
    }
}
