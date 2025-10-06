package com.team.todoktodok.presentation.xml.discussion.create

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ViewDraftDialogBinding

class DraftDialog : DialogFragment(R.layout.view_draft_dialog) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ViewDraftDialogBinding.bind(view)
        initView(binding)
    }

    private fun initView(binding: ViewDraftDialogBinding) {
        binding.apply {
            btnCancel.setOnClickListener {
                setFragmentResult(
                    requestKey = KEY_REQUEST_DRAFT,
                    result = bundleOf(Pair(KEY_RESULT_DRAFT, false)),
                )
                dismiss()
            }
            btnTempSave.setOnClickListener {
                setFragmentResult(
                    requestKey = KEY_REQUEST_DRAFT,
                    result = bundleOf(Pair(KEY_RESULT_DRAFT, true))
                )
                dismiss()
            }
        }
    }

    override fun getTheme(): Int = R.style.draftDialog

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            setGravity(Gravity.BOTTOM)
        }
    }

    companion object {
        const val TAG = "DRAFT"
        const val KEY_REQUEST_DRAFT = "key_request_draft"
        const val KEY_RESULT_DRAFT = "key_result_draft"
        fun newInstance(): DraftDialog = DraftDialog().apply {
            arguments = bundleOf()
        }
    }
}