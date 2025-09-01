package com.team.todoktodok.presentation.view.book

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.team.domain.model.book.AladinBook
import com.team.domain.model.book.Keyword
import com.team.domain.model.book.length
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivitySelectBookBinding
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
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
        SelectBookViewModelFactory(
            repositoryModule.bookRepository,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySelectBookBinding.inflate(layoutInflater)
        val adapter = SearchBooksAdapter { position -> viewModel.updateSelectedBook(position) }

        setContentView(binding.main)
        initSystemBar(binding)
        initView(binding, adapter)
        setUpUiState(binding, adapter)
        setUpUiEvent(binding)
    }

    private fun initSystemBar(binding: ActivitySelectBookBinding) {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeBottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            v.setPadding(
                binding.main.paddingLeft,
                systemBars.top,
                binding.main.paddingRight,
                systemBars.bottom,
            )
            binding.rvSearchedBooks.setPadding(
                binding.rvSearchedBooks.paddingLeft,
                binding.rvSearchedBooks.paddingTop,
                binding.rvSearchedBooks.paddingRight,
                imeBottom,
            )
            binding.nsvEmptySearchResult.setPadding(
                binding.nsvEmptySearchResult.paddingLeft,
                binding.nsvEmptySearchResult.paddingTop,
                binding.nsvEmptySearchResult.paddingRight,
                imeBottom,
            )
            insets
        }
    }

    private fun initView(
        binding: ActivitySelectBookBinding,
        adapter: SearchBooksAdapter,
    ) {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            etSearchKeyword.requestFocus()
            etSearchKeyword.setOnEditorActionListener { view, actionId, _ ->
                handleSearchAction(view, actionId)
            }
            rvSearchedBooks.adapter = adapter
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

    private fun setUpUiState(
        binding: ActivitySelectBookBinding,
        adapter: SearchBooksAdapter,
    ) {
        viewModel.uiState.observe(this) { state: SelectBookUiState ->
            updateSearchedBooks(state, binding, adapter)
            updateKeyword(state.keyword, binding)
        }
    }

    private fun updateKeyword(
        keyword: Keyword?,
        binding: ActivitySelectBookBinding,
    ) {
        val currentText = binding.etSearchKeyword.text.toString()
        if (currentText != keyword?.value) {
            binding.etSearchKeyword.setText(keyword?.value)
            keyword?.let { binding.etSearchKeyword.setSelection(it.length) }
        }
    }

    private fun updateSearchedBooks(
        state: SelectBookUiState,
        binding: ActivitySelectBookBinding,
        adapter: SearchBooksAdapter,
    ) {
        when (state.status) {
            is SearchedBookStatus.Loading -> updateLoadingStatus(binding)
            is SearchedBookStatus.NotStarted -> updateNotStartStatus(binding)
            is SearchedBookStatus.NotFound -> updateNotFoundStatus(binding, state)
            is SearchedBookStatus.Success -> updateSuccessStatus(binding, state, adapter)
        }
    }

    private fun updateSuccessStatus(
        binding: ActivitySelectBookBinding,
        state: SelectBookUiState,
        adapter: SearchBooksAdapter,
    ) {
        hideKeyBoard(view = currentFocus ?: View(this))
        binding.progressBar.visibility = View.GONE
        binding.nsvEmptySearchResult.visibility = View.GONE
        binding.rvSearchedBooks.visibility = View.VISIBLE
        adapter.submitList(state.searchBookGroup)
    }

    private fun updateNotFoundStatus(
        binding: ActivitySelectBookBinding,
        state: SelectBookUiState,
    ) {
        binding.progressBar.visibility = View.GONE
        binding.nsvEmptySearchResult.visibility = View.VISIBLE
        binding.rvSearchedBooks.visibility = View.GONE
        binding.tvEmptySearchResultTitle.text =
            highlightKeyword(state.keyword)
        binding.tvEmptySearchResultSubTitle.setText(R.string.select_book_empty_search_result_content)
    }

    private fun updateNotStartStatus(binding: ActivitySelectBookBinding) {
        binding.progressBar.visibility = View.GONE
        binding.nsvEmptySearchResult.visibility = View.VISIBLE
        binding.rvSearchedBooks.visibility = View.GONE
    }

    private fun updateLoadingStatus(binding: ActivitySelectBookBinding) {
        binding.progressBar.visibility = View.VISIBLE
        binding.nsvEmptySearchResult.visibility = View.GONE
        binding.rvSearchedBooks.visibility = View.GONE
    }

    private fun setUpUiEvent(binding: ActivitySelectBookBinding) {
        viewModel.uiEvent.observe(this) { event ->
            when (event) {
                is SelectBookUiEvent.NavigateToCreateDiscussionRoom ->
                    navigateToCreateDiscussionRoom(event.book)

                is SelectBookUiEvent.ShowException -> {
                    val messageConverter = ExceptionMessageConverter()
                    AlertSnackBar(binding.root, messageConverter(event.exception)).show()
                }
            }
        }
    }

    private fun highlightKeyword(keyword: Keyword?): SpannableString {
        val title = getString(R.string.select_book_empty_search_result, keyword?.value)
        val spannableTitle = SpannableString(title)
        keyword?.let {
            val start = title.indexOf(keyword.value)
            val end = start + keyword.length
            spannableTitle.setSpan(
                ForegroundColorSpan(
                    getColor(R.color.green_1A),
                ),
                start,
                end,
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }
        return spannableTitle
    }

    private fun navigateToCreateDiscussionRoom(book: AladinBook) {
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

    companion object {
        fun Intent(context: Context): Intent = Intent(context, SelectBookActivity::class.java)
    }
}
