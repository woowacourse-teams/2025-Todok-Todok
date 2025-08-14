package com.team.todoktodok.presentation.view.discussiondetail.commentcreate

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCommentCreateBottomSheetBinding
import com.team.todoktodok.presentation.view.discussiondetail.BottomSheetVisibilityListener
import com.team.todoktodok.presentation.view.discussiondetail.commentcreate.vm.CommentCreateViewModel
import com.team.todoktodok.presentation.view.discussiondetail.commentcreate.vm.CommentCreateViewModelFactory
import com.team.todoktodok.presentation.view.discussiondetail.replycreate.vm.ReplyCreateViewModelFactory

class CommentCreateBottomSheet : BottomSheetDialogFragment(R.layout.fragment_comment_create_bottom_sheet) {
    private val viewModel by viewModels<CommentCreateViewModel>(
        ownerProducer = { requireActivity() },
        factoryProducer = {
            val repositoryModule = (requireActivity().application as App).container.repositoryModule
            CommentCreateViewModelFactory(
                repositoryModule.commentRepository,
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
                CommentCreateViewModel.KEY_DISCUSSION_ID,
                args.getLong(CommentCreateViewModel.KEY_DISCUSSION_ID),
            )
            if (args.containsKey(CommentCreateViewModel.KEY_COMMENT_ID)) {
                putLong(
                    CommentCreateViewModel.KEY_COMMENT_ID,
                    args.getLong(CommentCreateViewModel.KEY_COMMENT_ID),
                )
            }
            args.getString(CommentCreateViewModel.KEY_COMMENT_CONTENT)?.let {
                putString(CommentCreateViewModel.KEY_COMMENT_CONTENT, it)
            }
        }
    }

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
        super.onDismiss(dialog)
        visibilityListener?.onBottomSheetDismissed()
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    private fun initView(binding: FragmentCommentCreateBottomSheetBinding) {
        with(binding) {
            etTextCommentContent.requestFocus()
            etTextCommentContent.setText(viewModel.commentText.value)
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etTextCommentContent, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun setupOnClickAddComment(binding: FragmentCommentCreateBottomSheetBinding) {
        with(binding) {
            ivAddComment.setOnClickListener {
                viewModel.submitComment()
                etTextCommentContent.text?.clear()
                etTextCommentContent.clearFocus()
            }
        }
    }

    private fun setupObserve(binding: FragmentCommentCreateBottomSheetBinding) {
        viewModel.uiEvent.observe(viewLifecycleOwner) { value ->
            handleUiEvent(value)
        }
        viewModel.commentText.observe(viewLifecycleOwner) { value ->
            binding.ivAddComment.isEnabled = value.isNotBlank()
        }
    }

    private fun handleUiEvent(uiEvent: CommentCreateUiEvent) {
        when (uiEvent) {
            is CommentCreateUiEvent.SubmitComment -> {
                setFragmentResult(COMMENT_REQUEST_KEY, bundleOf(COMMENT_CREATED_RESULT_KEY to true))
                dismiss()
            }
        }
    }

    private fun setupOnChangeComment(binding: FragmentCommentCreateBottomSheetBinding) {
        with(binding) {
            etTextCommentContent.addTextChangedListener { editable ->
                viewModel.onCommentChanged(editable)
            }
        }
    }

    companion object {
        const val COMMENT_REQUEST_KEY = "comment_create_result"
        const val COMMENT_CREATED_RESULT_KEY = "IS_COMMENT_CREATED"
        const val TAG = "COMMENTS_BOTTOM_SHEET"

        private const val DIM_AMOUNT = 0.001f

        fun newInstance(
            discussionId: Long,
            commentId: Long?,
            commentContent: String?,
        ): CommentCreateBottomSheet =
            CommentCreateBottomSheet().apply {
                arguments =
                    bundleOf(
                        CommentCreateViewModel.KEY_DISCUSSION_ID to discussionId,
                        CommentCreateViewModel.KEY_COMMENT_ID to commentId,
                        CommentCreateViewModel.KEY_COMMENT_CONTENT to commentContent,
                    )
            }
    }
}
