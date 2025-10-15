package com.team.todoktodok.presentation.compose.bookdiscussions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.team.todoktodok.presentation.compose.main.MainActivity
import com.team.todoktodok.presentation.compose.theme.TodoktodokTheme

class BookDiscussionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoktodokTheme {
                BookDetailNavHost(
                    intent.getLongExtra(BOOK_ID_KEY, -1L),
                    ::navigateToMain,
                    ::navigateToProfile,
                )
            }
        }
    }

    fun navigateToMain() {
        val intent = MainActivity.Intent(this)
        startActivity(intent)
    }

    fun navigateToProfile() {
        val intent = MainActivity.Intent(this)
        startActivity(intent)
    }

    companion object {
        private const val BOOK_ID_KEY = "book_id_key"

        fun intent(
            context: Context,
            bookId: Long,
        ) = Intent(context, BookDiscussionsActivity::class.java).apply {
            putExtra(BOOK_ID_KEY, bookId)
        }
    }
}
