package com.example.todoktodok.presentation.view.searchbooks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.domain.model.Book
import com.example.todoktodok.App
import com.example.todoktodok.databinding.ActivitySearchBooksBinding
import com.example.todoktodok.presentation.view.note.vm.NoteViewModelFactory
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
            viewModel.saveBook(selectedPosition)
            finish()
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
        binding.etBookSearchBar.addTextChangedListener { text: Editable? ->
            viewModel.updateSearchInput(text.toString())
        }
    }

    private fun setupBooks() {
        viewModel.books.observe(this) { books: List<Book> ->
            adapter.submitList(books)
        }
        viewModel.searchInput.observe(this) {
            viewModel.searchBooks()
        }
    }

    private fun finishSearchBooks() {
        binding.ivBookSearchBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun Intent(context: Context): Intent = Intent(context, SearchBooksActivity::class.java)
    }
}
