package com.team.todoktodok.presentation.view.discussiondetail.commentdetail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCommentDetailBinding
import com.team.todoktodok.presentation.view.discussiondetail.BottomSheetVisibilityListener
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.adapter.CommentDetailAdapter
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.vm.CommentDetailViewModel
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.vm.CommentDetailViewModelFactory
import com.team.todoktodok.presentation.view.discussiondetail.replycreate.ReplyCreateBottomSheet

class CommentDetailFragment : Fragment(R.layout.fragment_comment_detail) {
    private val adapter by lazy { CommentDetailAdapter() }

    private val viewModel: CommentDetailViewModel by viewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        CommentDetailViewModelFactory(
            repositoryModule.commentRepository,
            repositoryModule.replyRepository,
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCommentDetailBinding.bind(view)
        setOnNavigateUp(binding)
        initAdapter(binding)
        setupOnClickAddReply(binding)
        setupObserve(binding)
        setupFragmentResultListener()
    }

    fun initAdapter(binding: FragmentCommentDetailBinding) {
        binding.rvItems.adapter = adapter
    }

    private fun setupOnClickAddReply(binding: FragmentCommentDetailBinding) {
        with(binding) {
            tvInputComment.setOnClickListener { viewModel.showReplyCreate() }
        }
    }

    fun setupObserve(binding: FragmentCommentDetailBinding) {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            adapter.submitList(value?.getCommentDetailItems() ?: emptyList())
        }
        viewModel.uiEvent.observe(this) { value ->
            handleEvent(value, binding)
        }
    }

    private fun handleEvent(
        commentDetailUiEvent: CommentDetailUiEvent,
        binding: FragmentCommentDetailBinding,
    ) {
        when (commentDetailUiEvent) {
            is CommentDetailUiEvent.ShowReplyCreate ->
                showReplyCreate(
                    commentDetailUiEvent.discussionId,
                    commentDetailUiEvent.commentId,
                    binding,
                )

            is CommentDetailUiEvent.ShowNewReply -> {
                binding.rvItems.smoothScrollToPosition(adapter.itemCount)
            }
        }
    }

    private fun showReplyCreate(
        discussionId: Long,
        commentId: Long,
        binding: FragmentCommentDetailBinding,
    ) {
        val bottomSheet = ReplyCreateBottomSheet.newInstance(discussionId, commentId)
        bottomSheet.setVisibilityListener(getBottomSheetVisibilityListener(binding))
        bottomSheet.show(childFragmentManager, ReplyCreateBottomSheet.TAG)
    }

    private fun setupFragmentResultListener() {
        childFragmentManager.setFragmentResultListener(
            ReplyCreateBottomSheet.REPLY_REQUEST_KEY,
            this,
        ) { _, bundle ->
            val result = bundle.getBoolean(ReplyCreateBottomSheet.REPLY_CREATED_RESULT_KEY)
            if (result) {
                viewModel.repliesReload()
            }
        }
    }

    private fun getBottomSheetVisibilityListener(binding: FragmentCommentDetailBinding) =
        object : BottomSheetVisibilityListener {
            override fun onBottomSheetShown() {
                binding.tvInputComment.visibility = View.GONE
            }

            override fun onBottomSheetDismissed() {
                binding.tvInputComment.visibility = View.VISIBLE
            }
        }

    fun setOnNavigateUp(binding: FragmentCommentDetailBinding) {
        binding.ivCommentDetailBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        const val TAG = "TAG_COMMENT_DETAIL"

        fun newInstance(
            discussionId: Long,
            commentId: Long,
        ): CommentDetailFragment =
            CommentDetailFragment().apply {
                arguments =
                    bundleOf(
                        CommentDetailViewModel.KEY_DISCUSSION_ID to discussionId,
                        CommentDetailViewModel.KEY_COMMENT_ID to commentId,
                    )
            }
    }
}
