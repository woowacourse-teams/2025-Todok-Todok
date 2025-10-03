package com.team.todoktodok.presentation.xml.discussion.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.snackbar.Snackbar
import com.team.domain.model.Book
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivityCreateDiscussionRoomBinding
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.core.ext.getParcelableCompat
import com.team.todoktodok.presentation.core.ext.loadImage
import com.team.todoktodok.presentation.xml.discussion.create.vm.CreateDiscussionRoomViewModel
import com.team.todoktodok.presentation.xml.discussion.create.vm.CreateDiscussionRoomViewModelFactory
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity

class CreateDiscussionRoomActivity : AppCompatActivity() {
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

        val binding = ActivityCreateDiscussionRoomBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initSystemBar(binding)
        initView(binding)
        setupUiState(binding)
        setupUiEvent(binding)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            hideKeyBoard(view = currentFocus ?: View(this))
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun initSystemBar(binding: ActivityCreateDiscussionRoomBinding) {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initView(binding: ActivityCreateDiscussionRoomBinding) {
        binding.apply {
            etDiscussionRoomTitle.apply {
                requestFocus()
                doAfterTextChanged { text: Editable? ->
                    viewModel.updateTitle(text.toString())
                }
            }
            etDiscussionRoomOpinion.doAfterTextChanged { text: Editable? ->
                viewModel.updateOpinion(text.toString())
            }
        }
    }

    private fun setupUiState(binding: ActivityCreateDiscussionRoomBinding) {
        viewModel.uiState.observe(this@CreateDiscussionRoomActivity) { uiState: CreateDiscussionUiState ->
            observeBook(uiState.book, binding)
            observeIsCreate(uiState.isCreate, binding)
        }
    }

    private fun observeIsCreate(
        isCreate: Boolean,
        binding: ActivityCreateDiscussionRoomBinding,
    ) {
        if (isCreate) {
            binding.apply {
                btnCreate.setTextColor(
                    ContextCompat.getColor(
                        this@CreateDiscussionRoomActivity,
                        R.color.green_1A,
                    ),
                )
            }
        }
    }

    private fun observeBook(
        book: Book?,
        binding: ActivityCreateDiscussionRoomBinding,
    ) {
        binding.tvBookTitle.text = book?.title
        binding.tvBookAuthor.text = book?.author
        binding.ivBookImage.loadImage(book?.image)
    }

    private fun setupUiEvent(binding: ActivityCreateDiscussionRoomBinding) {
        viewModel.uiEvent.observe(this@CreateDiscussionRoomActivity) { event ->
            when (event) {
                is CreateDiscussionUiEvent.NavigateToDiscussionDetail -> navigateToDiscussionDetail(
                    event.discussionRoomId
                )

                is CreateDiscussionUiEvent.ShowToast -> {
                    Snackbar
                        .make(binding.root, getString(event.error.id), Snackbar.LENGTH_LONG)
                        .show()
                }

                is CreateDiscussionUiEvent.SaveDraft -> viewModel.save()

                CreateDiscussionUiEvent.Finish -> finish()

                is CreateDiscussionUiEvent.ShowNetworkErrorMessage -> {
                    val messageConverter = ExceptionMessageConverter()
                    AlertSnackBar(binding.root, messageConverter(event.exception)).show()
                }
            }
        }
    }

    private fun navigateToDiscussionDetail(
        discussionRoomId: Long,
    ) {
        val intent = DiscussionDetailActivity.Intent(
            this@CreateDiscussionRoomActivity,
            discussionRoomId,
        )
        startActivity(intent)
        finish()
    }

    private fun hideKeyBoard(view: View) {
        val inputMethodManager: InputMethodManager =
            this.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        private const val EXTRA_SELECTED_BOOK = "discussionBook"
        private const val EXTRA_MODE = "mode"

        fun Intent(
            context: Context,
            mode: SerializationCreateDiscussionRoomMode,
        ): Intent {
            val intent = Intent(context, CreateDiscussionRoomActivity::class.java)

            if (mode is SerializationCreateDiscussionRoomMode.Create) {
                intent.apply {
                    putExtra(EXTRA_MODE, mode)
                    putExtra(EXTRA_SELECTED_BOOK, mode.selectedBook)
                }
            }
            return intent
        }
    }
}
