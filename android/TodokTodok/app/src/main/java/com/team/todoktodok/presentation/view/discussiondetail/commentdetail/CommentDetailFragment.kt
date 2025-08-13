package com.team.todoktodok.presentation.view.discussiondetail.commentdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCommentDetailBinding
import com.team.todoktodok.databinding.MenuExternalDiscussionBinding
import com.team.todoktodok.databinding.MenuOwnedDiscussionBinding
import com.team.todoktodok.presentation.view.discussiondetail.BottomSheetVisibilityListener
import com.team.todoktodok.presentation.view.discussiondetail.commentcreate.CommentCreateBottomSheet
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.adapter.CommentDetailAdapter
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.adapter.CommentDetailItems
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.vm.CommentDetailViewModel
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.vm.CommentDetailViewModelFactory
import com.team.todoktodok.presentation.view.discussiondetail.comments.vm.CommentsViewModel
import com.team.todoktodok.presentation.view.discussiondetail.replycreate.ReplyCreateBottomSheet
import com.team.todoktodok.presentation.view.discussiondetail.vm.DiscussionDetailViewModel
import com.team.todoktodok.presentation.view.profile.ProfileActivity

class CommentDetailFragment : Fragment(R.layout.fragment_comment_detail) {
    private val adapter by lazy { CommentDetailAdapter(commentDetailHandler) }

    private val sharedViewModel: DiscussionDetailViewModel by activityViewModels()

    private val commentsViewModel: CommentsViewModel by viewModels(
        ownerProducer = { requireParentFragment() },
    )

