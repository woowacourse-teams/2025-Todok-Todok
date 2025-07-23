package com.example.todoktodok.presentation.view.discussion.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoktodok.App
import com.example.todoktodok.R
import com.example.todoktodok.databinding.ActivityDiscussionRoomDetailBinding
import com.example.todoktodok.presentation.view.discussion.detail.vm.DiscussionRoomDetailViewModel
import com.example.todoktodok.presentation.view.discussion.detail.vm.DiscussionRoomDetailViewModel.Companion.KEY_DISCUSSION_ID
import com.example.todoktodok.presentation.view.discussion.detail.vm.DiscussionRoomDetailViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DiscussionRoomDetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DiscussionRoomDetailViewModel> {
        val repositoryModule = (application as App).container.repositoryModule
        DiscussionRoomDetailViewModelFactory(
            repositoryModule.discussionRoomRepository,
            repositoryModule.commentRepository,
        )
    }
    private val binding: ActivityDiscussionRoomDetailBinding by lazy {
        ActivityDiscussionRoomDetailBinding.inflate(
            layoutInflater,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setContentView(binding.root)
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

    private fun setupObserve() {
        viewModel.discussionRoom.observe(this) { value ->
            with(binding) {
                tvBookTitle.text = value.book.title
                tvDiscussionRoomTitle.text = value.discussionTitle
                tvUserContent.text =
                    formatAuthorAndDate(value.writer.nickname.value, value.createAt)
                tvDiscussionRoomNote.text = value.snap
                tvDiscussionRoomOpinion.text = value.discussionOpinion
            }
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

    companion object {
        fun Intent(
            context: Context,
            discussionId: Long,
        ): Intent =
            Intent(context, DiscussionRoomDetailActivity::class.java).putExtra(
                KEY_DISCUSSION_ID,
                discussionId,
            )
    }
}
