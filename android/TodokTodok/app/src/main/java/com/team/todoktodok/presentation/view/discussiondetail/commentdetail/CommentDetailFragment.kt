package com.team.todoktodok.presentation.view.discussiondetail.commentdetail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCommentDetailBinding
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.vm.CommentDetailViewModel
import com.team.todoktodok.presentation.view.discussiondetail.comments.vm.CommentsViewModelFactory

class CommentDetailFragment : Fragment(R.layout.fragment_comment_detail) {
    private val viewModel: CommentDetailViewModel by viewModels {
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
        val binding = FragmentCommentDetailBinding.bind(view)
        setOnNavigateUp(binding)
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
