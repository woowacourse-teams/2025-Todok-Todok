package com.team.todoktodok.presentation.view.discussion.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
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
        initView()
        setupObserve()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            hideKeyBoard(view = currentFocus ?: View(this))
        }
        return super.dispatchTouchEvent(ev)
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

    private fun initView() {
        binding.apply {
            btnBack.setOnClickListener { finish() }
            btnEdit.setOnClickListener {
                navigateToSelectBook()
            }
            etDiscussionRoomTitle.doAfterTextChanged { text: Editable? ->
                viewModel.onTitleChanged(text.toString())
            }
            etDiscussionRoomOpinion.doAfterTextChanged { text: Editable? ->
                viewModel.onOpinionChanged(text.toString())
            }
            when (mode) {
                is SerializationCreateDiscussionRoomMode.Create -> settingCreateMode()
                is SerializationCreateDiscussionRoomMode.Edit -> settingEditMode()
            }
        }
    }

    private fun settingCreateMode() {
        binding.btnCreate.setOnClickListener {
            viewModel.createDiscussionRoom()
        }
    }

    private fun settingEditMode() {
        binding.apply {
            tvCreateDiscussionRoomTitle.text =
                getString(R.string.edit_discussion_room_title)
            btnEdit.visibility = View.INVISIBLE
            etDiscussionRoomTitle.setText(viewModel.title.value)
            etDiscussionRoomOpinion.setText(viewModel.opinion.value)
            btnCreate.setOnClickListener {
                viewModel.editDiscussionRoom()
            }
        }
    }

    private fun navigateToSelectBook() {
        val intent = SelectBookActivity.Intent(this@CreateDiscussionRoomActivity)
        startActivity(intent)
        finish()
    }

    private fun setupObserve() {
        observeBook()
        observeUiEvent()
        observeTitle()
        observeOpinion()
        observeIsCreate()
    }

    private fun observeIsCreate() {
        viewModel.isCreate.observe(this@CreateDiscussionRoomActivity) { isCreate: Boolean ->
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
    }

    private fun observeOpinion() {
        viewModel.opinion.observe(this@CreateDiscussionRoomActivity) { opinion ->
            if (binding.etDiscussionRoomOpinion.text.toString() != opinion) {
                binding.etDiscussionRoomOpinion.setText(opinion)
            }
        }
    }

    private fun observeTitle() {
        viewModel.title.observe(this@CreateDiscussionRoomActivity) { title ->
            if (binding.etDiscussionRoomTitle.text.toString() != title) {
                binding.etDiscussionRoomTitle.setText(title)
            }
        }
    }

    private fun observeBook() {
        viewModel.book.observe(this@CreateDiscussionRoomActivity) { book: Book ->
            binding.tvBookTitle.text = book.title
            binding.tvBookAuthor.text = book.author
            binding.ivBookImage.loadImage(book.image)
        }
    }

    private fun observeUiEvent() {
        viewModel.uiEvent.observe(this@CreateDiscussionRoomActivity) { event ->
            when (event) {
                is CreateDiscussionUiEvent.NavigateToDiscussionDetail ->
                    navigateToDiscussionDetail(
                        event,
                    )

                is CreateDiscussionUiEvent.ShowToast ->
                    Toast
                        .makeText(
                            this@CreateDiscussionRoomActivity,
                            event.error,
                            Toast.LENGTH_LONG,
                        ).show()
            }
        }
    }

    private fun navigateToDiscussionDetail(event: CreateDiscussionUiEvent.NavigateToDiscussionDetail) {
        val intent =
            DiscussionDetailActivity.Intent(
                this@CreateDiscussionRoomActivity,
                event.discussionRoomId,
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
