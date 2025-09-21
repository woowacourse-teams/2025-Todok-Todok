package com.team.todoktodok.presentation.xml.discussiondetail.comments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCommentsBinding
import com.team.todoktodok.databinding.MenuExternalDiscussionBinding
import com.team.todoktodok.databinding.MenuOwnedDiscussionBinding
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.core.component.CommonDialog
import com.team.todoktodok.presentation.core.component.ReportDialog
import com.team.todoktodok.presentation.core.ext.registerPositiveResultListener
import com.team.todoktodok.presentation.core.ext.registerReportResultListener
import com.team.todoktodok.presentation.core.ext.registerResultListener
import com.team.todoktodok.presentation.xml.discussiondetail.BottomSheetVisibilityListener
import com.team.todoktodok.presentation.xml.discussiondetail.commentcreate.CommentCreateBottomSheet
import com.team.todoktodok.presentation.xml.discussiondetail.commentdetail.CommentDetailFragment
import com.team.todoktodok.presentation.xml.discussiondetail.comments.adapter.CommentAdapter
import com.team.todoktodok.presentation.xml.discussiondetail.comments.vm.CommentsViewModel
import com.team.todoktodok.presentation.xml.discussiondetail.comments.vm.CommentsViewModelFactory
import com.team.todoktodok.presentation.xml.discussiondetail.model.CommentItemUiState
import com.team.todoktodok.presentation.xml.discussiondetail.vm.DiscussionDetailViewModel
import com.team.todoktodok.presentation.xml.profile.ProfileActivity

class CommentsFragment : BottomSheetDialogFragment(R.layout.fragment_comments) {
    private val adapter by lazy { CommentAdapter(adapterHandler) }

    private val layoutManager by lazy { LinearLayoutManager(requireContext()) }

    private val sharedViewModel: DiscussionDetailViewModel by activityViewModels()

    private val viewModel by viewModels<CommentsViewModel>(
        ownerProducer = { requireActivity() },
        factoryProducer = {
            val repositoryModule = (requireActivity().application as App).container.repositoryModule
            CommentsViewModelFactory(
                repositoryModule.commentRepository,
                repositoryModule.tokenRepository,
            )
        },
        extrasProducer = {
            MutableCreationExtras(requireActivity().defaultViewModelCreationExtras).apply {
                this[DEFAULT_ARGS_KEY] = buildCommentArgs()
            }
        },
    )

    private fun buildCommentArgs(): Bundle {
        val args = requireArguments()
        return Bundle().apply {
            putLong(
                CommentsViewModel.KEY_DISCUSSION_ID,
                args.getLong(CommentsViewModel.KEY_DISCUSSION_ID),
            )
        }
    }

    private var popupWindow: PopupWindow? = null

