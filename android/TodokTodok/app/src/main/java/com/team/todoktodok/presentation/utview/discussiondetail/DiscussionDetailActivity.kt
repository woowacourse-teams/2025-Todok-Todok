package com.team.todoktodok.presentation.utview.discussiondetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.UtActivityDiscussionDetailBinding
import com.team.todoktodok.presentation.utview.discussiondetail.CommentCreateBottomSheet.Companion.TAG
import com.team.todoktodok.presentation.utview.discussiondetail.adapter.CommentAdapter
import com.team.todoktodok.presentation.utview.discussiondetail.vm.DiscussionDetailViewModel
import com.team.todoktodok.presentation.utview.discussiondetail.vm.DiscussionDetailViewModel.Companion.KEY_DISCUSSION_ID
import com.team.todoktodok.presentation.utview.discussiondetail.vm.DiscussionDetailViewModelFactory
import com.team.todoktodok.presentation.utview.discussions.DiscussionsActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DiscussionDetailActivity : AppCompatActivity() {
    private val adapter by lazy { CommentAdapter() }
    private val viewModel by viewModels<DiscussionDetailViewModel> {
        val repositoryModule = (application as App).container.repositoryModule
        DiscussionDetailViewModelFactory(
            repositoryModule.discussionRepository,
            repositoryModule.commentRepository,
        )
    }
    private val binding: UtActivityDiscussionDetailBinding by lazy {
        UtActivityDiscussionDetailBinding.inflate(
            layoutInflater,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initAdapter()
        setContentView(binding.root)
        setupOnClickNavigateUp()
        setupObserve()
        setPopBackStack()
        setupFragmentResultListener()
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

    private fun initAdapter() {
        binding.rvComments.adapter = adapter
    }

    private fun setupOnClickNavigateUp() {
        with(binding) {
            ivDiscussionDetailBack.setOnClickListener {
                viewModel.onBackPressed()
            }
            tvInputComment.setOnClickListener {
                viewModel.showBottomSheet()
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
        viewModel.comments.observe(this) { value ->
            adapter.submitList(value)
        }
    }

    private fun handleEvent(discussionDetailUiEvent: DiscussionDetailUiEvent) {
        when (discussionDetailUiEvent) {
            DiscussionDetailUiEvent.NavigateUp -> onBackPressedDispatcher.onBackPressed()
            is DiscussionDetailUiEvent.ShowCreateComment -> showCommentBottomSheet()
        }
    }

    private fun showCommentBottomSheet() {
        val bottomSheet =
            CommentCreateBottomSheet.newInstance(discussionId = viewModel.discussionId)

        bottomSheet.setVisibilityListener(
            object : BottomSheetVisibilityListener {
                override fun onBottomSheetShown() {
                    binding.tvInputComment.visibility = View.GONE
                }

                override fun onBottomSheetDismissed() {
                    binding.tvInputComment.visibility = View.VISIBLE
                }
            },
        )
        bottomSheet.show(supportFragmentManager, TAG)
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

    private fun setupFragmentResultListener() {
        supportFragmentManager.setFragmentResultListener(
            CommentCreateBottomSheet.COMMENT_REQUEST_KEY,
            this,
        ) { _, bundle ->
            val result = bundle.getBoolean(CommentCreateBottomSheet.COMMENT_CREATED_RESULT_KEY)
            if (result) {
                viewModel.commentsReload()
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
