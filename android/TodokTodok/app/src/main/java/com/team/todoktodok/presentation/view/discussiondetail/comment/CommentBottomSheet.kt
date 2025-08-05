package com.team.todoktodok.presentation.view.discussiondetail.comment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team.todoktodok.R
import com.team.todoktodok.presentation.view.discussiondetail.comments.CommentsFragment

class CommentBottomSheet : BottomSheetDialogFragment(R.layout.fragment_comment_bottom_sheet) {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).apply {
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            window?.setDimAmount(DIM_AMOUNT)
        }

    private val discussionId: Long by lazy {
        requireArguments().getLong(KEY_DISCUSSION_ID)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fcv_comment, CommentsFragment.newInstance(discussionId))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        adjustBottomSheetBelowAnchor()
    }

    private fun adjustBottomSheetBelowAnchor() {
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val activityRoot = activity?.window?.decorView?.findViewById<View>(android.R.id.content)
        val anchorView = activity?.findViewById<View>(R.id.tv_discussion_create_at)

        activityRoot?.post {
            if (isAdded && anchorView != null && bottomSheet != null) {
                val anchorBottom = getViewBottomInWindow(anchorView)
                val availableHeight = activityRoot.height - anchorBottom
                setBottomSheetHeight(bottomSheet, availableHeight)
            }
        }
    }

    private fun getViewBottomInWindow(view: View): Int {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        return location[1] + view.height
    }

    private fun setBottomSheetHeight(
        sheet: View,
        height: Int,
    ) {
        sheet.layoutParams =
            sheet.layoutParams
                .apply {
                    this.height = height
                }
        sheet.requestLayout()

        BottomSheetBehavior.from(sheet).apply {
            skipCollapsed = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    companion object {
        private const val DIM_AMOUNT = 0.001f
        const val KEY_DISCUSSION_ID = "discussion_id"

        fun newInstance(discussionId: Long): CommentBottomSheet =
            CommentBottomSheet().apply {
                arguments = bundleOf(KEY_DISCUSSION_ID to discussionId)
            }
    }
}
