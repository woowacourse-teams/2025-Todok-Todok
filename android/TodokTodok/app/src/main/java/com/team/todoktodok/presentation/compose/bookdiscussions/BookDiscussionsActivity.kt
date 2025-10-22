package com.team.todoktodok.presentation.compose.bookdiscussions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.team.todoktodok.presentation.compose.theme.TodoktodokTheme

class BookDiscussionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoktodokTheme {
                BookDetailNavHost(
                    intent.getLongExtra(BOOK_ID_KEY, -1L),
                    ::navigateToMain,
                )
            }
        }
    }

    fun navigateToMain() {
        onBackPressedDispatcher.onBackPressed()
    }

    companion object {
        private const val BOOK_ID_KEY = "book_id_key"

        fun Intent(
            context: Context,
            bookId: Long,
        ) = Intent(context, BookDiscussionsActivity::class.java).apply {
            putExtra(BOOK_ID_KEY, bookId)
        }
    }
}
