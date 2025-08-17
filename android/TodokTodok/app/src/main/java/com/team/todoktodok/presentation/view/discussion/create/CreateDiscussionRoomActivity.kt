package com.team.todoktodok.presentation.view.discussion.create

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
import com.team.todoktodok.presentation.core.ext.getParcelableCompat
import com.team.todoktodok.presentation.core.ext.loadImage
import com.team.todoktodok.presentation.view.book.SelectBookActivity
import com.team.todoktodok.presentation.view.discussion.create.vm.CreateDiscussionRoomViewModel
import com.team.todoktodok.presentation.view.discussion.create.vm.CreateDiscussionRoomViewModelFactory
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailActivity

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
            v.setPadding(
                binding.root.paddingLeft,
                systemBars.top,
                binding.root.paddingRight,
                systemBars.bottom,
            )
            insets
        }
    }

    private fun initView(binding: ActivityCreateDiscussionRoomBinding) {
        binding.apply {
            when (mode) {
                is SerializationCreateDiscussionRoomMode.Create -> settingCreateMode(binding)
                is SerializationCreateDiscussionRoomMode.Edit -> settingEditMode(binding)
            }
            btnCreate.isEnabled = false
            btnBack.setOnClickListener { finish() }
            btnEdit.setOnClickListener { navigateToSelectBook() }
            etDiscussionRoomTitle.doAfterTextChanged { text: Editable? ->
                viewModel.updateTitle(text.toString())
            }
            etDiscussionRoomOpinion.doAfterTextChanged { text: Editable? ->
                viewModel.updateOpinion(text.toString())
            }
            etDiscussionRoomTitle.requestFocus()
        }
    }

    private fun settingCreateMode(binding: ActivityCreateDiscussionRoomBinding) {
        binding.btnCreate.setOnClickListener {
            viewModel.createDiscussionRoom()
        }
    }

    private fun settingEditMode(binding: ActivityCreateDiscussionRoomBinding) {
        binding.apply {
            tvCreateDiscussionRoomTitle.text =
                getString(R.string.edit_discussion_room_title)
            btnEdit.visibility = View.INVISIBLE
            btnCreate.apply {
                text = getString(R.string.edit)
                setOnClickListener {
                    viewModel.editDiscussionRoom()
                }
            etDiscussionRoomTitle.setText(viewModel.uiState.value?.title)
            etDiscussionRoomOpinion.setText(viewModel.uiState.value?.opinion)
            btnCreate.setOnClickListener {
                viewModel.editDiscussionRoom()
            }
        }
    }}

    private fun navigateToSelectBook() {
        val intent = SelectBookActivity.Intent(this@CreateDiscussionRoomActivity)
        startActivity(intent)
        finish()
    }

    private fun setupUiState(binding: ActivityCreateDiscussionRoomBinding) {
        viewModel.uiState.observe(this@CreateDiscussionRoomActivity) { uiState: CreateDiscussionUiState ->
            observeBook(uiState.book, binding)
            observeTitle(uiState.title, binding)
            observeOpinion(uiState.opinion, binding)
            observeIsCreate(uiState.isCreate, binding)
        }
    }

    private fun observeIsCreate(isCreate: Boolean, binding: ActivityCreateDiscussionRoomBinding) {
        if (isCreate) {
            binding.btnCreate.isEnabled = true
            binding.btnCreate.setTextColor(
                ContextCompat.getColor(
                    this@CreateDiscussionRoomActivity,
                    R.color.green_1A,
                ),
            )
        }
    }

    private fun observeOpinion(opinion: String, binding: ActivityCreateDiscussionRoomBinding) {
        if (binding.etDiscussionRoomOpinion.text.toString() != opinion) {
            binding.etDiscussionRoomOpinion.setText(opinion)
        }
    }

    private fun observeTitle(title: String, binding: ActivityCreateDiscussionRoomBinding) {
        if (binding.etDiscussionRoomTitle.text.toString() != title) {
            binding.etDiscussionRoomTitle.setText(title)
        }
    }

    private fun observeBook(book: Book?, binding: ActivityCreateDiscussionRoomBinding) {
        binding.tvBookTitle.text = book?.title
        binding.tvBookAuthor.text = book?.author
        binding.ivBookImage.loadImage(book?.image)
    }

    private fun setupUiEvent(binding: ActivityCreateDiscussionRoomBinding) {
        viewModel.uiEvent.observe(this@CreateDiscussionRoomActivity) { event ->
            when (event) {
                is CreateDiscussionUiEvent.NavigateToDiscussionDetail ->
                    navigateToDiscussionDetail(event.discussionRoomId)

                is CreateDiscussionUiEvent.ShowToast -> {
                    Snackbar
                        .make(binding.root, getString(event.error.id), Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun navigateToDiscussionDetail(discussionRoomId: Long) {
        val intent =
            DiscussionDetailActivity.Intent(
                this@CreateDiscussionRoomActivity,
                discussionRoomId,
            )
        startActivity(intent)
        finish()
    }

    private fun hideKeyBoard(view: View) {
        val inputMethodManager: InputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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
