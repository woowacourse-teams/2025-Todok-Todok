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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.team.domain.model.Book
import com.team.domain.model.book.SearchedBook
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivityCreateDiscussionRoomBinding
import com.team.todoktodok.presentation.compose.main.MainActivity
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.core.component.CommonDialog
import com.team.todoktodok.presentation.core.ext.loadImage
import com.team.todoktodok.presentation.xml.book.SelectBookActivity
import com.team.todoktodok.presentation.xml.discussion.create.DraftDialog.Companion.KEY_REQUEST_DRAFT
import com.team.todoktodok.presentation.xml.discussion.create.DraftDialog.Companion.KEY_RESULT_DRAFT
import com.team.todoktodok.presentation.xml.discussion.create.vm.CreateDiscussionRoomViewModel
import com.team.todoktodok.presentation.xml.discussion.create.vm.CreateDiscussionRoomViewModel.Companion.EXTRA_MODE
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.xml.draft.DraftsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateDiscussionRoomActivity : AppCompatActivity() {
    private val launcher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val selectedValue =
                    data?.getLongExtra("selected_draft", -1L) ?: error("잠시 오류가 있습니다")
                viewModel.getDraft(selectedValue)
            }
        }
    private val viewModel by viewModels<CreateDiscussionRoomViewModel>()

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
        binding.apply {
            btnSave.setOnClickListener { showDraftsListDialog() }
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
            when (viewModel.mode) {
                is SerializationCreateDiscussionRoomMode.Create -> settingCreateMode(binding)
                is SerializationCreateDiscussionRoomMode.Edit -> settingEditMode(binding)
                is SerializationCreateDiscussionRoomMode.Draft -> settingCreateMode(binding)
            }
        }
    }

    private fun settingCreateMode(binding: ActivityCreateDiscussionRoomBinding) {
        binding.apply {
            etDiscussionRoomTitle.setText(viewModel.uiState.value?.title)
            etDiscussionRoomOpinion.setText(viewModel.uiState.value?.opinion)
        }
    }

    private fun settingEditMode(binding: ActivityCreateDiscussionRoomBinding) {
        binding.apply {
            etDiscussionRoomTitle.setText(viewModel.uiState.value?.title)
            etDiscussionRoomOpinion.setText(viewModel.uiState.value?.opinion)
            btnCreate.text = getString(R.string.edit)
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun navigateToSelectBook() {
        val intent = SelectBookActivity.Intent(this@CreateDiscussionRoomActivity)
        startActivity(intent)
        finish()
    }

    private fun setupUiState(binding: ActivityCreateDiscussionRoomBinding) {
        viewModel.uiState.observe(this@CreateDiscussionRoomActivity) { uiState: CreateDiscussionUiState ->
            observeBook(uiState.editBook, uiState.draftBook, uiState.book, binding)
            observeIsCreate(uiState.isCreate, binding)
            observeTitle(uiState.title, binding)
            observeOpinion(uiState.opinion, binding)
            observeIsDraft(uiState.isDraft, binding)
            observeDraftCount(uiState.draftDiscussionCount, binding)
        }
    }

    fun observeDraftCount(
        draftCount: Int,
        binding: ActivityCreateDiscussionRoomBinding,
    ) {
        if (draftCount == 0) {
            binding.btnSave.visibility = View.GONE
        } else {
            binding.btnSave.visibility = View.VISIBLE
        }
        binding.btnSave.text = this.getString(R.string.create_discussion_temp_save, draftCount)
    }

    fun observeIsDraft(
        isDraft: Boolean,
        binding: ActivityCreateDiscussionRoomBinding,
    ) {
        if (viewModel.mode !is SerializationCreateDiscussionRoomMode.Create) return

        if (isDraft) {
            binding.btnBack.setOnClickListener { showDraftDialog() }
            onBackPressedDispatcher.addCallback { showDraftDialog() }
        } else {
            onBackPressedDispatcher.addCallback { navigateToSelectBook() }
            binding.btnBack.setOnClickListener { navigateToSelectBook() }
        }
    }

    private fun showDraftDialog() {
        DraftDialog.newInstance().show(supportFragmentManager, DraftDialog.TAG)
        supportFragmentManager.setFragmentResultListener(KEY_REQUEST_DRAFT, this) { _, bundle ->
            val isSave = bundle.getBoolean(KEY_RESULT_DRAFT)
            if (isSave) viewModel.saveDraft()
            navigateToMain()
        }
    }

    private fun showDraftsListDialog() {
        val intent = DraftsActivity.Intent(this)
        launcher.launch(intent)
    }

    private fun navigateToMain() {
        val intent = MainActivity.Intent(this)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
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
            if (viewModel.mode is SerializationCreateDiscussionRoomMode.Create) {
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
        editBook: Book?,
        draftBook: SearchedBook?,
        book: SearchedBook?,
        binding: ActivityCreateDiscussionRoomBinding,
    ) {
        binding.tvBookTitle.text = book?.mainTitle ?: draftBook?.title ?: editBook?.title
        binding.tvBookAuthor.text = book?.author ?: draftBook?.author ?: editBook?.author
        binding.ivBookImage.loadImage(book?.image ?: draftBook?.image ?: editBook?.image)
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
