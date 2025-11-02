package com.team.todoktodok.presentation.xml.discussiondetail.commentcreate

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
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCommentCreateBottomSheetBinding
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.xml.discussiondetail.BottomSheetVisibilityListener
import com.team.todoktodok.presentation.xml.discussiondetail.commentcreate.vm.CommentCreateViewModel
import com.team.todoktodok.presentation.xml.discussiondetail.comments.vm.CommentsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CommentCreateBottomSheet : BottomSheetDialogFragment(R.layout.fragment_comment_create_bottom_sheet) {
    private val commentsViewModel: CommentsViewModel by viewModels(
        ownerProducer = { requireActivity() },
    )

    private val viewModel: CommentCreateViewModel by viewModels()

    private var visibilityListener: BottomSheetVisibilityListener? = null

    @Inject
    lateinit var messageConverter: ExceptionMessageConverter

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
        setupOnClickAddComment(binding)
        setupOnChangeComment(binding)
        setupObserve(binding)
        viewModel.initUiState()
    }

    override fun onDismiss(dialog: DialogInterface) {
        viewModel.saveContent()
        visibilityListener?.onBottomSheetDismissed()
        super.onDismiss(dialog)
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

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
            handleUiEvent(value, binding)
        }
        viewModel.commentText.observe(viewLifecycleOwner) { value ->
            binding.ivAddComment.isEnabled = value.isNotBlank()
        }
    }

    private fun handleUiEvent(
        uiEvent: CommentCreateUiEvent,
        binding: FragmentCommentCreateBottomSheetBinding,
    ) {
        when (uiEvent) {
            is CommentCreateUiEvent.SubmitComment -> {
                setFragmentResult(COMMENT_REQUEST_KEY, bundleOf(COMMENT_CREATED_RESULT_KEY to true))
                dismiss()
            }

            is CommentCreateUiEvent.InitState -> {
                initContentInputState(binding)
            }

            is CommentCreateUiEvent.OnCreateDismiss -> {
                commentsViewModel.updateCommentContent(uiEvent.content)
            }

            is CommentCreateUiEvent.ShowError ->
                AlertSnackBar(
                    binding.root,
                    messageConverter(uiEvent.exception),
                ).show()
        }
    }

    private fun initContentInputState(binding: FragmentCommentCreateBottomSheetBinding) {
        with(binding) {
            etTextCommentContent.requestFocus()
            val commentContent = viewModel.commentText.value ?: ""
            etTextCommentContent.setText(commentContent)
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etTextCommentContent, InputMethodManager.SHOW_IMPLICIT)
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
