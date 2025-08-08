package com.team.todoktodok.presentation.view.discussiondetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivityDiscussionDetailBinding
import com.team.todoktodok.databinding.MenuExternalDiscussionBinding
import com.team.todoktodok.databinding.MenuOwnedDiscussionBinding
import com.team.todoktodok.presentation.view.discussion.create.CreateDiscussionRoomActivity
import com.team.todoktodok.presentation.view.discussion.create.SerializationCreateDiscussionRoomMode
import com.team.todoktodok.presentation.view.discussion.create.SerializationCreateDiscussionRoomMode.*
import com.team.todoktodok.presentation.view.discussiondetail.comment.CommentBottomSheet
import com.team.todoktodok.presentation.view.discussiondetail.comments.CommentsFragment
import com.team.todoktodok.presentation.view.discussiondetail.vm.DiscussionDetailViewModel
import com.team.todoktodok.presentation.view.discussiondetail.vm.DiscussionDetailViewModel.Companion.KEY_DISCUSSION_ID
import com.team.todoktodok.presentation.view.discussiondetail.vm.DiscussionDetailViewModelFactory
import com.team.todoktodok.presentation.view.discussions.DiscussionsActivity
import com.team.todoktodok.presentation.view.profile.ProfileActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setContentView(binding.root)
        setupOnClick()
        setupObserve()
        setPopBackStack()
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
            tvUserNickname.setOnClickListener {
                viewModel.navigateToProfile()
            }
            setupLickClick()
        }
    }

    private fun navigateToProfile(memberId: Long) {
        val intent = ProfileActivity.Intent(this)
        startActivity(intent)
    }

    private fun setupLickClick() {
        with(binding) {
            ivLike.setOnClickListener {
                ivLike.isSelected = !ivLike.isSelected
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
            binding.tvEdit.setOnClickListener { viewModel.updateDiscussion() }
            binding.tvDelete.setOnClickListener { viewModel.deleteDiscussion() }
            createPopUpView(binding.root)
        } else {
            val binding = MenuExternalDiscussionBinding.inflate(layoutInflater)
            binding.tvReport.setOnClickListener { showReportDialog() }
            createPopUpView(binding.root)
        }

    private fun showReportDialog() {
        val dialog = ReportDiscussionDialog.newInstance()
        dialog.show(supportFragmentManager, ReportDiscussionDialog.TAG)
    }

    private fun createPopUpView(popupView: View) =
        PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true,
        )

    private fun setupObserve() {
        viewModel.discussion.observe(this) { value ->
            with(binding) {
                tvBookTitle.text = value.discussion.book.title
                tvDiscussionTitle.text = value.discussion.discussionTitle
                tvUserNickname.text = value.discussion.writer.nickname.value
                tvDiscussionCreateAt.text = value.discussion.createAt.formatDate()
                tvDiscussionOpinion.text = value.discussion.discussionOpinion
                ivLike.isSelected = value.discussion.isLikedByMe
                tvHeartCount.text = value.discussion.likeCount.toString()
                tvCommentCount.text = value.discussion.commentCount.toString()
            }
            setupPopUpDiscussionClick(value.isMyDiscussion)
        }
        viewModel.uiEvent.observe(this) { value ->
            handleEvent(value)
        }
    }

    private fun handleEvent(discussionDetailUiEvent: DiscussionDetailUiEvent) {
        when (discussionDetailUiEvent) {
            is DiscussionDetailUiEvent.ShowComments -> showComments(discussionDetailUiEvent.discussionId)
            is DiscussionDetailUiEvent.DeleteDiscussion -> navigateUp()
            is DiscussionDetailUiEvent.AlreadyReportDiscussion -> showToast(getString(R.string.all_already_report_discussion))
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
                val intent = ProfileActivity.Intent(this, discussionDetailUiEvent.userId)
                startActivity(intent)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setPopBackStack() {
        onBackPressedDispatcher.addCallback(this) {
            navigateUp()
        }
    }

    private fun navigateUp() {
        val intent =
            Intent(this@DiscussionDetailActivity, DiscussionsActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
        startActivity(intent)
        finish()
    }

    private fun showComments(discussionId: Long) {
        val bottomSheet = CommentBottomSheet.newInstance(discussionId)
        bottomSheet.show(supportFragmentManager, CommentsFragment.TAG)
    }

    private fun setUpDialogResultListener() {
        supportFragmentManager.setFragmentResultListener(
            ReportDiscussionDialog.RESULT_KEY_REPORT,
            this,
        ) { _, bundle ->
            val result = bundle.getBoolean(ReportDiscussionDialog.RESULT_KEY_REPORT)
            if (result) {
                viewModel.reportDiscussion()
                popupWindow?.dismiss()
            }
        }
    }

    private fun LocalDateTime.formatDate(): String {
        val pattern = this@DiscussionDetailActivity.getString(R.string.date_format_pattern)
        val formatter = DateTimeFormatter.ofPattern(pattern, Locale.KOREA)
        return format(formatter)
    }

    companion object {
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
