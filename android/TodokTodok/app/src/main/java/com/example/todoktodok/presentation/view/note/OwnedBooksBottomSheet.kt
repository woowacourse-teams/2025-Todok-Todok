package com.example.todoktodok.presentation.view.note

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OwnedBooksBottomSheet : BottomSheetDialogFragment(R.layout.owned_books_bottom_sheet) {
    private val booksAdapter: BooksAdapter = BooksAdapter()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = OwnedBooksBottomSheetBinding.bind(view)

        // TODO(리사이클러뷰 아이템 이벤트로 수정)
        binding.root.setOnClickListener {
            setFragmentResult(REQUEST_KEY, bundleOf("selectedBook" to "da"))
            dismiss()
        }
        initView(binding)
    }

    fun initView(binding: OwnedBooksBottomSheetBinding) {
        binding.rvBooks.adapter = booksAdapter

        val books = requireArguments().getParcelableArrayListCompat<SerializationBook>(KEY_BOOKS)
        booksAdapter.submitList(books.map { it.toDomain() })
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
