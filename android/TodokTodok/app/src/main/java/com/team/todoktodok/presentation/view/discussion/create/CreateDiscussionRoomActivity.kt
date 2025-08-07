package com.team.todoktodok.presentation.view.discussion.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
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

class CreateDiscussionRoomActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCreateDiscussionRoomBinding.inflate(layoutInflater) }
    private val mode by lazy {
        intent.getParcelableCompat<SerializationCreateDiscussionRoomMode>(
            EXTRA_MODE,
        )
    }
    private val viewModel by viewModels<CreateDiscussionRoomViewModel> {
        val repositoryModule = (application as App).container.repositoryModule
        CreateDiscussionRoomViewModelFactory(
            mode,
            repositoryModule.bookRepository,
            repositoryModule.discussionRepository,
            repositoryModule.tokenRepository,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        initSystemBar()
        setupUi()

        binding.apply {
            btnBack.setOnClickListener { finish() }
            btnEdit.setOnClickListener {
                val intent = SelectBookActivity.Intent(this@CreateDiscussionRoomActivity)
                startActivity(intent)
                finish()
            }
            when (mode) {
                is SerializationCreateDiscussionRoomMode.Create -> {
                    binding.btnCreate.setOnClickListener {
                        viewModel.createDiscussionRoom()
                    }
                }

                is SerializationCreateDiscussionRoomMode.Edit -> {
                    binding.tvCreateDiscussionRoomTitle.text = "토론방 수정"
                    binding.btnEdit.visibility = View.INVISIBLE
                    btnCreate.isEnabled = true
                    binding.btnCreate.setOnClickListener {
                        viewModel.editDiscussionRoom()
                    }
                    binding.etDiscussionRoomTitle.setText(viewModel.title.value)
                    binding.etDiscussionRoomOpinion.setText(viewModel.opinion.value)

                    btnCreate.setTextColor(
                        ContextCompat.getColor(
                            this@CreateDiscussionRoomActivity,
                            R.color.green_1A,
                        ),
                    )
                }
            }
            etDiscussionRoomTitle.setOnEditorActionListener { view, actionId, event ->
                viewModel.onTitleChanged(view.text.toString())
                true
            }
            etDiscussionRoomOpinion.setOnEditorActionListener { view, actionId, event ->
                viewModel.onOpinionChanged(view.text.toString())
                true
            }
        }
    }

    private fun setupUi() {
        viewModel.book.observe(this) { book: Book ->
            binding.tvBookTitle.text = book.title
            binding.tvBookAuthor.text = book.author
            binding.ivBookImage.loadImage(book.image)
        }
        viewModel.uiEvent.observe(this) { event ->
            when (event) {
                is CreateDiscussionUiEvent.NavigateToDiscussionDetail -> {
                    val intent =
                        DiscussionDetailActivity.Intent(
                            this@CreateDiscussionRoomActivity,
                            event.discussionRoomId,
                        )
                    startActivity(intent)
                    finish()
                }
            }
        }
        viewModel.title.observe(this) { title ->
            binding.etDiscussionRoomTitle.setText(title)
        }
        viewModel.opinion.observe(this) { opinion ->
            binding.etDiscussionRoomOpinion.setText(opinion)
        }
//        viewModel.isCreate.observe(this) { isCreate: Boolean ->
//            binding.apply {
//                if (etDiscussionRoomTitle.text.isNullOrBlank() && etDiscussionRoomOpinion.text.isNullOrBlank()) {
//                    btnCreate.isEnabled = false
//                } else {
//                    btnCreate.isEnabled = true
//                    btnCreate.setTextColor(
//                        ContextCompat.getColor(
//                            this@CreateDiscussionRoomActivity,
//                            R.color.green_1A,
//                        ),
//                    )
//                }
//            }
//        }
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
        private const val EXTRA_MODE = "mode"

        fun Intent(
            context: Context,
            mode: SerializationCreateDiscussionRoomMode,
        ): Intent {
            val intent = Intent(context, CreateDiscussionRoomActivity::class.java)
            when (mode) {
                is SerializationCreateDiscussionRoomMode.Create ->
                    intent.apply {
                        putExtra(EXTRA_MODE, mode)
                        putExtra(EXTRA_SELECTED_BOOK, mode.selectedBook)
                    }

                is SerializationCreateDiscussionRoomMode.Edit ->
                    intent.apply {
                        putExtra(EXTRA_MODE, mode)
                        putExtra(EXTRA_DISCUSSION_ROOM_ID, mode.discussionRoomId)
                    }
            }
            return intent
        }
    }
}
