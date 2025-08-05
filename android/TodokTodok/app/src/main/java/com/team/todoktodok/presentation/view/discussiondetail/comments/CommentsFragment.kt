package com.team.todoktodok.presentation.view.discussiondetail.comments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCommentsBinding
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

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCommentsBinding.bind(view)
        initAdapter(binding)
        setupOnClickAddComment(binding)
        setupObserve(binding)
        setupFragmentResultListener()
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    private fun initAdapter(binding: FragmentCommentsBinding) {
        binding.rvComments.adapter = adapter
    }

    private fun setupOnClickAddComment(binding: FragmentCommentsBinding) {
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
                )

            CommentsUiEvent.ShowNewComment -> {
                binding.rvComments.smoothScrollToPosition(COMMENT_CREATE_POSITION)
            }
        }
    }

    private fun showCommentCreate(
        discussionId: Long,
        binding: FragmentCommentsBinding,
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
            override fun onItemClick(commentId: Long) {
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
