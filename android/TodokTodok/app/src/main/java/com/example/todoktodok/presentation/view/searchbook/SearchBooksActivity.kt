package com.example.todoktodok.presentation.view.searchbook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.domain.model.Book
import com.example.todoktodok.databinding.ActivitySearchBooksBinding
import com.example.todoktodok.presentation.view.searchbook.vm.SearchBooksViewModel

class SearchBooksActivity : AppCompatActivity() {
    private val viewModel: SearchBooksViewModel by viewModels()
    private val binding: ActivitySearchBooksBinding by lazy {
        ActivitySearchBooksBinding.inflate(
            layoutInflater,
        )
    }
    private val adapter by lazy {
        SearchBooksAdapter(
            OnSelectBookListener { id: Long ->
                finish()
            },
        )
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
        setContentView(binding.root)
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
        binding.rcBookSearchResult.adapter = adapter
    }

    private fun setupBooks() {
        viewModel.books.observe(this) { books: List<Book> ->
            adapter.submitList(books)
        }
    }

    private fun finishSearchBooks() {
        binding.ivBookSearchBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, SearchBooksActivity::class.java)
    }
}
