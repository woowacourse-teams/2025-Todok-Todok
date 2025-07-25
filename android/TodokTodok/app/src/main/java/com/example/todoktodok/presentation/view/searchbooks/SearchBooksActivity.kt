package com.example.todoktodok.presentation.view.searchbooks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoktodok.App
import com.example.todoktodok.databinding.ActivitySearchBooksBinding
import com.example.todoktodok.presentation.view.searchbooks.adapter.SearchBooksAdapter
import com.example.todoktodok.presentation.view.searchbooks.vm.SearchBooksViewModel
import com.example.todoktodok.presentation.view.searchbooks.vm.SearchBooksViewModelFactory

class SearchBooksActivity : AppCompatActivity() {
    private val viewModel: SearchBooksViewModel by viewModels {
        val container = (this.application as App).container
        SearchBooksViewModelFactory(container.repositoryModule.bookRepository)
    }

    private val binding: ActivitySearchBooksBinding by lazy {
        ActivitySearchBooksBinding.inflate(
            layoutInflater,
        )
    }

    private val adapter by lazy {
        SearchBooksAdapter { selectedPosition: Int ->
            viewModel.updateSelectedBook(selectedPosition)
            viewModel.saveSelectedBook()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSystemBar()
        initView()
        setupBooks()
        finishSearchBooks()
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
        setContentView(binding.root)
        binding.rcBookSearchResult.adapter = adapter
        binding.etBookSearchBar.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                viewModel.updateSearchInput(binding.etBookSearchBar.text.toString())
                viewModel.searchBooks()
                true
            } else {
                false
            }
        }
    }

    private fun setupBooks() {
        viewModel.uiEvent.observe(this) { event ->
            when (event) {
                is SearchBooksUiEvent.NavigateToLibrary -> {
                    val intent = Intent()
                    setResult(RESULT_OK, intent)
                    finish()
                }

                is SearchBooksUiEvent.ShowDialog -> {
                    Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show()
                    viewModel.clearUiState()
                }

                is SearchBooksUiEvent.ShowSearchedBooks -> {
                    adapter.submitList(event.books)
                    viewModel.clearUiState()
                }

                else -> Unit
            }
        }
    }

    private fun finishSearchBooks() {
        binding.ivBookSearchBack.setOnClickListener {
            viewModel.navigateToLibrary()
        }
    }

    companion object {
        fun Intent(context: Context): Intent = Intent(context, SearchBooksActivity::class.java)
    }
}