    private val viewModel: CommentDetailViewModel by viewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        CommentDetailViewModelFactory(
            repositoryModule.commentRepository,
            repositoryModule.replyRepository,
            repositoryModule.tokenRepository,
        )
    }

    private var popupWindow: PopupWindow? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCommentDetailBinding.bind(view)
        setOnNavigateUp(binding)
        initAdapter(binding)
        initView(binding)
        setupOnClickAddReply(binding)
        setupObserve(binding)
        setupFragmentResultListener()
    }

    override fun onDestroyView() {
        popupWindow?.dismiss()
        popupWindow = null
        super.onDestroyView()
    }

    fun initAdapter(binding: FragmentCommentDetailBinding) {
        binding.rvItems.adapter = adapter
    }

    fun initView(binding: FragmentCommentDetailBinding) {
        with(binding) {
            rvItems.itemAnimator = null
        }
    }

    private fun setupOnClickAddReply(binding: FragmentCommentDetailBinding) {
        with(binding) {
            tvInputComment.setOnClickListener { viewModel.showReplyCreate() }
        }
    }

    fun setupObserve(binding: FragmentCommentDetailBinding) {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            adapter.submitList(value.getCommentDetailItems())
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
                commentsViewModel.reloadComments()
                sharedViewModel.reloadDiscussion()
                binding.rvItems.doOnPreDraw {
                    binding.rvItems.smoothScrollToPosition(adapter.itemCount)
                }
            }

            is CommentDetailUiEvent.ShowCommentUpdate -> {
                showCommentCreate(
                    commentDetailUiEvent.discussionId,
                    commentDetailUiEvent.commentId,
                    binding,
                )
                popupWindow?.dismiss()
            }

            is CommentDetailUiEvent.ShowReplyUpdate -> {
                showReplyUpdate(
                    commentDetailUiEvent.discussionId,
                    commentDetailUiEvent.commentId,
                    commentDetailUiEvent.replyId,
                    commentDetailUiEvent.content,
                    binding,
                )
                popupWindow?.dismiss()
            }

            CommentDetailUiEvent.DeleteComment -> {
                sharedViewModel.reloadDiscussion()
                commentsViewModel.reloadComments()
                parentFragmentManager.popBackStack()
            }

            CommentDetailUiEvent.ToggleCommentLike -> commentsViewModel.reloadComments()
            CommentDetailUiEvent.CommentUpdate -> commentsViewModel.reloadComments()
            CommentDetailUiEvent.DeleteReply -> {
                popupWindow?.dismiss()
                commentsViewModel.reloadComments()
            }
        }
    }

    private fun bindCommentDetailPopupView(commentDetailItems: CommentDetailItems) =
        when (commentDetailItems) {
            is CommentDetailItems.CommentItem ->
                if (commentDetailItems.value.isMyComment) {
                    optionPopupView(
                        layoutInflater,
                        { viewModel.updateComment() },
                        { viewModel.deleteComment() },
                    )
                } else {
                    reportPopupView(
                        layoutInflater,
                    ) {
                        viewModel.reportComment()
                    }
                }

            is CommentDetailItems.ReplyItem ->
                if (commentDetailItems.value.isMyReply) {
                    optionPopupView(
                        layoutInflater,
                        {
                            viewModel.updateReply(
                                commentDetailItems.value.reply.replyId,
                                commentDetailItems.value.reply.content,
                            )
                        },
                        { viewModel.deleteReply(commentDetailItems.value.reply.replyId) },
                    )
                } else {
                    reportPopupView(
                        layoutInflater,
                    ) { viewModel.reportReply(commentDetailItems.value.reply.replyId) }
                }
        }

    private fun optionPopupView(
        layoutInflater: LayoutInflater,
        onUpdate: () -> Unit,
        onDelete: () -> Unit,
    ): PopupWindow {
        val binding = MenuOwnedDiscussionBinding.inflate(layoutInflater)
        binding.tvEdit.setOnClickListener { onUpdate() }
        binding.tvDelete.setOnClickListener { onDelete() }
        return createPopupView(binding.root)
    }

    private fun reportPopupView(
        layoutInflater: LayoutInflater,
        onReport: () -> Unit,
    ): PopupWindow {
        val binding = MenuExternalDiscussionBinding.inflate(layoutInflater)
        binding.tvReport.setOnClickListener { onReport() }
        return createPopupView(binding.root)
    }

    private fun showCommentCreate(
        discussionId: Long,
        commentId: Long?,
        binding: FragmentCommentDetailBinding,
    ) {
        val bottomSheet = CommentCreateBottomSheet.newInstance(discussionId, commentId)
        bottomSheet.setVisibilityListener(getBottomSheetVisibilityListener(binding))
        bottomSheet.show(childFragmentManager, CommentCreateBottomSheet.TAG)
    }

    private fun showReplyCreate(
        discussionId: Long,
        commentId: Long,
        binding: FragmentCommentDetailBinding,
    ) {
        val bottomSheet = ReplyCreateBottomSheet.newInstance(discussionId, commentId, null, null)
        bottomSheet.setVisibilityListener(getBottomSheetVisibilityListener(binding))
        bottomSheet.show(childFragmentManager, ReplyCreateBottomSheet.TAG)
    }

    private fun showReplyUpdate(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
        content: String,
        binding: FragmentCommentDetailBinding,
    ) {
        val bottomSheet =
            ReplyCreateBottomSheet.newInstance(discussionId, commentId, replyId, content)
        bottomSheet.setVisibilityListener(getBottomSheetVisibilityListener(binding))
        bottomSheet.show(childFragmentManager, ReplyCreateBottomSheet.TAG)
    }

    private fun createPopupView(popupView: View) =
        PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true,
        )

    private fun setupFragmentResultListener() {
        childFragmentManager.setFragmentResultListener(
            CommentCreateBottomSheet.COMMENT_REQUEST_KEY,
            this,
        ) { _, bundle ->
            val result = bundle.getBoolean(CommentCreateBottomSheet.COMMENT_CREATED_RESULT_KEY)
            if (result) {
                viewModel.updatedComment()
            }
        }
        childFragmentManager.setFragmentResultListener(
            ReplyCreateBottomSheet.REPLY_REQUEST_KEY,
            this,
        ) { _, bundle ->
            val result = bundle.getBoolean(ReplyCreateBottomSheet.REPLY_CREATED_RESULT_KEY)
            if (result) {
                viewModel.reloadComment()
            }
        }
    }

    private fun getBottomSheetVisibilityListener(binding: FragmentCommentDetailBinding) =
        object : BottomSheetVisibilityListener {
            override fun onBottomSheetShown() {
                binding.tvInputComment.visibility = View.GONE
                popupWindow?.dismiss()
            }

            override fun onBottomSheetDismissed() {
                binding.tvInputComment.visibility = View.VISIBLE
                popupWindow?.dismiss()
            }
        }

    fun setOnNavigateUp(binding: FragmentCommentDetailBinding) {
        binding.ivCommentDetailBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private val commentDetailHandler =
        object : CommentDetailAdapter.Handler {
            override fun onClickReplyLike(replyId: Long) {
                viewModel.toggleReplyLike(replyId)
            }

            override fun onClickReplyUserName(userId: Long) {
                navigateToProfile(userId)
            }

            override fun onClickReplyOption(
                item: CommentDetailItems.ReplyItem,
                anchorView: View,
            ) {
                popupWindow = bindCommentDetailPopupView(item)
                if (popupWindow?.isShowing == true) {
                    popupWindow?.dismiss()
                } else {
                    popupWindow?.showAsDropDown(anchorView)
                }
            }

            override fun onClickCommentOption(
                item: CommentDetailItems.CommentItem,
                anchorView: View,
            ) {
                popupWindow = bindCommentDetailPopupView(item)
                if (popupWindow?.isShowing == true) {
                    popupWindow?.dismiss()
                } else {
                    popupWindow?.showAsDropDown(anchorView)
                }
            }

            override fun onClickCommentLike() {
                viewModel.toggleCommentLike()
                commentsViewModel.reloadComments()
            }

            override fun onClickCommentUserName(userId: Long) {
                navigateToProfile(userId)
            }
        }

    private fun navigateToProfile(userId: Long) {
        val intent = ProfileActivity.Intent(requireContext(), userId)
        startActivity(intent)
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
