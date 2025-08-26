package com.team.todoktodok.presentation.view.profile.created

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCreatedDiscussionsRoomBinding
import com.team.todoktodok.presentation.core.component.adapter.BaseDiscussionViewHolder
import com.team.todoktodok.presentation.core.component.adapter.DiscussionAdapter
import com.team.todoktodok.presentation.core.ext.getParcelableArrayListCompat
import com.team.todoktodok.presentation.view.book.SelectBookActivity
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.view.discussions.DiscussionUiState
import com.team.todoktodok.presentation.view.serialization.SerializationDiscussion

class CreatedDiscussionsRoomFragment : Fragment(R.layout.fragment_created_discussions_room) {
    private var _binding: FragmentCreatedDiscussionsRoomBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCreatedDiscussionsRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var discussionAdapter: DiscussionAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        discussionAdapter = DiscussionAdapter(userDiscussionAdapterHandler, BaseDiscussionViewHolder.ViewHolderType.WRITER_HIDDEN)
        val discussions =
            arguments?.getParcelableArrayListCompat<SerializationDiscussion>(
                ARG_CREATED_MEMBER_DISCUSSIONS,
            ) ?: emptyList()

        if (discussions.isEmpty()) {
            showEmptyResourceView()
        } else {
            showCreatedDiscussions(discussions)
        }
    }

    private fun showEmptyResourceView() {
        with(binding) {
            rvDiscussions.visibility = View.GONE
            viewResourceNotFound.show(
                getString(R.string.profile_not_has_created_discussion_title),
                getString(R.string.profile_not_has_created_discussion_subtitle),
                getString(R.string.profile_action_created_discussion),
                { moveToCreateDiscussion() },
            )
        }
    }

    private fun moveToCreateDiscussion() {
        val intent = SelectBookActivity.Intent(requireContext())
        startActivity(intent)
    }

    private fun showCreatedDiscussions(discussions: List<SerializationDiscussion>) {
        with(binding) {
            val createdDiscussions =
                discussions.map { discussion -> DiscussionUiState(discussion.toDomain()) }
            rvDiscussions.visibility = View.VISIBLE
            rvDiscussions.adapter = discussionAdapter
            discussionAdapter.submitList(createdDiscussions)
        }
    }

    private val userDiscussionAdapterHandler =
        object : DiscussionAdapter.Handler {
            override fun onItemClick(index: Int) {
                moveToDiscussionDetail(index)
            }
        }

    private fun moveToDiscussionDetail(index: Int) {
        val discussionId = discussionAdapter.currentList[index].discussionId
        val intent =
            DiscussionDetailActivity.Intent(requireContext(), discussionId)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_CREATED_MEMBER_DISCUSSIONS = "created_discussions"

        fun newInstance(discussions: List<SerializationDiscussion>): CreatedDiscussionsRoomFragment =
            CreatedDiscussionsRoomFragment().apply {
                arguments = bundleOf(ARG_CREATED_MEMBER_DISCUSSIONS to ArrayList(discussions))
            }
    }
}
