package com.team.ui_xml.profile.activated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team.core.navigation.BookDiscussionsRoute
import com.team.domain.model.Book
import com.team.ui_xml.R
import com.team.ui_xml.databinding.FragmentActivatedBooksBinding
import com.team.ui_xml.profile.BaseProfileFragment
import com.team.ui_xml.profile.activated.adapter.BooksAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.team.core.R as coreR

@AndroidEntryPoint
class ActivatedBooksFragment : BaseProfileFragment(R.layout.fragment_activated_books) {
    @Inject
    lateinit var bookDiscussionsNavigation: BookDiscussionsRoute
    private var _binding: FragmentActivatedBooksBinding? = null
    val binding get() = _binding!!

    private lateinit var booksAdapter: BooksAdapter

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
        val bookAdapterHandler by lazy {
            object : BooksAdapter.Handler {
                override fun onSelectBook(bookId: Long) {
                    bookDiscussionsNavigation.navigateToBookDiscussions(requireActivity(), bookId)
                }
            }
        }

        booksAdapter = BooksAdapter(bookAdapterHandler)
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
                getString(coreR.string.all_not_has_activated_book_title),
            )
        }
    }

    private fun showActivatedBooks(activatedBooks: List<Book>) {
        binding.viewResourceNotFound.hide()
        binding.rvBooks.visibility = View.VISIBLE
        booksAdapter.submitList(activatedBooks)
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
