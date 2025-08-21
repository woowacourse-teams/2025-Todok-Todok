package com.team.todoktodok.presentation.view.discussiondetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivityDiscussionDetailBinding
import com.team.todoktodok.databinding.MenuExternalDiscussionBinding
import com.team.todoktodok.databinding.MenuOwnedDiscussionBinding
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.core.component.CommonDialog
import com.team.todoktodok.presentation.core.component.ReportDialog
import com.team.todoktodok.presentation.core.ext.extractSubtitle
import com.team.todoktodok.presentation.core.ext.loadCircleImage
import com.team.todoktodok.presentation.core.ext.loadImage
import com.team.todoktodok.presentation.core.ext.registerPositiveResultListener
import com.team.todoktodok.presentation.core.ext.registerReportResultListener
import com.team.todoktodok.presentation.core.ext.toRelativeString
import com.team.todoktodok.presentation.view.discussion.create.CreateDiscussionRoomActivity
import com.team.todoktodok.presentation.view.discussion.create.SerializationCreateDiscussionRoomMode.Edit
import com.team.todoktodok.presentation.view.discussiondetail.comment.CommentBottomSheet
import com.team.todoktodok.presentation.view.discussiondetail.comments.CommentsFragment
import com.team.todoktodok.presentation.view.discussiondetail.vm.DiscussionDetailViewModel
import com.team.todoktodok.presentation.view.discussiondetail.vm.DiscussionDetailViewModel.Companion.KEY_DISCUSSION_ID
import com.team.todoktodok.presentation.view.discussiondetail.vm.DiscussionDetailViewModelFactory
import com.team.todoktodok.presentation.view.profile.ProfileActivity

class DiscussionDetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DiscussionDetailViewModel> {
        val repositoryModule = (application as App).container.repositoryModule
        DiscussionDetailViewModelFactory(
            repositoryModule.discussionRepository,
            repositoryModule.tokenRepository,
        )
    }
    private val binding: ActivityDiscussionDetailBinding by lazy {
        ActivityDiscussionDetailBinding.inflate(
            layoutInflater,
        )
    }

    private var popupWindow: PopupWindow? = null

    private val messageConverter: ExceptionMessageConverter by lazy {
        ExceptionMessageConverter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setContentView(binding.root)
        setupOnClick()
        setupObserve()
        setUpDialogResultListener()
    }

    override fun onDestroy() {
        popupWindow?.dismiss()
        popupWindow = null
        super.onDestroy()
    }

    private fun initView() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                binding.root.paddingLeft,
                systemBars.top,
                binding.root.paddingRight,
                systemBars.bottom,
            )
            insets
        }
    }

    private fun setupOnClick() {
        with(binding) {
            ivDiscussionDetailBack.setOnClickListener {
                navigateUp()
            }
            ivComment.setOnClickListener {
                viewModel.showComments()
            }
            tvCommentCount.setOnClickListener {
                viewModel.showComments()
            }
            ivUserProfile.setOnClickListener {
                viewModel.navigateToProfile()
            }
            tvUserNickname.setOnClickListener {
                viewModel.navigateToProfile()
            }
            setupLikeClick()
        }
    }

    private fun setupLikeClick() {
        with(binding) {
            ivLike.setOnClickListener {
                viewModel.toggleLike()
            }
            tvLikeCount.setOnClickListener {
                viewModel.toggleLike()
            }
        }
    }

    private fun setupPopUpDiscussionClick(isMyDiscussion: Boolean) {
        binding.ivDiscussionOption.setOnClickListener {
            if (popupWindow == null) popupWindow = getPopUpView(isMyDiscussion)
            if (popupWindow?.isShowing == true) {
                popupWindow?.dismiss()
            } else {
                popupWindow?.showAsDropDown(it)
            }
        }
    }

    private fun getPopUpView(isMyDiscussion: Boolean): PopupWindow =
        if (isMyDiscussion) {
            val binding = MenuOwnedDiscussionBinding.inflate(layoutInflater)
            binding.tvEdit.setOnClickListener {
                viewModel.updateDiscussion()
                popupWindow?.dismiss()
            }
            binding.tvDelete.setOnClickListener {
                showDeleteDialog()
                popupWindow?.dismiss()
            }
            createPopUpView(binding.root)
        } else {
            val binding = MenuExternalDiscussionBinding.inflate(layoutInflater)
            binding.tvReport.setOnClickListener {
                showReportDialog()
                popupWindow?.dismiss()
            }
            createPopUpView(binding.root)
        }

    private fun showDeleteDialog() {
        val dialog =
            CommonDialog.newInstance(
                getString(R.string.all_discussion_delete_confirm),
                getString(R.string.all_delete_action),
                DISCUSSION_DELETE_DIALOG_REQUEST_KEY,
            )
        dialog.show(supportFragmentManager, CommonDialog.TAG)
    }

    private fun showReportDialog() {
        val dialog =
            ReportDialog.newInstance(
                DISCUSSION_REPORT_DIALOG_REQUEST_KEY,
            )
        dialog.show(supportFragmentManager, CommonDialog.TAG)
    }

    private fun createPopUpView(popupView: View) =
        PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true,
        )

    private fun setupObserve() {
        viewModel.uiState.observe(this) { value ->
            with(binding) {
                if (value.isLoading) {
                    progressBar.show()
                } else {
                    progressBar.hide()
                    val discussion = value.discussionItemUiState.discussion
                    tvBookTitle.text = discussion.book.title.extractSubtitle()
                    tvDiscussionTitle.text = discussion.discussionTitle
                    tvUserNickname.text = discussion.writer.nickname.value
                    ivUserProfile.loadCircleImage(discussion.writer.profileImage)
                    ivBookImage.loadImage(discussion.book.image)
                    tvDiscussionCreateAt.text =
                        discussion.createAt.toRelativeString(this@DiscussionDetailActivity)
                    tvDiscussionOpinion.text = discussion.discussionOpinion
                    ivLike.isSelected = discussion.isLikedByMe
                    tvLikeCount.text = discussion.likeCount.toString()
                    tvCommentCount.text = discussion.commentCount.toString()
                }
            }
            setupPopUpDiscussionClick(value.discussionItemUiState.isMyDiscussion)
        }
        viewModel.uiEvent.observe(this) { value ->
            handleEvent(value)
        }
    }

    private fun handleEvent(discussionDetailUiEvent: DiscussionDetailUiEvent) {
        when (discussionDetailUiEvent) {
            is DiscussionDetailUiEvent.ShowComments -> showComments(discussionDetailUiEvent.discussionId)
            is DiscussionDetailUiEvent.DeleteDiscussion -> navigateUp()
            is DiscussionDetailUiEvent.UpdateDiscussion -> {
                val discussionId = discussionDetailUiEvent.discussionId
                val intent =
                    CreateDiscussionRoomActivity.Intent(
                        this@DiscussionDetailActivity,
                        Edit(discussionId),
                    )
                startActivity(intent)
                finish()
            }

            is DiscussionDetailUiEvent.NavigateToProfile -> {
                navigateToProfile(memberId = discussionDetailUiEvent.userId)
            }

            is DiscussionDetailUiEvent.ShowErrorMessage ->
                AlertSnackBar(
                    binding.root,
                    messageConverter(
                        discussionDetailUiEvent.exceptions,
                    ),
                ).show()

            DiscussionDetailUiEvent.ShowReportDiscussionSuccessMessage ->
                showShortToast(R.string.all_report_discussion_success)
        }
    }

    private fun navigateUp() {
        onBackPressedDispatcher.onBackPressed()
    }

    private fun navigateToProfile(memberId: Long) {
        val intent = ProfileActivity.Intent(this, memberId)
        startActivity(intent)
    }

    private fun showComments(discussionId: Long) {
        val bottomSheet = CommentBottomSheet.newInstance(discussionId)
        bottomSheet.show(supportFragmentManager, CommentsFragment.TAG)
    }

    private fun setUpDialogResultListener() {
        supportFragmentManager.registerReportResultListener(
            this,
            DISCUSSION_REPORT_DIALOG_REQUEST_KEY,
            ReportDialog.RESULT_KEY_REPORT,
        ) { reason ->
            viewModel.reportDiscussion(reason)
        }

        supportFragmentManager.registerPositiveResultListener(
            this,
            DISCUSSION_DELETE_DIALOG_REQUEST_KEY,
            CommonDialog.RESULT_KEY_COMMON_DIALOG,
        ) {
            viewModel.deleteDiscussion()
        }
    }

    private fun showShortToast(
        @StringRes resId: Int,
    ) {
        Toast
            .makeText(this, resId, Toast.LENGTH_SHORT)
            .show()
    }

    companion object {
        private const val DISCUSSION_DELETE_DIALOG_REQUEST_KEY =
            "discussion_delete_dialog_request_key"
        private const val DISCUSSION_REPORT_DIALOG_REQUEST_KEY =
            "discussion_report_dialog_request_key"

        fun Intent(
            context: Context,
            discussionId: Long,
        ): Intent =
            Intent(context, DiscussionDetailActivity::class.java).putExtra(
                KEY_DISCUSSION_ID,
                discussionId,
            )
    }
}
