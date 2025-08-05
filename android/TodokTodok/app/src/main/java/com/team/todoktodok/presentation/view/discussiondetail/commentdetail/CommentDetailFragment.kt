package com.team.todoktodok.presentation.view.discussiondetail.commentdetail

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.vm.CommentDetailViewModel
import com.team.todoktodok.presentation.view.discussiondetail.comments.vm.CommentsViewModelFactory

class CommentDetailFragment : Fragment(R.layout.fragment_comment_detail) {
    private val viewModel: CommentDetailViewModel by viewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        CommentsViewModelFactory(
            repositoryModule.commentRepository,
        )
    }

    companion object {
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
