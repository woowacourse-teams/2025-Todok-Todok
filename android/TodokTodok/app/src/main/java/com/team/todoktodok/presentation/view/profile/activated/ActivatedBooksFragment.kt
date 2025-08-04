package com.team.todoktodok.presentation.view.profile.activated

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentActivatedBooksBinding
import com.team.todoktodok.presentation.view.profile.activated.adapter.BooksAdapter
import com.team.todoktodok.presentation.view.profile.activated.vm.ActivatedBooksViewModel
import com.team.todoktodok.presentation.view.profile.activated.vm.ActivatedBooksViewModelFactory

class ActivatedBooksFragment : Fragment(R.layout.fragment_activated_books) {
    private val viewModel: ActivatedBooksViewModel by viewModels {
        ActivatedBooksViewModelFactory()
    }
    private lateinit var booksAdapter: BooksAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        val binding = FragmentActivatedBooksBinding.bind(view)

        initView(binding)
        setUpUiState()
    }

    private fun initView(binding: FragmentActivatedBooksBinding) {
        booksAdapter = BooksAdapter(bookAdapterHandler)
        binding.rvBooks.adapter = booksAdapter
    }

    private fun setUpUiState() {
        viewModel.books.observe(viewLifecycleOwner) { value ->
            booksAdapter.submitList(value.items)
        }
    }

    val bookAdapterHandler =
        object : BooksAdapter.Handler {
            override fun onSelectBook(index: Int) {
            }
        }
}
