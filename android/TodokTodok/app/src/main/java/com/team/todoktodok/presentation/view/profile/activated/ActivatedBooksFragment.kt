package com.team.todoktodok.presentation.view.profile.activated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.domain.model.member.MemberId.Companion.DEFAULT_MEMBER_ID
import com.team.todoktodok.App
import com.team.todoktodok.databinding.FragmentActivatedBooksBinding
import com.team.todoktodok.presentation.view.profile.ProfileActivity.Companion.ARG_MEMBER_ID
import com.team.todoktodok.presentation.view.profile.ProfileActivity.Companion.MEMBER_ID_NOT_FOUND
import com.team.todoktodok.presentation.view.profile.activated.adapter.BooksAdapter
import com.team.todoktodok.presentation.view.profile.activated.vm.ActivatedBooksViewModel
import com.team.todoktodok.presentation.view.profile.activated.vm.ActivatedBooksViewModelFactory

class ActivatedBooksFragment : Fragment() {
    private var _binding: FragmentActivatedBooksBinding? = null
    val binding get() = _binding!!

    private val viewModel: ActivatedBooksViewModel by viewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        ActivatedBooksViewModelFactory(repositoryModule.memberRepository)
    }
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
        initView(binding)
        setUpUiState()
    }

    private fun initView(binding: FragmentActivatedBooksBinding) {
        val memberId = arguments?.getLong(ARG_MEMBER_ID, DEFAULT_MEMBER_ID)
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

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
