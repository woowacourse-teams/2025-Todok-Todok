package com.team.todoktodok.presentation.xml.discussion.create

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ViewCreateDiscussionDialogBinding

class TempSaveDialog : DialogFragment(R.layout.view_create_discussion_dialog) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ViewCreateDiscussionDialogBinding.bind(view)
        binding.apply {
            btnCancel.setOnClickListener {
                setFragmentResult(
                    requestKey = KEY_TEMP_SAVE,
                    result = bundleOf(Pair(KEY_TEMP_SAVE, false)),
                )
                dismiss()
            }
            btnTempSave.setOnClickListener {
                setFragmentResult(
                    requestKey = KEY_TEMP_SAVE,
                    result = bundleOf(Pair(KEY_TEMP_SAVE, true))
                )
                dismiss()
            }
        }
    }

    override fun getTheme(): Int = R.style.tempSaveDialog

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
        const val KEY_TEMP_SAVE = "temp_save"
        fun newInstance(): TempSaveDialog = TempSaveDialog().apply {
            arguments = bundleOf()
        }
    }
}