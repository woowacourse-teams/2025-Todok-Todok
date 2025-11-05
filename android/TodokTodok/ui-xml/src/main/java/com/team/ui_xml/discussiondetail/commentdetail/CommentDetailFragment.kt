package com.team.ui_xml.discussiondetail.commentdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.team.core.ExceptionMessageConverter
import com.team.core.extension.registerPositiveResultListener
import com.team.core.extension.registerReportResultListener
import com.team.core.extension.registerResultListener
import com.team.ui_xml.R
import com.team.ui_xml.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.ui_xml.component.CommonDialog
import com.team.ui_xml.component.ReportDialog
import com.team.ui_xml.databinding.FragmentCommentDetailBinding
import com.team.ui_xml.databinding.MenuExternalDiscussionBinding
import com.team.ui_xml.databinding.MenuOwnedDiscussionBinding
import com.team.ui_xml.discussiondetail.BottomSheetVisibilityListener
import com.team.ui_xml.discussiondetail.commentcreate.CommentCreateBottomSheet
import com.team.ui_xml.discussiondetail.commentdetail.adapter.CommentDetailAdapter
import com.team.ui_xml.discussiondetail.commentdetail.adapter.CommentDetailItems
import com.team.ui_xml.discussiondetail.commentdetail.vm.CommentDetailViewModel
import com.team.ui_xml.discussiondetail.comments.vm.CommentsViewModel
import com.team.ui_xml.discussiondetail.replycreate.ReplyCreateBottomSheet
import com.team.ui_xml.discussiondetail.vm.DiscussionDetailViewModel
import com.team.ui_xml.profile.ProfileActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CommentDetailFragment : Fragment(R.layout.fragment_comment_detail) {
    private val adapter by lazy { CommentDetailAdapter(commentDetailHandler) }

    private val sharedViewModel: DiscussionDetailViewModel by activityViewModels()

    private val commentsViewModel: CommentsViewModel by viewModels(
        ownerProducer = { requireActivity() },
    )

    private val viewModel by viewModels<CommentDetailViewModel>()

    private var popupWindow: PopupWindow? = null

    @Inject
    lateinit var messageConverter: ExceptionMessageConverter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCommentDetailBinding.bind(view)
        setOnNavigateUp(binding)
        initAdapter(binding)
        initView(binding)
        setupOnClick(binding)
        setupObserve(binding)
        setupFragmentResultListener()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requestActivityClose()
                }
            },
        )
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

    private fun setupOnClick(binding: FragmentCommentDetailBinding) {
        with(binding) {
            tvInputReply.setOnClickListener { viewModel.showReplyCreate() }
            ivAddReply.setOnClickListener { viewModel.createReply() }
            ivReload.setOnClickListener { viewModel.reloadContents() }
        }
    }

    private fun setupObserve(binding: FragmentCommentDetailBinding) {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            if (value.isLoading) {
                binding.progressBar.show()
            } else {
                if (binding.progressBar.isVisible) binding.progressBar.hide()
                adapter.submitList(value.getCommentDetailItems())
                val hasContent = value.content.isNotBlank()
                binding.ivAddReply.isEnabled = hasContent
                binding.tvInputReply.text =
                    if (hasContent) value.content else getString(R.string.reply_input_hint)
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

    private fun requestActivityClose() {
        val hasReplyContent =
            viewModel.uiState.value
                ?.content
                ?.isNotBlank() ?: false
        val hasCommentContent =
            commentsViewModel.uiState.value
                ?.commentContent
                ?.isNotBlank() ?: false

        when {
            hasReplyContent -> showConfirmActivityClose(getString(R.string.reply_confirm_delete_message))
            hasCommentContent -> showConfirmActivityClose(getString(R.string.comment_confirm_delete_message))
            else -> sharedViewModel.onFinishEvent()
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

    private fun showConfirmActivityClose(deleteMessage: String) {
        val confirmDialog =
            CommonDialog
                .newInstance(
                    deleteMessage,
                    getString(R.string.all_delete_action),
                    REPLY_DRAFT_DISCARD_REQUEST_KEY_ACTIVITY,
                )
        confirmDialog.show(childFragmentManager, CommonDialog.TAG)
    }

    private fun showConfirmClose() {
        val confirmDialog =
            CommonDialog
                .newInstance(
                    getString(R.string.reply_confirm_delete_message),
                    getString(R.string.all_delete_action),
                    REPLY_DRAFT_DISCARD_REQUEST_KEY_POP,
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
            }

            is CommentDetailUiEvent.ShowReplyUpdate -> {
                showReplyUpdate(
                    commentDetailUiEvent.discussionId,
                    commentDetailUiEvent.commentId,
                    commentDetailUiEvent.replyId,
                    commentDetailUiEvent.content,
                    binding,
                )
            }

            CommentDetailUiEvent.DeleteComment -> {
                sharedViewModel.reloadDiscussion()
                commentsViewModel.showNewComment()
                parentFragmentManager.popBackStack()
            }

            CommentDetailUiEvent.ToggleCommentLike -> commentsViewModel.showNewComment()
            CommentDetailUiEvent.CommentUpdate -> commentsViewModel.showNewComment()
            CommentDetailUiEvent.DeleteReply -> {
                commentsViewModel.showNewComment()
            }

            is CommentDetailUiEvent.ShowError ->
                AlertSnackBar(
                    binding.root,
                    messageConverter(commentDetailUiEvent.exception),
                ).show()

            CommentDetailUiEvent.ShowReportCommentSuccessMessage ->
                showShortToast(R.string.all_report_comment_success)

            CommentDetailUiEvent.ShowReportReplySuccessMessage ->
                showShortToast(R.string.all_report_reply_success)

            is CommentDetailUiEvent.NavigateToProfile -> {
                navigateToProfile(commentDetailUiEvent.memberId)
            }
        }
    }

    private fun onNewReplyCommitted(binding: FragmentCommentDetailBinding) {
        commentsViewModel.showNewComment()
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
                        {
                            viewModel.updateComment(commentDetailItems.value.comment.content)
                            popupWindow?.dismiss()
                        },
                        {
                            showCommentDeleteDialog()
                            popupWindow?.dismiss()
                        },
                    )
                } else {
                    reportPopupView(
                        layoutInflater,
                    ) {
                        showCommentReportDialog()
                        popupWindow?.dismiss()
                    }
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
                    popupWindow?.dismiss()
                },
            )
        } else {
            reportPopupView(
                layoutInflater,
            ) {
                showReplyReportDialog(commentDetailItems.value.reply.replyId)
                popupWindow?.dismiss()
            }
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
        val bottomSheet = CommentCreateBottomSheet.Companion.newInstance(discussionId, commentId, content)
        bottomSheet.setVisibilityListener(getBottomSheetVisibilityListener(binding))
        bottomSheet.show(childFragmentManager, CommentCreateBottomSheet.Companion.TAG)
    }

    private fun showReplyCreate(
        discussionId: Long,
        commentId: Long,
        content: String,
        binding: FragmentCommentDetailBinding,
    ) {
        val bottomSheet = ReplyCreateBottomSheet.Companion.newInstance(discussionId, commentId, null, content)
        bottomSheet.setVisibilityListener(getBottomSheetVisibilityListener(binding))
        bottomSheet.show(childFragmentManager, ReplyCreateBottomSheet.Companion.TAG)
    }

    private fun showReplyUpdate(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
        content: String,
        binding: FragmentCommentDetailBinding,
    ) {
        val bottomSheet =
            ReplyCreateBottomSheet.Companion.newInstance(discussionId, commentId, replyId, content)
        bottomSheet.setVisibilityListener(getBottomSheetVisibilityListener(binding))
        bottomSheet.show(childFragmentManager, ReplyCreateBottomSheet.Companion.TAG)
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
            CommentCreateBottomSheet.Companion.COMMENT_REQUEST_KEY,
            CommentCreateBottomSheet.Companion.COMMENT_CREATED_RESULT_KEY,
        ) { viewModel.updatedComment() }

        childFragmentManager.registerResultListener(
            viewLifecycleOwner,
            ReplyCreateBottomSheet.Companion.REPLY_REQUEST_KEY,
            ReplyCreateBottomSheet.Companion.REPLY_CREATED_RESULT_KEY,
        ) { viewModel.reloadReplies() }

        childFragmentManager.registerPositiveResultListener(
            viewLifecycleOwner,
            REPLY_DRAFT_DISCARD_REQUEST_KEY_ACTIVITY,
            CommonDialog.RESULT_KEY_COMMON_DIALOG,
        ) { sharedViewModel.onFinishEvent() }

        childFragmentManager.registerPositiveResultListener(
            viewLifecycleOwner,
            REPLY_DRAFT_DISCARD_REQUEST_KEY_POP,
            CommonDialog.RESULT_KEY_COMMON_DIALOG,
        ) { parentFragmentManager.popBackStack() }

        childFragmentManager.registerReportResultListener(
            viewLifecycleOwner,
            COMMENT_REPORT_DIALOG_REQUEST_KEY,
            ReportDialog.RESULT_KEY_REPORT,
        ) { reason -> viewModel.reportComment(reason) }
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
        ) { reason -> viewModel.reportReply(replyId, reason) }

        childFragmentManager.registerPositiveResultListener(
            viewLifecycleOwner,
            REPLY_DELETE_DIALOG_REQUEST_KEY.format(replyId),
            CommonDialog.RESULT_KEY_COMMON_DIALOG,
        ) { viewModel.deleteReply(replyId) }
    }

    private fun showShortToast(
        @StringRes resId: Int,
    ) {
        Toast
            .makeText(requireContext(), resId, Toast.LENGTH_SHORT)
            .show()
    }

    private fun getBottomSheetVisibilityListener(binding: FragmentCommentDetailBinding) =
        object : BottomSheetVisibilityListener {
            override fun onBottomSheetShown() {
                binding.tvInputReply.visibility = View.GONE
                popupWindow?.dismiss()
            }

            override fun onBottomSheetDismissed() {
                binding.tvInputReply.visibility = View.VISIBLE
                popupWindow?.dismiss()
            }
        }

    private val commentDetailHandler =
        object : CommentDetailAdapter.Handler {
            override fun onClickReplyLike(replyId: Long) {
                viewModel.toggleReplyLike(replyId)
            }

            override fun onClickReplyUser(
                userId: Long,
                userName: String,
            ) {
                viewModel.navigateToOtherUserProfile(userId, userName)
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
                commentsViewModel.showNewComment()
            }

            override fun onClickCommentUser(
                userId: Long,
                userName: String,
            ) {
                viewModel.navigateToOtherUserProfile(userId, userName)
            }
        }

    private fun navigateToProfile(userId: Long) {
        val intent = ProfileActivity.Companion.Intent(requireContext(), userId)
        startActivity(intent)
    }

    companion object {
        const val TAG = "TAG_COMMENT_DETAIL"
        private const val COMMENT_DELETE_DIALOG_REQUEST_KEY = "comment_delete_dialog_request_key"
        private const val COMMENT_REPORT_DIALOG_REQUEST_KEY = "comment_report_dialog_request_key"

        private const val REPLY_DRAFT_DISCARD_REQUEST_KEY_ACTIVITY =
            "reply_draft_discard_request_key_activity"
        private const val REPLY_DRAFT_DISCARD_REQUEST_KEY_POP =
            "reply_draft_discard_request_key_pop"
        private const val REPLY_REPORT_DIALOG_REQUEST_KEY = "reply_report_dialog_request_key_%d"
        private const val REPLY_DELETE_DIALOG_REQUEST_KEY = "reply_delete_dialog_request_key_%d"

        fun newInstance(
            discussionId: Long,
            commentId: Long,
        ): CommentDetailFragment =
            CommentDetailFragment().apply {
                arguments =
                    bundleOf(
                        CommentDetailViewModel.Companion.KEY_DISCUSSION_ID to discussionId,
                        CommentDetailViewModel.Companion.KEY_COMMENT_ID to commentId,
                    )
            }
    }
}
