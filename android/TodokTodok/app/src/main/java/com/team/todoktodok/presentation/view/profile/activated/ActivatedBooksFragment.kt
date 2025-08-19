package com.team.todoktodok.presentation.view.profile.activated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentActivatedBooksBinding
import com.team.todoktodok.presentation.core.ext.getParcelableArrayListCompat
import com.team.todoktodok.presentation.view.profile.activated.adapter.BooksAdapter
import com.team.todoktodok.presentation.view.serialization.SerializationBook

class ActivatedBooksFragment : Fragment() {
    private var _binding: FragmentActivatedBooksBinding? = null
    val binding get() = _binding!!

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
        initView(binding)
    }

    private fun initView(binding: FragmentActivatedBooksBinding) {
        val activatedBooks =
            arguments?.getParcelableArrayListCompat<SerializationBook>(ARG_ACTIVATED_BOOKS)
                ?: emptyList()

        if (activatedBooks.isEmpty()) {
            showEmptyResourceView()
        } else {
            showActivatedBooks(activatedBooks)
        }
    }

    private fun showEmptyResourceView() {
        with(binding) {
            rvBooks.visibility = View.GONE
            viewResourceNotFound.show(
                getString(R.string.profile_not_has_activated_book_title),
                getString(R.string.profile_not_has_activated_book_subtitle),
                getString(R.string.profile_action_participated_discussion),
                { moveToDiscussions() },
            )
        }
    }

    private fun moveToDiscussions() {
        requireActivity().finish()
    }

    private fun showActivatedBooks(activatedBooks: List<SerializationBook>) {
        val booksAdapter = BooksAdapter(bookAdapterHandler)
        binding.rvBooks.adapter = booksAdapter
        val books = activatedBooks.map { book -> book.toDomain() }
        booksAdapter.submitList(books)
    }

    val bookAdapterHandler =
        object : BooksAdapter.Handler {
            override fun onSelectBook(index: Int) {}
        }

    companion object {
        private const val ARG_ACTIVATED_BOOKS = "ACTIVATED_BOOKS"

        fun newInstance(books: List<SerializationBook>) =
            ActivatedBooksFragment().apply {
                arguments = bundleOf(ARG_ACTIVATED_BOOKS to (books))
            }
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
