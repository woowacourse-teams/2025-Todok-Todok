package com.team.todoktodok.presentation.view.discussions.search

import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentSearchDiscussionsBinding
import com.team.todoktodok.presentation.core.component.adapter.BaseDiscussionViewHolder
import com.team.todoktodok.presentation.core.component.adapter.DiscussionAdapter
import com.team.todoktodok.presentation.view.discussions.BaseDiscussionsFragment
import com.team.todoktodok.presentation.view.discussions.DiscussionUiState

class SearchDiscussionsFragment : BaseDiscussionsFragment(R.layout.fragment_search_discussions) {
    private val discussionAdapter: DiscussionAdapter by lazy {
        DiscussionAdapter(adapterHandler, BaseDiscussionViewHolder.ViewHolderType.QUERY_HIGHLIGHTING)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSearchDiscussionsBinding.bind(view)
        initView(binding)
        setUpUiState(binding)
    }

    private fun initView(binding: FragmentSearchDiscussionsBinding) {
        with(binding) {
            rvDiscussions.adapter = discussionAdapter
            rvDiscussions.setHasFixedSize(true)
        }
    }

    private fun setUpUiState(binding: FragmentSearchDiscussionsBinding) =
        with(binding) {
            viewModel.uiState.observe(viewLifecycleOwner) { value ->
                val searchKeyword = value.searchDiscussion.searchKeyword

                if (value.searchDiscussion.items.isEmpty()) {
                    displayNotHasSearchResultView(binding, searchKeyword)
                } else {
                    displaySearchResult(
                        binding,
                        value.searchDiscussion.items,
                    )
                }
            }
        }

    private fun displayNotHasSearchResultView(
        binding: FragmentSearchDiscussionsBinding,
        searchKeyword: String,
    ) = with(binding) {
        val highlightKeyword = highlightKeyword(searchKeyword)
        viewResourceNotFound.show(
            highlightKeyword,
            getString(R.string.discussion_no_search_subtitle),
        )
        rvDiscussions.visibility = View.GONE
    }

    private fun displaySearchResult(
        binding: FragmentSearchDiscussionsBinding,
        discussions: List<DiscussionUiState>,
    ) = with(binding) {
        viewResourceNotFound.hide()
        rvDiscussions.visibility = View.VISIBLE
        discussionAdapter.submitList(discussions)
    }

    private fun highlightKeyword(keyword: String): SpannableString {
        val title = getString(R.string.discussion_no_search_title, keyword)
        val spannableTitle = SpannableString(title)
        val start = title.indexOf(keyword)
        val end = start + keyword.length
        spannableTitle.setSpan(
            ForegroundColorSpan(
                requireContext().getColor(R.color.green_1A),
            ),
            start,
            end,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE,
        )
        return spannableTitle
    }

    private val adapterHandler =
        object : DiscussionAdapter.Handler {
            override fun onItemClick(index: Int) {
                val discussionId = discussionAdapter.currentList[index].discussionId
                moveToDiscussionDetail(discussionId)
            }
        }

    companion object {
        const val TAG = "SearchDiscussionsFragment"
    }
}
