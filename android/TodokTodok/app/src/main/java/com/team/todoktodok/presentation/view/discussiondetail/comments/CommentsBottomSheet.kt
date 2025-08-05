package com.team.todoktodok.presentation.view.discussiondetail.comments

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCommentsBottomSheetBinding
import com.team.todoktodok.presentation.view.discussiondetail.BottomSheetVisibilityListener
import com.team.todoktodok.presentation.view.discussiondetail.commentcreate.CommentCreateBottomSheet
import com.team.todoktodok.presentation.view.discussiondetail.comments.adapter.CommentAdapter
import com.team.todoktodok.presentation.view.discussiondetail.comments.vm.CommentsViewModel
import com.team.todoktodok.presentation.view.discussiondetail.comments.vm.CommentsViewModelFactory

class CommentsBottomSheet : BottomSheetDialogFragment(R.layout.fragment_comments_bottom_sheet) {
    private val adapter by lazy { CommentAdapter() }

    private val viewModel by viewModels<CommentsViewModel> {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        CommentsViewModelFactory(
            repositoryModule.commentRepository,
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).apply {
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            window?.setDimAmount(DIM_AMOUNT_NONE)
        }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCommentsBottomSheetBinding.bind(view)
        initAdapter(binding)
        setupOnClickAddComment(binding)
        setupObserve(binding)
        setupFragmentResultListener()
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
            if (anchorView != null && bottomSheet != null) {
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

    private fun initAdapter(binding: FragmentCommentsBottomSheetBinding) {
        binding.rvComments.adapter = adapter
    }

    private fun setupOnClickAddComment(binding: FragmentCommentsBottomSheetBinding) {
        with(binding) {
            tvInputComment.setOnClickListener { viewModel.showCommentCreate() }
        }
    }

    private fun setupObserve(binding: FragmentCommentsBottomSheetBinding) {
        viewModel.comments.observe(viewLifecycleOwner) { value ->
            adapter.submitList(value)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { value ->
            handleEvent(value, binding)
        }
    }

    private fun handleEvent(
        commentsUiEvent: CommentsUiEvent,
        binding: FragmentCommentsBottomSheetBinding,
    ) {
        when (commentsUiEvent) {
            is CommentsUiEvent.ShowCommentCreate ->
                showCommentCreate(
                    commentsUiEvent.discussionId,
                    binding,
                )

            CommentsUiEvent.ShowNewComment -> {
                binding.rvComments.smoothScrollToPosition(COMMENT_CREATE_POSITION)
            }
        }
    }

    private fun showCommentCreate(
        discussionId: Long,
        binding: FragmentCommentsBottomSheetBinding,
    ) {
        val bottomSheet = CommentCreateBottomSheet.newInstance(discussionId)
        bottomSheet.setVisibilityListener(getBottomSheetVisibilityListener(binding))
        bottomSheet.show(childFragmentManager, CommentCreateBottomSheet.TAG)
    }

    private fun setupFragmentResultListener() {
        childFragmentManager.setFragmentResultListener(
            CommentCreateBottomSheet.COMMENT_REQUEST_KEY,
            this,
        ) { _, bundle ->
            val result = bundle.getBoolean(CommentCreateBottomSheet.COMMENT_CREATED_RESULT_KEY)
            if (result) {
                viewModel.commentsReload()
            }
        }
    }

    private fun getBottomSheetVisibilityListener(binding: FragmentCommentsBottomSheetBinding) =
        object : BottomSheetVisibilityListener {
            override fun onBottomSheetShown() {
                binding.tvInputComment.visibility = View.GONE
            }

            override fun onBottomSheetDismissed() {
                binding.tvInputComment.visibility = View.VISIBLE
            }
        }

    companion object {
        const val TAG = "COMMENTS_BOTTOM_SHEET"
        private const val COMMENT_CREATE_POSITION = 0
        private const val DIM_AMOUNT_NONE = 0f

        fun newInstance(discussionId: Long): CommentsBottomSheet =
            CommentsBottomSheet().apply {
                arguments = bundleOf(CommentsViewModel.KEY_DISCUSSION_ID to discussionId)
            }
    }
}
