package com.team.todoktodok.presentation.view.profile.activated

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.domain.model.member.MemberId.Companion.INVALID_MEMBER_ID
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentActivatedBooksBinding
import com.team.todoktodok.presentation.view.profile.ProfileActivity.Companion.ARG_MEMBER_ID
import com.team.todoktodok.presentation.view.profile.ProfileActivity.Companion.MEMBER_ID_NOT_FOUND
import com.team.todoktodok.presentation.view.profile.activated.adapter.BooksAdapter
import com.team.todoktodok.presentation.view.profile.activated.vm.ActivatedBooksViewModel
import com.team.todoktodok.presentation.view.profile.activated.vm.ActivatedBooksViewModelFactory

class ActivatedBooksFragment : Fragment(R.layout.fragment_activated_books) {
    private val viewModel: ActivatedBooksViewModel by viewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        ActivatedBooksViewModelFactory(repositoryModule.memberRepository)
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
        val memberId = arguments?.getLong(ARG_MEMBER_ID, INVALID_MEMBER_ID)
        requireNotNull(memberId) { MEMBER_ID_NOT_FOUND }
        viewModel.loadActivatedBooks(memberId)

        booksAdapter = BooksAdapter(bookAdapterHandler)
        binding.rvBooks.adapter = booksAdapter
    }

    private fun setUpUiState() {
        viewModel.books.observe(viewLifecycleOwner) { value ->
            booksAdapter.submitList(value)
        }
    }

    val bookAdapterHandler =
        object : BooksAdapter.Handler {
            override fun onSelectBook(index: Int) {}
        }

    companion object {
        fun newInstance(memberId: Long?) =
            ActivatedBooksFragment().apply {
                memberId?.let { arguments = bundleOf(ARG_MEMBER_ID to it) }
            }
    }
}
