package com.team.todoktodok.presentation.view.discussion.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.team.domain.model.Book
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivityCreateDiscussionRoomBinding
import com.team.todoktodok.presentation.core.ext.getParcelableCompat
import com.team.todoktodok.presentation.core.ext.loadImage
import com.team.todoktodok.presentation.view.book.SelectBookActivity
import com.team.todoktodok.presentation.view.discussion.create.vm.CreateDiscussionRoomViewModel
import com.team.todoktodok.presentation.view.discussion.create.vm.CreateDiscussionRoomViewModelFactory
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.view.serialization.SerializationBook

class CreateDiscussionRoomActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCreateDiscussionRoomBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<CreateDiscussionRoomViewModel> {
        val repositoryModule = (application as App).container.repositoryModule
        CreateDiscussionRoomViewModelFactory(
            repositoryModule.bookRepository,
            repositoryModule.discussionRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initSystemBar()
        setupUi()
        val intent = intent
        val book = intent.getParcelableCompat<SerializationBook>(EXTRA_SELECTED_BOOK).toDomain()

        binding.apply {
            btnBack.setOnClickListener { finish() }
            btnEdit.setOnClickListener {
                val intent = SelectBookActivity.Intent(this@CreateDiscussionRoomActivity)
                startActivity(intent)
                finish()
            }
            btnCreate.setOnClickListener {
                viewModel.createDiscussionRoom()
            }

            etDiscussionRoomTitle.setOnEditorActionListener { view, actionId, event ->
                handleTextInput(view, actionId)
            }
            etDiscussionRoomOpinion.setOnEditorActionListener { view, actionId, event ->
                handleTextInput(view, actionId)
            }
            if (etDiscussionRoomTitle.text.isNullOrBlank() && etDiscussionRoomOpinion.text.isNullOrBlank()) {
                btnCreate.isEnabled = false
            } else {
                btnCreate.isEnabled = true
                btnCreate.setTextColor(
                    ContextCompat.getColor(
                        this@CreateDiscussionRoomActivity,
                        R.color.green_1A
                    )
                )
            }
        }
    }

    private fun setupUi() {
        viewModel.book.observe(this) { book: Book ->
            binding.tvBookTitle.text = book.title
            binding.tvBookAuthor.text = book.author
            binding.ivBookImage.loadImage(book.image)
        }
        viewModel.discussionRoomId.observe(this) { discussionRoomId: Long ->
            val intent =
                DiscussionDetailActivity.Intent(this@CreateDiscussionRoomActivity, discussionRoomId)
            startActivity(intent)
            finish()
        }



    }

    private fun handleTextInput(
        view: TextView,
        actionId: Int,
    ): Boolean =
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            when (view.id) {
                binding.etDiscussionRoomTitle.id -> viewModel.onTitleChanged(view.text.toString())
                binding.etDiscussionRoomOpinion.id -> viewModel.onOpinionChanged(view.text.toString())
            }
            true
        } else {
            false
        }

    private fun initSystemBar() {
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

    companion object {
        private const val EXTRA_SELECTED_BOOK = "discussionBook"
        private const val EXTRA_DISCUSSION_ROOM_ID = "discussionRoomId"

        fun Intent(
            context: Context,
            mode: CreateDiscussionRoomMode,
        ): Intent {
            val intent = Intent(context, CreateDiscussionRoomActivity::class.java)
            when (mode) {
                is CreateDiscussionRoomMode.Create ->
                    intent.putExtra(EXTRA_SELECTED_BOOK, mode.selectedBook)

                is CreateDiscussionRoomMode.Edit ->
                    intent.putExtra(EXTRA_DISCUSSION_ROOM_ID, mode.discussionRoomId)
            }
            return intent
        }
    }
}
