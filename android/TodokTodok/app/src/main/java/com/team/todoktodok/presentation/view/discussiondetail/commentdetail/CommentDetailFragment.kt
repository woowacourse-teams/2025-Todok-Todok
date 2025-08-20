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
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.core.component.CommonDialog
import com.team.todoktodok.presentation.core.component.ReportDialog
import com.team.todoktodok.presentation.core.ext.registerPositiveResultListener
import com.team.todoktodok.presentation.core.ext.registerReportResultListener
import com.team.todoktodok.presentation.core.ext.registerResultListener
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
        ownerProducer = { requireActivity() },
    )

    private val viewModel by viewModels<CommentDetailViewModel> {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        CommentDetailViewModelFactory(
            repositoryModule.commentRepository,
            repositoryModule.replyRepository,
            repositoryModule.tokenRepository,
        )
    }

    private var popupWindow: PopupWindow? = null

    private val messageConverter: ExceptionMessageConverter by lazy {
        ExceptionMessageConverter()
    }

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
            if (value.isLoading) {
                binding.progressBar.show()
            } else {
                binding.progressBar.hide()
                adapter.submitList(value.getCommentDetailItems())
                val currentContent = value.content
                if (currentContent.isNotBlank()) binding.tvInputComment.text = currentContent
            }
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { value ->
            handleEvent(value, binding)
        }
    }

    fun setOnNavigateUp(binding: FragmentCommentDetailBinding) {
        binding.ivCommentDetailBack.setOnClickListener {
            requestClose()
        }
    }

    private fun requestClose() {
        if (viewModel.uiState.value
                ?.content
                ?.isNotEmpty() == true
        ) {
            showConfirmClose()
        } else {
            parentFragmentManager.popBackStack()
        }
    }

    private fun showConfirmClose() {
        val confirmDialog =
            CommonDialog
                .newInstance(
                    getString(R.string.confirm_delete_message),
                    getString(R.string.all_delete_action),
                    REPLY_CONTENT_DELETE_DIALOG_REQUEST_KEY,
                )
        confirmDialog.show(childFragmentManager, CommonDialog.TAG)
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
                    commentDetailUiEvent.content,
                    binding,
                )

            is CommentDetailUiEvent.ShowNewReply -> {
                onNewReplyCommitted(binding)
            }

            is CommentDetailUiEvent.ShowCommentUpdate -> {
                showCommentCreate(
                    commentDetailUiEvent.discussionId,
                    commentDetailUiEvent.commentId,
                    commentDetailUiEvent.comment,
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

            is CommentDetailUiEvent.ShowError ->
                AlertSnackBar(
                    binding.root,
                    messageConverter(commentDetailUiEvent.exception),
                ).show()
        }
    }

    private fun onNewReplyCommitted(binding: FragmentCommentDetailBinding) {
        commentsViewModel.reloadComments()
        sharedViewModel.reloadDiscussion()
        binding.rvItems.doOnPreDraw {
            binding.rvItems.smoothScrollToPosition(adapter.itemCount)
        }
    }

    private fun bindCommentDetailPopupView(commentDetailItems: CommentDetailItems) =
        when (commentDetailItems) {
            is CommentDetailItems.CommentItem ->
                if (commentDetailItems.value.isMyComment) {
                    optionPopupView(
                        layoutInflater,
                        { viewModel.updateComment(commentDetailItems.value.comment.content) },
                        { showCommentDeleteDialog() },
                    )
                } else {
                    reportPopupView(
                        layoutInflater,
                        ::showCommentReportDialog,
                    )
                }

            is CommentDetailItems.ReplyItem -> showReplyOptions(commentDetailItems)
        }

    private fun showReplyOptions(commentDetailItems: CommentDetailItems.ReplyItem) =
        if (commentDetailItems.value.isMyReply) {
            optionPopupView(
                layoutInflater,
                {
                    viewModel.updateReply(
                        commentDetailItems.value.reply.replyId,
                        commentDetailItems.value.reply.content,
                    )
                },
                {
                    showReplyDeleteDialog(commentDetailItems.value.reply.replyId)
                },
            )
        } else {
            reportPopupView(
                layoutInflater,
            ) { showReplyReportDialog(commentDetailItems.value.reply.replyId) }
        }

    private fun showCommentReportDialog() {
        val dialog =
            ReportDialog.newInstance(
                COMMENT_REPORT_DIALOG_REQUEST_KEY,
            )
        dialog.show(childFragmentManager, ReportDialog.TAG)
    }

    private fun showCommentDeleteDialog() {
        val dialog =
            CommonDialog.newInstance(
                getString(R.string.all_comment_delete_confirm),
                getString(R.string.all_delete_action),
                COMMENT_DELETE_DIALOG_REQUEST_KEY,
            )
        dialog.show(childFragmentManager, CommonDialog.TAG)
    }

    private fun showReplyReportDialog(replyId: Long) {
        val dialog =
            ReportDialog.newInstance(
                REPLY_REPORT_DIALOG_REQUEST_KEY.format(replyId),
            )
        dialog.show(childFragmentManager, ReportDialog.TAG)
    }

    private fun showReplyDeleteDialog(replyId: Long) {
        val dialog =
            CommonDialog.newInstance(
                getString(R.string.all_reply_delete_confirm),
                getString(R.string.all_delete_action),
                REPLY_DELETE_DIALOG_REQUEST_KEY.format(replyId),
            )
        dialog.show(childFragmentManager, CommonDialog.TAG)
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
        binding.tvReport.setOnClickListener {
            onReport()
        }
        return createPopupView(binding.root)
    }

    private fun showCommentCreate(
        discussionId: Long,
        commentId: Long?,
        content: String?,
        binding: FragmentCommentDetailBinding,
    ) {
        val bottomSheet = CommentCreateBottomSheet.newInstance(discussionId, commentId, content)
        bottomSheet.setVisibilityListener(getBottomSheetVisibilityListener(binding))
        bottomSheet.show(childFragmentManager, CommentCreateBottomSheet.TAG)
    }

    private fun showReplyCreate(
        discussionId: Long,
        commentId: Long,
        content: String,
        binding: FragmentCommentDetailBinding,
    ) {
        val bottomSheet = ReplyCreateBottomSheet.newInstance(discussionId, commentId, null, content)
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
        childFragmentManager.registerResultListener(
            viewLifecycleOwner,
            CommentCreateBottomSheet.COMMENT_REQUEST_KEY,
            CommentCreateBottomSheet.COMMENT_CREATED_RESULT_KEY,
        ) { viewModel.updatedComment() }

        childFragmentManager.registerResultListener(
            viewLifecycleOwner,
            ReplyCreateBottomSheet.REPLY_REQUEST_KEY,
            ReplyCreateBottomSheet.REPLY_CREATED_RESULT_KEY,
        ) { viewModel.reloadComment() }

        childFragmentManager.registerPositiveResultListener(
            viewLifecycleOwner,
            REPLY_CONTENT_DELETE_DIALOG_REQUEST_KEY,
            CommonDialog.RESULT_KEY_COMMON_DIALOG,
        ) { parentFragmentManager.popBackStack() }

        childFragmentManager.registerReportResultListener(
            viewLifecycleOwner,
            COMMENT_REPORT_DIALOG_REQUEST_KEY,
            ReportDialog.RESULT_KEY_REPORT,
        ) { viewModel.reportComment() }
        childFragmentManager.registerPositiveResultListener(
            viewLifecycleOwner,
            COMMENT_DELETE_DIALOG_REQUEST_KEY,
            CommonDialog.RESULT_KEY_COMMON_DIALOG,
        ) { viewModel.deleteComment() }
    }

    private fun setupFragmentReplyResultListener(replyId: Long) {
        childFragmentManager.registerReportResultListener(
            viewLifecycleOwner,
            REPLY_REPORT_DIALOG_REQUEST_KEY.format(replyId),
            ReportDialog.RESULT_KEY_REPORT,
        ) { viewModel.reportReply(replyId) }

        childFragmentManager.registerPositiveResultListener(
            viewLifecycleOwner,
            REPLY_DELETE_DIALOG_REQUEST_KEY.format(replyId),
            CommonDialog.RESULT_KEY_COMMON_DIALOG,
        ) { viewModel.deleteReply(replyId) }
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

    private val commentDetailHandler =
        object : CommentDetailAdapter.Handler {
            override fun onClickReplyLike(replyId: Long) {
                viewModel.toggleReplyLike(replyId)
            }

            override fun onClickReplyUser(userId: Long) {
                navigateToProfile(userId)
            }

            override fun onClickReplyOption(
                item: CommentDetailItems.ReplyItem,
                anchorView: View,
            ) {
                setupFragmentReplyResultListener(item.value.reply.replyId)
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
        private const val COMMENT_DELETE_DIALOG_REQUEST_KEY = "comment_delete_dialog_request_key"
        private const val COMMENT_REPORT_DIALOG_REQUEST_KEY = "comment_report_dialog_request_key"
        private const val REPLY_CONTENT_DELETE_DIALOG_REQUEST_KEY =
            "reply_content_delete_dialog_request_key"
        private const val REPLY_REPORT_DIALOG_REQUEST_KEY = "reply_report_dialog_request_key_%d"
        private const val REPLY_DELETE_DIALOG_REQUEST_KEY = "reply_delete_dialog_request_key_%d"

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
