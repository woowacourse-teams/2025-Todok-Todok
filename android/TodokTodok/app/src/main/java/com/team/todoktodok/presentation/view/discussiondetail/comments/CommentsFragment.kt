package com.team.todoktodok.presentation.view.discussiondetail.comments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCommentsBinding
import com.team.todoktodok.databinding.MenuExternalDiscussionBinding
import com.team.todoktodok.databinding.MenuOwnedDiscussionBinding
import com.team.todoktodok.presentation.core.component.CommonDialog
import com.team.todoktodok.presentation.view.discussiondetail.BottomSheetVisibilityListener
import com.team.todoktodok.presentation.view.discussiondetail.commentcreate.CommentCreateBottomSheet
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.CommentDetailFragment
import com.team.todoktodok.presentation.view.discussiondetail.comments.adapter.CommentAdapter
import com.team.todoktodok.presentation.view.discussiondetail.comments.vm.CommentsViewModel
import com.team.todoktodok.presentation.view.discussiondetail.model.CommentUiModel
import com.team.todoktodok.presentation.view.discussiondetail.vm.DiscussionDetailViewModel
import com.team.todoktodok.presentation.view.profile.ProfileActivity

class CommentsFragment : BottomSheetDialogFragment(R.layout.fragment_comments) {
    private val adapter by lazy { CommentAdapter(adapterHandler) }

    private val sharedViewModel: DiscussionDetailViewModel by activityViewModels()

    private val viewModel by viewModels<CommentsViewModel>(
        ownerProducer = { requireParentFragment() },
    )

    private var popupWindow: PopupWindow? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCommentsBinding.bind(view)
        initAdapter(binding)
        setupOnClick(binding)
        setupObserve(binding)
        setupFragmentResultListener()
    }

    override fun onDestroy() {
        popupWindow?.dismiss()
        popupWindow = null
        super.onDestroy()
    }

    private fun initAdapter(binding: FragmentCommentsBinding) {
        binding.rvComments.adapter = adapter
    }

    private fun setupOnClick(binding: FragmentCommentsBinding) {
        with(binding) {
            tvInputComment.setOnClickListener { viewModel.showCommentCreate() }
        }
    }

    private fun setupObserve(binding: FragmentCommentsBinding) {
        viewModel.comments.observe(viewLifecycleOwner) { value ->
            adapter.submitList(value)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { value ->
            handleEvent(value, binding)
        }
    }

    private fun handleEvent(
        commentsUiEvent: CommentsUiEvent,
        binding: FragmentCommentsBinding,
    ) {
        when (commentsUiEvent) {
            is CommentsUiEvent.ShowCommentCreate ->
                showCommentCreate(
                    commentsUiEvent.discussionId,
                    binding,
                    null,
                )

            CommentsUiEvent.ShowNewComment -> {
                sharedViewModel.reloadDiscussion()
                binding.rvComments.doOnPreDraw {
                    binding.rvComments.smoothScrollToPosition(adapter.itemCount)
                }
            }

            is CommentsUiEvent.ShowCommentUpdate -> {
                showCommentCreate(commentsUiEvent.discussionId, binding, commentsUiEvent.commentId)
            }

            CommentsUiEvent.DeleteComment -> sharedViewModel.reloadDiscussion()
        }
    }

    private fun showCommentCreate(
        discussionId: Long,
        binding: FragmentCommentsBinding,
        commentId: Long?,
    ) {
        val bottomSheet = CommentCreateBottomSheet.newInstance(discussionId, commentId)
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
                viewModel.reloadComments()
            }
        }
    }

    private fun createPopUpView(popupView: View) =
        PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true,
        )

    private fun getPopUpView(commentUiModel: CommentUiModel): PopupWindow =
        if (commentUiModel.isMyComment) {
            val binding = MenuOwnedDiscussionBinding.inflate(layoutInflater)
            binding.tvEdit.setOnClickListener {
                viewModel.updateComment(commentUiModel.comment.id)
            }
            binding.tvDelete.setOnClickListener {
                viewModel.deleteComment(commentUiModel.comment.id)
                popupWindow?.dismiss()
            }
            createPopUpView(binding.root)
        } else {
            setUpDialogResultListener(commentId = commentUiModel.comment.id)
            val binding = MenuExternalDiscussionBinding.inflate(layoutInflater)
            binding.tvReport.setOnClickListener {
                showReportDialog()
            }
            createPopUpView(binding.root)
        }

    private fun showReportDialog() {
        val dialog =
            CommonDialog.newInstance(
                getString(R.string.all_report_comment),
                getString(R.string.all_report_action),
            )
        dialog.show(childFragmentManager, CommonDialog.TAG)
    }

    private fun setUpDialogResultListener(commentId: Long) {
        childFragmentManager.setFragmentResultListener(
            CommonDialog.RESULT_KEY_COMMON_DIALOG,
            this,
        ) { _, bundle ->
            val result = bundle.getBoolean(CommonDialog.RESULT_KEY_COMMON_DIALOG)
            if (result) {
                viewModel.report(commentId)
                popupWindow?.dismiss()
            }
        }
    }

    private fun getBottomSheetVisibilityListener(binding: FragmentCommentsBinding) =
        object : BottomSheetVisibilityListener {
            override fun onBottomSheetShown() {
                binding.tvInputComment.visibility = View.GONE
            }

            override fun onBottomSheetDismissed() {
                binding.tvInputComment.visibility = View.VISIBLE
            }
        }

    private val adapterHandler =
        object : CommentAdapter.Handler {
            override fun onReplyClick(commentId: Long) {
                parentFragmentManager.commit {
                    hide(this@CommentsFragment)
                    add(
                        R.id.fcv_comment,
                        CommentDetailFragment.newInstance(viewModel.discussionId, commentId),
                        CommentDetailFragment.TAG,
                    )
                    addToBackStack(null)
                }
            }

            override fun onOptionClick(
                commentUiModel: CommentUiModel,
                view: View,
            ) {
                popupWindow = getPopUpView(commentUiModel)
                if (popupWindow?.isShowing == true) {
                    popupWindow?.dismiss()
                } else {
                    popupWindow?.showAsDropDown(view)
                }
            }

            override fun onToggleLike(commentId: Long) {
                viewModel.toggleLike(commentId)
            }

            override fun onClickUserName(userId: Long) {
                navigateToProfile(userId)
            }
        }

    fun navigateToProfile(userId: Long) {
        val intent = ProfileActivity.Intent(requireContext(), userId)
        startActivity(intent)
    }

    companion object {
        const val TAG = "COMMENTS"

        fun newInstance(): CommentsFragment = CommentsFragment()
    }
}
