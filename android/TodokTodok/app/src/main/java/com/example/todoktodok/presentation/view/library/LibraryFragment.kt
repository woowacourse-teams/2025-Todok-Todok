package com.example.todoktodok.presentation.view.library

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todoktodok.App
import com.example.todoktodok.R
import com.example.todoktodok.databinding.FragmentLibraryBinding
import com.example.todoktodok.presentation.view.library.vm.LibraryViewModel
import com.example.todoktodok.presentation.view.library.vm.LibraryViewModelFactory
import com.example.todoktodok.presentation.view.searchbooks.SearchBooksActivity
import kotlin.getValue

class LibraryFragment : Fragment(R.layout.fragment_library) {
    private val viewModel: LibraryViewModel by viewModels {
        val container = (requireActivity().application as App).container
        LibraryViewModelFactory(container.repositoryModule.bookRepository)
    }
    private val booksAdapter: BooksAdapter by lazy { BooksAdapter() }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLibraryBinding.bind(view)

        binding.rv.adapter = booksAdapter
        binding.ivLibraryNavigation.setOnClickListener {
            val intent = SearchBooksActivity.Intent(requireActivity())
            startActivity.launch(intent)
        }
        viewModel.books.observe(viewLifecycleOwner) { value ->
            booksAdapter.submitList(value)
        }
    }

    private val startActivity =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.loadBooks()
            }
        }
}