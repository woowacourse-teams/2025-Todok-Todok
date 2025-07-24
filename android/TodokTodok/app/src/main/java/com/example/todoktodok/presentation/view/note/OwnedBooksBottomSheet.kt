package com.example.todoktodok.presentation.view.note

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.todoktodok.R
import com.example.todoktodok.databinding.OwnedBooksBottomSheetBinding
import com.example.todoktodok.presentation.core.ext.getParcelableArrayListCompat
import com.example.todoktodok.presentation.view.library.BooksAdapter
import com.example.todoktodok.presentation.view.note.NoteFragment.Companion.REQUEST_KEY
import com.example.todoktodok.presentation.view.serialization.SerializationBook
import com.example.todoktodok.state.BookState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OwnedBooksBottomSheet : BottomSheetDialogFragment(R.layout.owned_books_bottom_sheet) {
    private val bookHandler =
        object : BooksAdapter.Handler {
            override fun onSelectBook(position: Int) {
                setFragmentResult(REQUEST_KEY, bundleOf(RESULT_KEY to position))
                dismiss()
            }
        }

    private val booksAdapter: BooksAdapter = BooksAdapter(bookHandler)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        setOnShowDialogListener(dialog)
        return dialog
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = OwnedBooksBottomSheetBinding.bind(view)
        initView(binding)
    }

    private fun initView(binding: OwnedBooksBottomSheetBinding) {
        binding.rvBooks.adapter = booksAdapter

        val books = requireArguments().getParcelableArrayListCompat<SerializationBook>(KEY_BOOKS)
        booksAdapter.submitList(
            books.map { book: SerializationBook ->
                BookState(
                    book.id,
                    book.title,
                    book.author,
                    book.image,
                )
            },
        )
    }

    private fun setOnShowDialogListener(dialog: BottomSheetDialog) {
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                val halfScreenHeight = getHalfScreenHeight()
                setUpBehavior(behavior, halfScreenHeight)

                bottomSheet.layoutParams.height = halfScreenHeight
            }
        }
    }

    private fun getHalfScreenHeight(): Int {
        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val halfScreenHeight = screenHeight / 2

        return halfScreenHeight
    }

    private fun setUpBehavior(
        behavior: BottomSheetBehavior<View>,
        halfScreenHeight: Int,
    ) {
        behavior.peekHeight = halfScreenHeight
    }

    companion object {
        fun newInstance(books: List<SerializationBook>): OwnedBooksBottomSheet =
            OwnedBooksBottomSheet().apply {
                arguments =
                    Bundle().apply {
                        putParcelableArrayList(KEY_BOOKS, ArrayList(books))
                    }
            }

        private const val KEY_BOOKS = "books"
        const val RESULT_KEY = "selectedBook"
    }
}
