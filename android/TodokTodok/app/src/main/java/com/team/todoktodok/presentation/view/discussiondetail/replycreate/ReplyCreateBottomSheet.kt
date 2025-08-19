package com.team.todoktodok.presentation.view.discussiondetail.replycreate

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCommentCreateBottomSheetBinding
import com.team.todoktodok.presentation.view.discussiondetail.BottomSheetVisibilityListener
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.vm.CommentDetailViewModel
import com.team.todoktodok.presentation.view.discussiondetail.replycreate.vm.ReplyCreateViewModel
import com.team.todoktodok.presentation.view.discussiondetail.replycreate.vm.ReplyCreateViewModelFactory

class ReplyCreateBottomSheet : BottomSheetDialogFragment(R.layout.fragment_comment_create_bottom_sheet) {
    private val viewModel by viewModels<ReplyCreateViewModel> {
        val repoModule = (requireActivity().application as App).container.repositoryModule
        ReplyCreateViewModelFactory(repoModule.replyRepository)
    }

    private val commentDetailViewModel by viewModels<CommentDetailViewModel>(
        ownerProducer = { requireParentFragment() },
    )

    private var visibilityListener: BottomSheetVisibilityListener? = null

    fun setVisibilityListener(listener: BottomSheetVisibilityListener) {
        visibilityListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme).apply {
            window?.setDimAmount(DIM_AMOUNT)
        }

    override fun onStart() {
        super.onStart()
        visibilityListener?.onBottomSheetShown()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCommentCreateBottomSheetBinding.bind(view)
        initView(binding)
        setupOnClickAddComment(binding)
        setupOnChangeComment(binding)
        setupObserve(binding)
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.saveReply()
        visibilityListener?.onBottomSheetDismissed()
        super.onDismiss(dialog)
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    private fun initView(binding: FragmentCommentCreateBottomSheetBinding) {
        with(binding) {
            etTextCommentContent.requestFocus()
            val content = viewModel.replyContent.value ?: ""
            etTextCommentContent.setText(content)
            viewModel.onReplyChanged(content)
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etTextCommentContent, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun setupOnClickAddComment(binding: FragmentCommentCreateBottomSheetBinding) {
        with(binding) {
            ivAddComment.setOnClickListener {
                viewModel.submitReply()
                etTextCommentContent.text?.clear()
                etTextCommentContent.clearFocus()
            }
        }
    }

    private fun setupObserve(binding: FragmentCommentCreateBottomSheetBinding) {
        viewModel.uiEvent.observe(viewLifecycleOwner) { value ->
            handleUiEvent(value)
        }
        viewModel.replyContent.observe(viewLifecycleOwner) { value ->
            binding.ivAddComment.isEnabled = value.isNotBlank()
        }
    }

    private fun handleUiEvent(uiEvent: ReplyCreateUiEvent) {
        when (uiEvent) {
            ReplyCreateUiEvent.CreateReply -> {
                setFragmentResult(
                    REPLY_REQUEST_KEY,
                    bundleOf(REPLY_CREATED_RESULT_KEY to true),
                )
                dismiss()
            }

            is ReplyCreateUiEvent.SaveContent -> {
                commentDetailViewModel.updateContent(uiEvent.content)
            }
        }
    }

    private fun setupOnChangeComment(binding: FragmentCommentCreateBottomSheetBinding) {
        with(binding) {
            etTextCommentContent.addTextChangedListener { editable ->
                viewModel.onReplyChanged(editable)
            }
        }
    }

    companion object {
        const val REPLY_REQUEST_KEY = "reply_request_key"
        const val REPLY_CREATED_RESULT_KEY = "IS_REPLY_CREATED"
        const val TAG = "COMMENT_DETAIL_SHEET"

        private const val DIM_AMOUNT = 0.001f

        fun newInstance(
            discussionId: Long,
            commentId: Long,
            replyId: Long?,
            content: String?,
        ): ReplyCreateBottomSheet =
            ReplyCreateBottomSheet().apply {
                arguments =
                    bundleOf(
                        ReplyCreateViewModel.KEY_DISCUSSION_ID to discussionId,
                        ReplyCreateViewModel.KEY_COMMENT_ID to commentId,
                        ReplyCreateViewModel.KEY_REPLY_ID to replyId,
                        ReplyCreateViewModel.KEY_REPLY_CONTENT to content,
                    )
            }
    }
}
