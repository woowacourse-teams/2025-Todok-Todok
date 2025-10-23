package com.team.todoktodok.presentation.compose.bookdiscussions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.team.todoktodok.presentation.compose.theme.TodoktodokTheme

class BookDiscussionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoktodokTheme {
                BookDetailNavHost(
                    intent.getLongExtra(BOOK_ID_KEY, -1L),
                    ::navigateToBack,
                )
            }
        }
    }

    fun navigateToBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    companion object {
        private const val BOOK_ID_KEY = "book_id_key"

        fun Intent(
            context: Context,
            bookId: Long,
        ): Intent =
            Intent(context, BookDiscussionsActivity::class.java).apply {
                putExtra(BOOK_ID_KEY, bookId)
            }
    }
}
