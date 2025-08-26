package com.team.todoktodok.presentation.view.profile.activated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team.domain.model.Book
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentActivatedBooksBinding
import com.team.todoktodok.presentation.view.profile.BaseProfileFragment
import com.team.todoktodok.presentation.view.profile.activated.adapter.BooksAdapter

class ActivatedBooksFragment : BaseProfileFragment(R.layout.fragment_activated_books) {
    private var _binding: FragmentActivatedBooksBinding? = null
    val binding get() = _binding!!

    val bookAdapterHandler =
        object : BooksAdapter.Handler {
            override fun onSelectBook(index: Int) {}
        }

    private val booksAdapter by lazy {
        BooksAdapter(bookAdapterHandler)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentActivatedBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        initView()
        setupUiState()
    }

    private fun initView() {
        binding.rvBooks.adapter = booksAdapter
    }

    private fun setupUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            val activatedBooks = value.activatedBooks
            if (activatedBooks.isEmpty()) {
                showEmptyResourceView()
            } else {
                showActivatedBooks(activatedBooks)
            }
        }
    }

    private fun showEmptyResourceView() {
        with(binding) {
            rvBooks.visibility = View.GONE
            viewResourceNotFound.show(
                getString(R.string.profile_not_has_activated_book_title),
                getString(R.string.profile_not_has_activated_book_subtitle),
                getString(R.string.profile_action_activated_book),
                { moveToDiscussions() },
            )
        }
    }

    private fun showActivatedBooks(activatedBooks: List<Book>) {
        binding.viewResourceNotFound.hide()
        binding.rvBooks.visibility = View.VISIBLE
        booksAdapter.submitList(activatedBooks)
    }

    private fun moveToDiscussions() {
        requireActivity().finish()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
