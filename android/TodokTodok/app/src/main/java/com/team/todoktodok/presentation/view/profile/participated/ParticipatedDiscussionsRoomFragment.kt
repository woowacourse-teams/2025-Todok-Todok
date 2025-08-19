package com.team.todoktodok.presentation.view.profile.participated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentParticipatedDiscussionsRoomBinding
import com.team.todoktodok.presentation.core.ext.getParcelableArrayListCompat
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.view.profile.created.adapter.UserDiscussionAdapter
import com.team.todoktodok.presentation.view.serialization.SerializationMemberDiscussion

class ParticipatedDiscussionsRoomFragment : Fragment(R.layout.fragment_participated_discussions_room) {
    private var _binding: FragmentParticipatedDiscussionsRoomBinding? = null
    val binding get() = _binding!!

    private lateinit var discussionAdapter: UserDiscussionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentParticipatedDiscussionsRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        discussionAdapter = UserDiscussionAdapter(userDiscussionAdapterHandler)

        val discussions =
            arguments?.getParcelableArrayListCompat<SerializationMemberDiscussion>(
                ARG_PARTICIPATED_MEMBER_DISCUSSIONS,
            ) ?: emptyList()

        if (discussions.isEmpty()) {
            showEmptyResourceView()
        } else {
            binding.viewResourceNotFound.hide()
            showParticipatedDiscussions(discussions)
        }
    }

    private fun showEmptyResourceView() {
        with(binding) {
            rvDiscussions.visibility = View.GONE
            viewResourceNotFound.show(
                getString(R.string.profile_not_has_participated_discussions_title),
                getString(R.string.profile_not_has_participated_discussions_subtitle),
                getString(R.string.profile_action_participated_discussion),
                { moveToDiscussions() },
            )
        }
    }

    private fun showParticipatedDiscussions(discussions: List<SerializationMemberDiscussion>) {
        with(binding) {
            val participatedDiscussions = discussions.map { discussion -> discussion.toDomain() }
            rvDiscussions.visibility = View.VISIBLE
            rvDiscussions.adapter = discussionAdapter
            discussionAdapter.submitList(participatedDiscussions)
        }
    }

    private fun moveToDiscussions() {
        requireActivity().finish()
    }

    private val userDiscussionAdapterHandler =
        object : UserDiscussionAdapter.Handler {
            override fun onSelectDiscussion(index: Int) {
                moveToDiscussionDetail(index)
            }
        }

    private fun moveToDiscussionDetail(index: Int) {
        val discussionRoomId = discussionAdapter.currentList[index].id
        val intent =
            DiscussionDetailActivity.Intent(requireContext(), discussionRoomId)
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
        private const val ARG_PARTICIPATED_MEMBER_DISCUSSIONS = "participated_member_discussions"

        fun newInstance(discussions: List<SerializationMemberDiscussion>): ParticipatedDiscussionsRoomFragment =
            ParticipatedDiscussionsRoomFragment().apply {
                arguments = bundleOf(ARG_PARTICIPATED_MEMBER_DISCUSSIONS to ArrayList(discussions))
            }
    }
}