    private val messageConverter by lazy {
        ExceptionMessageConverter()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCommentsBinding.bind(view)
        viewModel.reloadComments()
        initAdapter(binding)
        setupOnClick(binding)
        setupObserve(binding)
        setupFragmentResultListener()
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onStop() {
        super.onStop()
        saveRvStateIfPossible()
    }

    private fun saveRvStateIfPossible() {
        viewModel.loadCommentsShowState(layoutManager.onSaveInstanceState())
    }

    override fun onDestroyView() {
        popupWindow?.dismiss()
        popupWindow = null
        super.onDestroyView()
    }

    private fun initAdapter(binding: FragmentCommentsBinding) {
        binding.rvComments.apply {
            layoutManager = this@CommentsFragment.layoutManager
            adapter = this@CommentsFragment.adapter
            setHasFixedSize(true)
        }
    }

    private fun setupOnClick(binding: FragmentCommentsBinding) {
        with(binding) {
            tvInputComment.setOnClickListener { viewModel.showCommentCreate() }
            ivAddComment.setOnClickListener { viewModel.createComment() }
        }
    }

    private fun setupObserve(binding: FragmentCommentsBinding) {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (state.isLoading) {
                binding.progressBar.show()
            } else {
                binding.progressBar.hide()
            }
            if (state.comments.isEmpty()) {
                binding.llNothingCommentsLayout.visibility = View.VISIBLE
            } else {
                binding.llNothingCommentsLayout.visibility = View.GONE
            }
            val hasCommentText = state.commentContent.isNotBlank()
            binding.ivAddComment.isEnabled = hasCommentText
            binding.tvInputComment.text =
                if (hasCommentText) state.commentContent else getString(R.string.comment_input_hint)
            adapter.submitList(state.comments) {
                viewModel.commentsRvState?.let { saved ->
                    binding.rvComments.layoutManager?.onRestoreInstanceState(saved)
                    viewModel.loadCommentsShowState(null)
                }
            }

            val draft = state.commentContent
            if (draft.isNotBlank() && binding.tvInputComment.text.toString() != draft) {
                binding.tvInputComment.text = draft
            }
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
                    commentsUiEvent.content,
                )

            CommentsUiEvent.ShowNewComment -> {
                sharedViewModel.reloadDiscussion()
                binding.rvComments.doOnPreDraw {
                    binding.rvComments.smoothScrollToPosition(adapter.itemCount)
                }
            }

            is CommentsUiEvent.ShowCommentUpdate -> {
                showCommentCreate(
                    commentsUiEvent.discussionId,
                    binding,
                    commentsUiEvent.commentId,
                    commentsUiEvent.content,
                )
            }

            CommentsUiEvent.DeleteComment -> sharedViewModel.reloadDiscussion()
            is CommentsUiEvent.ShowError ->
                AlertSnackBar(
                    binding.root,
                    messageConverter(commentsUiEvent.exception),
                ).show()

            CommentsUiEvent.ShowReportCommentSuccessMessage ->
                showShortToast(R.string.all_report_comment_success)
        }
    }

    private fun showCommentCreate(
        discussionId: Long,
        binding: FragmentCommentsBinding,
        commentId: Long?,
        content: String?,
    ) {
        val bottomSheet = CommentCreateBottomSheet.newInstance(discussionId, commentId, content)
        bottomSheet.setVisibilityListener(getBottomSheetVisibilityListener(binding))
        bottomSheet.show(childFragmentManager, CommentCreateBottomSheet.TAG)
    }

    private fun setupFragmentResultListener() {
        childFragmentManager.registerResultListener(
            viewLifecycleOwner,
            CommentCreateBottomSheet.COMMENT_REQUEST_KEY,
            CommentCreateBottomSheet.COMMENT_CREATED_RESULT_KEY,
        ) {
            viewModel.showNewComment()
        }
    }

    private fun createPopUpView(popupView: View) =
        PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true,
        )

    private fun getPopUpView(commentItemUiState: CommentItemUiState): PopupWindow =
        if (commentItemUiState.isMyComment) {
            createOwnedCommentPopup(commentItemUiState)
        } else {
            createExternalCommentPopup(commentItemUiState)
        }

    private fun createOwnedCommentPopup(state: CommentItemUiState): PopupWindow {
        val commentId = state.comment.id
        val requestKey = COMMENT_DELETE_DIALOG_REQUEST_KEY.format(commentId)

        childFragmentManager.registerPositiveResultListener(
            viewLifecycleOwner,
            requestKey,
            CommonDialog.RESULT_KEY_COMMON_DIALOG,
        ) { viewModel.deleteComment(commentId) }

        val binding = MenuOwnedDiscussionBinding.inflate(layoutInflater)

        binding.tvEdit.setOnClickListener {
            viewModel.updateComment(commentId, state.comment.content)
            popupWindow?.dismiss()
        }

        binding.tvDelete.setOnClickListener {
            showDeleteDialog(requestKey)
            popupWindow?.dismiss()
        }

        return createPopUpView(binding.root)
    }

    private fun createExternalCommentPopup(state: CommentItemUiState): PopupWindow {
        val commentId = state.comment.id
        val requestKey = COMMENT_REPORT_DIALOG_REQUEST_KEY.format(commentId)

        childFragmentManager.registerReportResultListener(
            viewLifecycleOwner,
            requestKey,
            ReportDialog.RESULT_KEY_REPORT,
        ) { reason -> viewModel.reportComment(commentId, reason) }

        val binding = MenuExternalDiscussionBinding.inflate(layoutInflater)

        binding.tvReport.setOnClickListener {
            showReportDialog(requestKey)
            popupWindow?.dismiss()
        }

        return createPopUpView(binding.root)
    }

    private fun showReportDialog(requestKey: String) {
        val dialog =
            ReportDialog.newInstance(
                requestKey,
            )
        dialog.show(childFragmentManager, ReportDialog.TAG)
    }

    private fun showDeleteDialog(requestKey: String) {
        val dialog =
            CommonDialog.newInstance(
                getString(R.string.all_comment_delete_confirm),
                getString(R.string.all_delete_action),
                requestKey,
            )
        dialog.show(childFragmentManager, CommonDialog.TAG)
    }

    private fun showShortToast(
        @StringRes resId: Int,
    ) {
        Toast
            .makeText(requireContext(), resId, Toast.LENGTH_SHORT)
            .show()
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
                    setReorderingAllowed(true)
                    setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right,
                    )
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
                commentItemUiState: CommentItemUiState,
                view: View,
            ) {
                popupWindow = getPopUpView(commentItemUiState)
                if (popupWindow?.isShowing == true) {
                    popupWindow?.dismiss()
                } else {
                    popupWindow?.showAsDropDown(view)
                }
            }

            override fun onToggleLike(commentId: Long) {
                viewModel.toggleLike(commentId)
            }

            override fun onClickUser(userId: Long) {
                navigateToProfile(userId)
            }
        }

    fun navigateToProfile(userId: Long) {
        val intent = ProfileActivity.Intent(requireContext(), userId)
        startActivity(intent)
    }

    companion object {
        const val TAG = "COMMENTS"

        private const val COMMENT_DELETE_DIALOG_REQUEST_KEY = "comment_delete_dialog_request_key_%d"
        private const val COMMENT_REPORT_DIALOG_REQUEST_KEY = "comment_report_dialog_request_key_%d"

        fun newInstance(discussionId: Long): CommentsFragment =
            CommentsFragment().apply {
                arguments =
                    bundleOf(
                        CommentsViewModel.KEY_DISCUSSION_ID to discussionId,
                    )
            }
    }
}
