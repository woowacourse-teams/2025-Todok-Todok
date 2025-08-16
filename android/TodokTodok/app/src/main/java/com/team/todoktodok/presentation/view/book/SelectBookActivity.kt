package com.team.todoktodok.presentation.view.book

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsAnimation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.team.domain.model.Book
import com.team.domain.model.Books
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivitySelectBookBinding
import com.team.todoktodok.presentation.view.book.adapter.SearchBooksAdapter
import com.team.todoktodok.presentation.view.book.vm.SelectBookViewModel
import com.team.todoktodok.presentation.view.book.vm.SelectBookViewModelFactory
import com.team.todoktodok.presentation.view.discussion.create.CreateDiscussionRoomActivity
import com.team.todoktodok.presentation.view.discussion.create.SerializationCreateDiscussionRoomMode
import com.team.todoktodok.presentation.view.serialization.SerializationBook
import com.team.todoktodok.presentation.view.serialization.toSerialization

class SelectBookActivity : AppCompatActivity() {
    private val viewModel by viewModels<SelectBookViewModel> {
        val repositoryModule = (application as App).container.repositoryModule
        SelectBookViewModelFactory(repositoryModule.bookRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySelectBookBinding.inflate(layoutInflater)
        val adapter = SearchBooksAdapter { position -> viewModel.updateSelectedBook(position) }

        setContentView(binding.main)
        initSystemBar(binding)
        initView(binding, adapter)
        setupUiState(binding, adapter)
        setupUiEvent(binding)
        liftViewWithIme(binding.nsvEmptySearchResult, R.dimen.space_120)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            hideKeyBoard(view = currentFocus ?: View(this))
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun initSystemBar(binding: ActivitySelectBookBinding) {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                binding.main.paddingLeft,
                systemBars.top,
                binding.main.paddingRight,
                systemBars.bottom,
            )
            insets
        }
    }

    private fun initView(
        binding: ActivitySelectBookBinding,
        adapter: SearchBooksAdapter,
    ) {
        binding.apply {
            etSearchKeyword.requestFocus()
            rvSearchedBooks.adapter = adapter
            btnBack.setOnClickListener {
                finish()
            }
            etlSearchKeyword.setEndIconOnClickListener {
                binding.etSearchKeyword.text = null
                viewModel.updateKeyword(binding.etSearchKeyword.text.toString())
            }
            etSearchKeyword.setOnEditorActionListener { view, actionId, _ ->
                handleSearchAction(view, actionId)
            }
        }
    }

    private fun handleSearchAction(
        view: TextView,
        actionId: Int,
    ): Boolean =
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            val keyword = view.text.toString()
            viewModel.searchWithCurrentKeyword(keyword)
            true
        } else {
            false
        }

    private fun setupUiState(
        binding: ActivitySelectBookBinding,
        adapter: SearchBooksAdapter,
    ) {
        viewModel.uiState.observe(this) { state: SelectBookUiState ->
            updateSearchedBooks(state.searchedBooks, binding, adapter)
            updateLoadingState(state, binding)
        }
    }

    private fun updateLoadingState(
        state: SelectBookUiState,
        binding: ActivitySelectBookBinding,
    ) {
        if (state.isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.nsvEmptySearchResult.visibility = View.GONE
            binding.rvSearchedBooks.visibility = View.GONE
            return
        }
        binding.progressBar.visibility = View.GONE
    }

    private fun updateSearchedBooks(
        searchedBooks: Books,
        binding: ActivitySelectBookBinding,
        adapter: SearchBooksAdapter,
    ) {
        if (searchedBooks.size == IS_EMPTY_SEARCH_RESULT) {
            binding.nsvEmptySearchResult.visibility = View.VISIBLE
            binding.rvSearchedBooks.visibility = View.GONE
            return
        }
        binding.nsvEmptySearchResult.visibility = View.GONE
        binding.rvSearchedBooks.visibility = View.VISIBLE
        adapter.submitList(searchedBooks.items)
    }

    private fun setupUiEvent(binding: ActivitySelectBookBinding) {
        viewModel.uiEvent.observe(this) { event ->
            when (event) {
                is SelectBookUiEvent.NavigateToCreateDiscussionRoom ->
                    navigateToCreateDiscussionRoom(event.book)

                is SelectBookUiEvent.HideKeyboard ->
                    hideKeyBoard(binding.etSearchKeyword)

                is SelectBookUiEvent.ShowErrorMessage -> {
                    Snackbar
                        .make(binding.root, getString(event.message.id), Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun navigateToCreateDiscussionRoom(book: Book) {
        val serializationBook: SerializationBook = book.toSerialization()
        val intent =
            CreateDiscussionRoomActivity.Intent(
                this,
                SerializationCreateDiscussionRoomMode.Create(serializationBook),
            )
        startActivity(intent)
        finish()
    }

    private fun hideKeyBoard(view: View) {
        val inputMethodManager: InputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun liftViewWithIme(
        view: View,
        size: Int,
    ) {
        view.setWindowInsetsAnimationCallback(
            object : WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
                override fun onProgress(
                    insets: WindowInsets,
                    runningAnimations: MutableList<WindowInsetsAnimation>,
                ): WindowInsets {
                    val imeBottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                    val maxLiftPx = resources.getDimensionPixelSize(size)
                    val lift = minOf(imeBottom, maxLiftPx)
                    view.translationY = (-lift).toFloat()
                    return insets
                }
            },
        )
    }

    companion object {
        private const val IS_EMPTY_SEARCH_RESULT: Int = 0

        fun Intent(context: Context): Intent = Intent(context, SelectBookActivity::class.java)
    }
}
