package com.team.todoktodok.presentation.view.book

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivitySelectBookBinding
import com.team.todoktodok.presentation.view.book.adapter.SearchBooksAdapter
import com.team.todoktodok.presentation.view.book.vm.SelectBookViewModel
import com.team.todoktodok.presentation.view.book.vm.SelectBookViewModelFactory

class SelectBookActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySelectBookBinding.inflate(layoutInflater) }

    private val adapter by lazy {
        SearchBooksAdapter { position -> viewModel.updateSelectedBook(position) }
    }

    private val viewModel by viewModels<SelectBookViewModel> {
        val repositoryModule = (application as App).container.repositoryModule
        SelectBookViewModelFactory(repositoryModule.bookRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.main)
        initSystemBar()
        initView()
        setupUiEvent()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            hideKeyBoard(view = currentFocus ?: View(this))
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun initSystemBar() {
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

    private fun initView() {
        binding.apply {
            rvSearchedBooks.adapter = adapter
            btnBack.setOnClickListener {
                viewModel.navigateToBack()
            }
            etlSearchKeyword.setEndIconOnClickListener {
                binding.etSearchKeyword.text = null
                viewModel.onDeleteKeywordButtonClicked()
            }
            etSearchKeyword.setOnEditorActionListener { view, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    val keyword = view.text.toString()
                    viewModel.onSearchAction(keyword)
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setupUiEvent() {
        viewModel.uiEvent.observe(this) { event ->
            when (event) {
                is SelectBookUiEvent.StartLoading -> {
                    // 로딩 중 스페너 넣기
                }

                is SelectBookUiEvent.FinishLoading -> {
                    // 로딩 중 스페너 치우기
                }

                is SelectBookUiEvent.NavigateToBack -> {
                    finish()
                }

                is SelectBookUiEvent.NavigateToCreateDiscussionRoom -> {
                    // 토론방 생성 페이지로 이동
                }

                is SelectBookUiEvent.HideKeyboard -> {
                    binding.etSearchKeyword.clearFocus()
                    hideKeyBoard(binding.etSearchKeyword)
                }

                is SelectBookUiEvent.RevealKeyboard -> {
                    binding.etSearchKeyword.requestFocus()
                }

                is SelectBookUiEvent.ShowDialog -> {
                    val message: String =
                        when (event.error) {
                            ErrorSelectBookType.ERROR_NO_SELECTED_BOOK -> {
                                getString(R.string.error_no_selected_book)
                            }
                            ErrorSelectBookType.ERROR_NETWORK -> {
                                getString(R.string.error_network)
                            }
                            ErrorSelectBookType.ERROR_EMPTY_KEYWORD -> {
                                getString(R.string.error_empty_keyword)
                            }
                            ErrorSelectBookType.ERROR_DELETE_KEYWORD -> {
                                getString(R.string.error_delete_keyword)
                            }
                        }
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }

                is SelectBookUiEvent.ShowSearchResult -> {
                    adapter.submitList(event.books.items)
                }
            }
        }
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
