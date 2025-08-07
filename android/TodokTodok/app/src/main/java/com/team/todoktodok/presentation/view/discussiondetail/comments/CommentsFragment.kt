package com.team.todoktodok.presentation.view.discussiondetail.comments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCommentsBinding
import com.team.todoktodok.databinding.MenuExternalDiscussionBinding
import com.team.todoktodok.databinding.MenuOwnedDiscussionBinding
import com.team.todoktodok.presentation.view.discussiondetail.BottomSheetVisibilityListener
import com.team.todoktodok.presentation.view.discussiondetail.commentcreate.CommentCreateBottomSheet
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.CommentDetailFragment
import com.team.todoktodok.presentation.view.discussiondetail.comments.adapter.CommentAdapter
import com.team.todoktodok.presentation.view.discussiondetail.comments.vm.CommentsViewModel
import com.team.todoktodok.presentation.view.discussiondetail.comments.vm.CommentsViewModelFactory

class CommentsFragment : BottomSheetDialogFragment(R.layout.fragment_comments) {
    private val adapter by lazy { CommentAdapter(adapterHandler) }

    private val viewModel by viewModels<CommentsViewModel> {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        CommentsViewModelFactory(
            repositoryModule.commentRepository,
        )
    }

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
                binding.rvComments.smoothScrollToPosition(COMMENT_CREATE_POSITION)
            }

            is CommentsUiEvent.ShowCommentUpdate -> {
                showCommentCreate(commentsUiEvent.discussionId, binding, commentsUiEvent.commentId)
            }
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
                viewModel.commentsReload()
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

    private fun getPopUpView(commentId: Long): PopupWindow =
        if (true) {
            val binding = MenuOwnedDiscussionBinding.inflate(layoutInflater)
            binding.tvEdit.setOnClickListener { viewModel.updateComment(commentId) }
            binding.tvDelete.setOnClickListener { viewModel.deleteComment(commentId = commentId) }
            createPopUpView(binding.root)
        } else {
            val binding = MenuExternalDiscussionBinding.inflate(layoutInflater)
            binding.tvReport.setOnClickListener { }
            createPopUpView(binding.root)
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

            override fun onDeleteClick(commentId: Long) {
                viewModel.deleteComment(commentId)
            }

            override fun onOptionClick(
                commentId: Long,
                view: View,
            ) {
                if (popupWindow == null) popupWindow = getPopUpView(commentId)
                if (popupWindow?.isShowing == true) {
                    popupWindow?.dismiss()
                } else {
                    popupWindow?.showAsDropDown(view)
                }
            }

            override fun onToggleLike(commentId: Long) {
                viewModel.toggleLike(commentId)
            }
        }

    companion object {
        const val TAG = "COMMENTS"
        private const val COMMENT_CREATE_POSITION = 0

        fun newInstance(discussionId: Long): CommentsFragment =
            CommentsFragment().apply {
                arguments = bundleOf(CommentsViewModel.KEY_DISCUSSION_ID to discussionId)
            }
    }
}
