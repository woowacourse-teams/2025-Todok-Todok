package com.example.todoktodok.presentation.view.discussion.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.todoktodok.App
import com.example.todoktodok.R
import com.example.todoktodok.databinding.ActivityDiscussionDetailBinding
import com.example.todoktodok.presentation.view.MainActivity
import com.example.todoktodok.presentation.view.discussion.detail.adapter.CommentAdapter
import com.example.todoktodok.presentation.view.discussion.detail.vm.DiscussionDetailViewModel
import com.example.todoktodok.presentation.view.discussion.detail.vm.DiscussionDetailViewModel.Companion.KEY_DISCUSSION_ID
import com.example.todoktodok.presentation.view.discussion.detail.vm.DiscussionDetailViewModelFactory
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
    private val binding: ActivityDiscussionDetailBinding by lazy {
        ActivityDiscussionDetailBinding.inflate(
            layoutInflater,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initAdapter()
        setContentView(binding.root)
        setupOnClickAddComment()
        setupOnClickNavigateUp()
        setupOnChangeComment()
        setupObserve()
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

    private fun setupOnClickAddComment() {
        with(binding) {
            ivAddComment.setOnClickListener {
                viewModel.onUiEvent(DiscussionDetailUiEvent.AddComment(etTextCommentContent.text.toString()))
                etTextCommentContent.text?.clear()
                etTextCommentContent.clearFocus()
            }
        }
    }

    private fun setupOnClickNavigateUp() {
        with(binding) {
            ivDiscussionDetailBack.setOnClickListener {
                viewModel.onUiEvent(DiscussionDetailUiEvent.NavigateUp)
            }
        }
    }

    private fun setupOnChangeComment() {
        with(binding) {
            etTextCommentContent.addTextChangedListener { editable ->
                viewModel.onCommentChanged(editable)
            }
        }
    }

    private fun setupObserve() {
        viewModel.discussion.observe(this) { value ->
            with(binding) {
                tvBookTitle.text = value.book.title
                tvDiscussionTitle.text = value.discussionTitle
                tvUserContent.text =
                    formatAuthorAndDate(value.writer.nickname.value, value.createAt)
                tvDiscussionNote.text = value.snap
                tvDiscussionOpinion.text = value.discussionOpinion
            }
        }
        viewModel.uiEvent.observe(this) { value ->
            handleEvent(value)
        }
        viewModel.comments.observe(this) { value ->
            adapter.submitList(value)
        }
        viewModel.commentText.observe(this) { value ->
            binding.ivAddComment.isEnabled = value.isNotBlank()
        }
    }

    private fun formatAuthorAndDate(
        author: String,
        date: LocalDateTime,
    ): String {
        val pattern = this.getString(R.string.date_format_pattern)
        val formatter = DateTimeFormatter.ofPattern(pattern, Locale.KOREA)
        val formattedDate = date.format(formatter)

        return this.getString(R.string.author_and_date_format, author, formattedDate)
    }

    private fun handleEvent(discussionDetailUiEvent: DiscussionDetailUiEvent) {
        when (discussionDetailUiEvent) {
            DiscussionDetailUiEvent.NavigateUp -> onBackPressedDispatcher.onBackPressed()
            is DiscussionDetailUiEvent.AddComment -> {
                viewModel.submitComment()
                viewModel.loadComments()
            }
        }
    }

    private fun setPopBackStack() {
        onBackPressedDispatcher.addCallback(this) {
            navigateUp()
            startActivity(intent)
            finish()
        }
    }

    private fun navigateUp() {
        val intent =
            Intent(this@DiscussionDetailActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
        startActivity(intent)
        finish()
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
