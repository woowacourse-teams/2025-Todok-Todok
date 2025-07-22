package com.example.todoktodok.presentation.view.note

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todoktodok.App
import com.example.todoktodok.R
import com.example.todoktodok.databinding.FragmentNoteBinding
import com.example.todoktodok.presentation.view.note.OwnedBooksBottomSheet.Companion.RESULT_KEY
import com.example.todoktodok.presentation.view.note.vm.NoteViewModel
import com.example.todoktodok.presentation.view.note.vm.NoteViewModelFactory
import com.example.todoktodok.presentation.view.serialization.SerializationBook

class NoteFragment : Fragment(R.layout.fragment_note) {
    private val viewModel: NoteViewModel by viewModels {
        val container = (requireActivity().application as App).container
        NoteViewModelFactory(
            container.repositoryModule.bookRepository,
            container.repositoryModule.noteRepository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        childFragmentManager
            .setFragmentResultListener(REQUEST_KEY, this) { _, result ->
                val selected = result.getString(RESULT_KEY)
            }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        val binding = FragmentNoteBinding.bind(view)
        initView(binding)
        setUpUiEvent()
    }

    private fun initView(binding: FragmentNoteBinding) {
        binding.etSearchBookLayout.setOnClickListener {
            viewModel.loadBooks()
        }
    }

    private fun setUpUiEvent() {
        viewModel.uiEvent.observe(viewLifecycleOwner) { value ->
            when (value) {
                is NoteUiEvent.ShowOwnBooks -> showOwnedBooksBottomSheet(value.books)
            }
        }
    }

    private fun showOwnedBooksBottomSheet(books: List<SerializationBook>) {
        OwnedBooksBottomSheet
            .newInstance(books)
            .show(childFragmentManager, OWNED_BOOKS_BOTTOM_SHEET_TAG)
    }

    companion object {
        private const val OWNED_BOOKS_BOTTOM_SHEET_TAG = "OwnedBooksBottomSheet"
        const val REQUEST_KEY = "selectedBook"
    }
}
