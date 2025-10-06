package com.team.todoktodok.presentation.xml.discussion.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.team.domain.model.Book
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivityCreateDiscussionRoomBinding
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.core.component.CommonDialog
import com.team.todoktodok.presentation.core.ext.getParcelableCompat
import com.team.todoktodok.presentation.core.ext.loadImage
import com.team.todoktodok.presentation.xml.book.SelectBookActivity
import com.team.todoktodok.presentation.xml.discussion.create.vm.CreateDiscussionRoomViewModel
import com.team.todoktodok.presentation.xml.discussion.create.vm.CreateDiscussionRoomViewModelFactory
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity

class CreateDiscussionRoomActivity : AppCompatActivity() {
    private val mode by lazy {
        intent.getParcelableCompat<SerializationCreateDiscussionRoomMode>(
            EXTRA_MODE,
        ) ?: throw IllegalStateException(MODE_NOT_EXIST)
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
        setupImeResize(binding)
        setupAutoScrollToFocused(binding)
    }

    private fun setupAutoScrollToFocused(binding: ActivityCreateDiscussionRoomBinding) {
        val scroll = binding.scrollContainer
        val title = binding.etDiscussionRoomTitle
        val opinion = binding.etDiscussionRoomOpinion

        val ensureVisible: (View) -> Unit = { target ->
            scroll.post {
                val y = target.bottom + (target.parent as View).top
                scroll.smoothScrollTo(
                    0,
                    maxOf(0, y - scroll.height + scroll.paddingBottom + target.height),
                )
            }
        }

        title.setOnFocusChangeListener { v, hasFocus -> if (hasFocus) ensureVisible(v) }
        opinion.setOnFocusChangeListener { v, hasFocus -> if (hasFocus) ensureVisible(v) }

        opinion.addTextChangedListener {
            if (opinion.hasFocus()) ensureVisible(opinion)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            hideKeyBoard(view = currentFocus ?: View(this))
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun setupImeResize(binding: ActivityCreateDiscussionRoomBinding) {
        val scroll = binding.scrollContainer

        ViewCompat.setOnApplyWindowInsetsListener(scroll) { v, insets ->
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, ime)
            insets
        }

        ViewCompat.setWindowInsetsAnimationCallback(
            scroll,
            object : WindowInsetsAnimationCompat.Callback(
                DISPATCH_MODE_CONTINUE_ON_SUBTREE,
            ) {
                override fun onProgress(
                    insets: WindowInsetsCompat,
                    runningAnimations: MutableList<WindowInsetsAnimationCompat>,
                ): WindowInsetsCompat {
                    val ime = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                    scroll.setPadding(
                        scroll.paddingLeft,
                        scroll.paddingTop,
                        scroll.paddingRight,
                        ime,
                    )
                    return insets
                }
            },
        )
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
        supportFragmentManager.setFragmentResultListener(
            CommonDialog.REQUEST_KEY_COMMON_DIALOG,
            this,
        ) { _, bundle ->
            val confirmed = bundle.getBoolean(CommonDialog.RESULT_KEY_COMMON_DIALOG)
            if (confirmed) {
                viewModel.saveDraft()
                return@setFragmentResultListener
            }
            viewModel.finish()
        }
        binding.apply {
            when (mode) {
                is SerializationCreateDiscussionRoomMode.Create -> settingCreateMode(binding)
                is SerializationCreateDiscussionRoomMode.Edit -> settingEditMode(binding)
                is SerializationCreateDiscussionRoomMode.Draft -> settingCreateMode(binding)
            }
            onBackPressedDispatcher.addCallback { navigateToSelectBook() }
            btnBack.setOnClickListener { navigateToSelectBook() }
            etDiscussionRoomTitle.apply {
                requestFocus()
                doAfterTextChanged { text: Editable? ->
                    viewModel.updateTitle(text.toString())
                }
            }
            etDiscussionRoomOpinion.doAfterTextChanged { text: Editable? ->
                viewModel.updateOpinion(text.toString())
            }
            etDiscussionRoomTitle.requestFocus()
        }
    }

    private fun settingCreateMode(binding: ActivityCreateDiscussionRoomBinding) {
        binding.apply {
            btnCreate.setOnClickListener {
                viewModel.createDiscussionRoom()
            }
            etDiscussionRoomTitle.setText(viewModel.uiState.value?.title)
            etDiscussionRoomOpinion.setText(viewModel.uiState.value?.opinion)
        }
    }

    private fun settingEditMode(binding: ActivityCreateDiscussionRoomBinding) {
        binding.apply {
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
        }
    }

    private fun navigateToSelectBook() {
        val intent = SelectBookActivity.Intent(this@CreateDiscussionRoomActivity)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
    }

    private fun setupUiState(binding: ActivityCreateDiscussionRoomBinding) {
        viewModel.uiState.observe(this@CreateDiscussionRoomActivity) { uiState: CreateDiscussionUiState ->
            observeBook(uiState.book, binding)
            observeIsCreate(uiState.isCreate, binding)
            observeTitle(uiState.title, binding)
            observeOpinion(uiState.opinion, binding)
        }
    }

    private fun observeTitle(
        title: String,
        binding: ActivityCreateDiscussionRoomBinding,
    ) {
        if (binding.etDiscussionRoomTitle.text.toString() != title) {
            binding.etDiscussionRoomTitle.setText(title)
        }
    }

    private fun observeOpinion(
        opinion: String,
        binding: ActivityCreateDiscussionRoomBinding,
    ) {
        if (binding.etDiscussionRoomOpinion.text.toString() != opinion) {
            binding.etDiscussionRoomOpinion.setText(opinion)
        }
    }

    private fun observeIsCreate(
        isCreate: Boolean,
        binding: ActivityCreateDiscussionRoomBinding,
    ) {
        decideBtnCreateColor(binding, isCreate)
        if (isCreate) {
            if (mode is SerializationCreateDiscussionRoomMode.Create) {
                binding.btnBack.setOnClickListener {
                    viewModel.checkIsPossibleToSave()
                }
            }
        }
        binding.btnCreate.setOnClickListener {
            viewModel.isPossibleToCreate()
        }
    }

    private fun decideBtnCreateColor(
        binding: ActivityCreateDiscussionRoomBinding,
        isCreate: Boolean,
    ) {
        binding.btnCreate.setTextColor(
            ContextCompat.getColor(
                this@CreateDiscussionRoomActivity,
                if (isCreate) R.color.green_1A else R.color.gray_76,
            ),
        )
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
                is CreateDiscussionUiEvent.NavigateToDiscussionDetail ->
                    navigateToDiscussionDetail(
                        event.discussionRoomId,
                        event.mode,
                    )

                is CreateDiscussionUiEvent.ShowToast ->
                    AlertSnackBar(binding.root, event.error.id).show()

                is CreateDiscussionUiEvent.SaveDraft -> {
                    if (event.possible) {
                        val dialog =
                            CommonDialog.newInstance(
                                getString(R.string.draft_message),
                                getString(R.string.draft),
                            )
                        dialog.show(supportFragmentManager, CommonDialog.TAG)
                        return@observe
                    }
                    val dialog =
                        CommonDialog.newInstance(
                            getString(R.string.no_exist_file),
                            getString(R.string.overload),
                        )
                    dialog.show(supportFragmentManager, CommonDialog.TAG)
                }

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
        mode: SerializationCreateDiscussionRoomMode,
    ) {
        val intent =
            DiscussionDetailActivity
                .Intent(
                    this@CreateDiscussionRoomActivity,
                    discussionRoomId,
                    mode,
                ).apply {
                    if (mode is SerializationCreateDiscussionRoomMode.Edit) {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    }
                }

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
        private const val EXTRA_DISCUSSION_ROOM_ID = "discussionRoomId"
        private const val EXTRA_MODE = "mode"
        private const val MODE_NOT_EXIST = "mode가 존재하지 않습니다."

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

                is SerializationCreateDiscussionRoomMode.Draft ->
                    intent.apply {
                        putExtra(EXTRA_MODE, mode)
                        putExtra(EXTRA_SELECTED_BOOK, mode.selectedBook)
                    }
            }
            return intent
        }
    }
}
