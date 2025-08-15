package com.team.todoktodok.presentation.view.discussiondetail.comment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.presentation.view.discussiondetail.comments.CommentsFragment
import com.team.todoktodok.presentation.view.discussiondetail.comments.vm.CommentsViewModel
import com.team.todoktodok.presentation.view.discussiondetail.comments.vm.CommentsViewModelFactory

class CommentBottomSheet : BottomSheetDialogFragment(R.layout.fragment_comment_bottom_sheet) {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).apply {
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            window?.setDimAmount(DIM_AMOUNT)
        }

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        by lazy {
            val repoModule = (requireActivity().application as App).container.repositoryModule
            CommentsViewModelFactory(
                repoModule.commentRepository,
                repoModule.tokenRepository,
            )
        }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.fcv_comment, CommentsFragment.newInstance())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        alignBottomSheetBelow()
    }

    private fun alignBottomSheetBelow() {
        val bottomSheet =
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?: return
        val activityRoot =
            activity?.window?.decorView?.findViewById<View>(android.R.id.content) ?: return
        val anchorView = activity?.findViewById<View>(R.id.tv_discussion_create_at) ?: return

        resizeBottomSheetToFitBelow(activityRoot, anchorView, bottomSheet)
    }

    private fun resizeBottomSheetToFitBelow(
        activityRoot: View,
        anchorView: View,
        bottomSheet: View,
    ) {
        activityRoot.post {
            if (!isAdded) return@post
            val anchorBottom = getViewBottomInWindow(anchorView)
            val availableHeight = activityRoot.height - anchorBottom
            val finalHeight =
                if (availableHeight > 0) availableHeight else activityRoot.height / 2

            setBottomSheetHeight(bottomSheet, finalHeight)
        }
    }

    private fun getViewBottomInWindow(view: View): Int {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        return location[LOCATION_INDEX_Y] + view.height
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
        private const val LOCATION_INDEX_Y = 1
        private const val DIM_AMOUNT = 0.001f

        fun newInstance(discussionId: Long): CommentBottomSheet =
            CommentBottomSheet().apply {
                arguments = bundleOf(CommentsViewModel.KEY_DISCUSSION_ID to discussionId)
            }
    }
}
