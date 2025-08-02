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
import com.team.todoktodok.presentation.view.discussiondetail.vm.DiscussionDetailViewModel
import com.team.todoktodok.presentation.view.discussiondetail.vm.DiscussionDetailViewModel.Companion.KEY_DISCUSSION_ID
import com.team.todoktodok.presentation.view.discussiondetail.vm.DiscussionDetailViewModelFactory
import com.team.todoktodok.presentation.view.discussions.DiscussionsActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DiscussionDetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DiscussionDetailViewModel> {
        val repositoryModule = (application as App).container.repositoryModule
        DiscussionDetailViewModelFactory(
            repositoryModule.discussionRepository,
            repositoryModule.commentRepository,
        )
    }
    private val binding: ActivityDiscussionDetailBinding by lazy {
        ActivityDiscussionDetailBinding.inflate(
            layoutInflater,
        )
    }

    private val popupWindow by lazy { setupPopUpView() }

    private fun setupPopUpView(): PopupWindow =
        if (viewModel.isMyDiscussion) {
            val binding = MenuOwnedDiscussionBinding.inflate(layoutInflater)
            binding.tvEdit.setOnClickListener { viewModel.updateDiscussion() }
            binding.tvDelete.setOnClickListener { viewModel.deleteDiscussion() }
            createPopUpView(binding.root)
        } else {
            val binding = MenuExternalDiscussionBinding.inflate(layoutInflater)
            binding.tvReport.setOnClickListener { viewModel.reportDiscussion() }
            createPopUpView(binding.root)
        }

    private fun createPopUpView(popupView: View) =
        PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true,
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setContentView(binding.root)
        setupOnClick()
        setupObserve()
        setPopBackStack()
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
                viewModel.onBackPressed()
            }
            ivComment.setOnClickListener {
                viewModel.showBottomSheet()
            }
            setupPopUpDiscussionClick()
            setupLickClick()
        }
    }

    private fun setupLickClick() {
        with(binding) {
            ivLike.setOnClickListener {
                ivLike.isSelected = !ivLike.isSelected
                viewModel.toggleLike()
            }
        }
    }

    private fun setupPopUpDiscussionClick() {
        binding.ivDiscussionOption.setOnClickListener {
            if (popupWindow.isShowing) {
                popupWindow.dismiss()
            } else {
                popupWindow.showAsDropDown(it)
            }
        }
    }

    private fun setupObserve() {
        viewModel.discussion.observe(this) { value ->
            with(binding) {
                tvBookTitle.text = value.book.title
                tvDiscussionTitle.text = value.discussionTitle
                tvUserNickname.text = value.writer.nickname.value
                tvDiscussionCreateAt.text = value.createAt.formatDate()
                tvDiscussionOpinion.text = value.discussionOpinion
            }
        }
        viewModel.uiEvent.observe(this) { value ->
            handleEvent(value)
        }
    }

    private fun handleEvent(discussionDetailUiEvent: DiscussionDetailUiEvent) {
        when (discussionDetailUiEvent) {
            DiscussionDetailUiEvent.NavigateUp -> onBackPressedDispatcher.onBackPressed()
            is DiscussionDetailUiEvent.ShowComments -> showToast("댓글 보이기")
            is DiscussionDetailUiEvent.ToggleLikeOnDiscussion -> showToast("좋아요 클릭")
            is DiscussionDetailUiEvent.DeleteDiscussion -> showToast("토론 삭제")
            is DiscussionDetailUiEvent.ReportDiscussion -> showToast("토론 신고")
            is DiscussionDetailUiEvent.UpdateDiscussion -> showToast("토론 수정")
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
