package com.example.todoktodok.presentation.view.searchbook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.domain.model.Book
import com.example.todoktodok.databinding.ActivitySearchBooksBinding

class SearchBooksActivity : AppCompatActivity() {
    private val books: List<Book> =
        listOf(
            Book(
                id = 0,
                title = "나무처럼 생각하기 : 나무처럼 자연의 질서 속에서 다시 살아가는 방법에 대하여",
                author = "조슈아 블로쉬",
                image = "",
            ),
            Book(
                id = 0,
                title = "나무처럼 생각하기 : 나무처럼 자연의 질서 속에서 다시 살아가는 방법에 대하여",
                author = "조슈아 블로쉬",
                image = "",
            ),
            Book(
                id = 0,
                title = "나무처럼 생각하기 : 나무처럼 자연의 질서 속에서 다시 살아가는 방법에 대하여",
                author = "조슈아 블로쉬",
                image = "",
            ),
            Book(
                id = 0,
                title = "나무처럼 생각하기 : 나무처럼 자연의 질서 속에서 다시 살아가는 방법에 대하여",
                author = "조슈아 블로쉬",
                image = "",
            ),
            Book(
                id = 0,
                title = "나무처럼 생각하기 : 나무처럼 자연의 질서 속에서 다시 살아가는 방법에 대하여",
                author = "조슈아 블로쉬",
                image = "",
            ),
        )

    private val binding: ActivitySearchBooksBinding by lazy {
        ActivitySearchBooksBinding.inflate(
            layoutInflater,
        )
    }

    private val adapter by lazy {
        SearchBooksAdapter(
            books,
            OnSelectBookListener { id: Long ->
                finish()
            },
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        binding.rcBookSearchResult.adapter = adapter
        finishSearchBooks()
    }

    private fun initView() {
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

    private fun finishSearchBooks() {
        binding.ivBookSearchBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun intent(context: Context): Intent = Intent(context, SearchBooksActivity::class.java)
    }
}
