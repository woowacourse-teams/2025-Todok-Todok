package com.team.todoktodok.presentation.utview.discussion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivityCreateDiscussionRoomBinding
import com.team.todoktodok.presentation.utview.discussion.vm.CreateDiscussionRoomViewModel
import com.team.todoktodok.presentation.utview.discussion.vm.CreateDiscussionRoomViewModelFactory
import com.team.todoktodok.presentation.view.note.NoteFragment.Companion.REQUEST_KEY
import com.team.todoktodok.presentation.view.note.OwnedBooksBottomSheet
import com.team.todoktodok.presentation.view.note.OwnedBooksBottomSheet.Companion.RESULT_KEY
import com.team.todoktodok.presentation.view.serialization.SerializationBook

class CreateDiscussionRoomActivity : AppCompatActivity() {
    private val binding: ActivityCreateDiscussionRoomBinding by lazy {
        ActivityCreateDiscussionRoomBinding.inflate(layoutInflater)
    }

    private val viewModel: CreateDiscussionRoomViewModel by viewModels {
        val contanier = (application as App).container
        CreateDiscussionRoomViewModelFactory(
            contanier.repositoryModule.bookRepository,
            contanier.repositoryModule.discussionRepository,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpSystemBar()
        setup()

        supportFragmentManager
            .setFragmentResultListener(REQUEST_KEY, this) { _, result ->
                val selectedIndex = result.getInt(RESULT_KEY)
                viewModel.updateSelectedBook(selectedIndex)
            }

        with(binding) {
            etlSearchBook.setOnClickListener {
                viewModel.searchBooks()
            }

            etSearchBook.addTextChangedListener { text ->
                viewModel.updateSearchInput(text.toString())
            }

            etDiscussionTitle.addTextChangedListener { text ->
                viewModel.updateDiscussionTitle(text.toString())
            }

            etDiscussionContent.addTextChangedListener { text ->
                viewModel.updateDiscussionContent(text.toString())
            }

            ivBack.setOnClickListener {
                viewModel.moveToBack()
            }

            ivSearchBook.setOnClickListener {
                viewModel.searchBooks()
            }

            tvCreateDiscussionRoom.setOnClickListener {
                viewModel.saveDiscussionRoom()
            }
        }
    }

    private fun showOwnedBooksBottomSheet(books: List<SerializationBook>) {
        OwnedBooksBottomSheet
            .newInstance(books)
            .show(supportFragmentManager, OWNED_BOOKS_BOTTOM_SHEET_TAG)
    }

    private fun setUpSystemBar() {
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

    private fun setup() {
        viewModel.uiEvent.observe(this) { event ->
            when (event) {
                is CreateDiscussionRoomUiEvent.NavigateToBack -> {
                    finish()
                }

                is CreateDiscussionRoomUiEvent.ShowDialog -> {
                    val message =
                        when (event.errorType) {
                            SearchBookErrorType.NO_SELECTED_BOOK -> R.string.error_no_selected_book
                            SearchBookErrorType.BOOK_NOT_FOUND -> R.string.error_book_not_found
                            SearchBookErrorType.NO_SEARCH_RESULTS -> R.string.error_no_search_results
                            SearchBookErrorType.EMPTY_SEARCH_INPUT -> R.string.error_empty_search_input
                        }
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    viewModel.clearUiState()
                }

                is CreateDiscussionRoomUiEvent.ShowSearchedBooks -> {
                    showOwnedBooksBottomSheet(event.books)
                    viewModel.clearUiState()
                }

                is CreateDiscussionRoomUiEvent.CreateDiscussionRoom -> {
                    viewModel.saveDiscussionRoom()
                    viewModel.clearUiState()
                }

                is CreateDiscussionRoomUiEvent.ShowSelectedBook -> {
                    setUpSelectedBookText(event.book.title)
                }

                else -> null
            }
        }
    }

    private fun setUpSelectedBookText(title: String) {
        with(binding) {
            etSearchBook.setText(title)
            etSearchBook.setTextColor(
                ContextCompat.getColor(
                    this@CreateDiscussionRoomActivity,
                    R.color.black_18,
                ),
            )
            ivSearchBook.visibility = View.INVISIBLE
        }
    }

    companion object {
        fun Intent(context: Context): Intent = Intent(context, CreateDiscussionRoomActivity::class.java)

        private const val OWNED_BOOKS_BOTTOM_SHEET_TAG = "OwnedBooksBottomSheet"
    }
}
