package com.team.todoktodok.presentation.view.profile.created

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCreatedDiscussionsRoomBinding
import com.team.todoktodok.presentation.core.ext.getParcelableArrayListCompat
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.view.profile.created.adapter.UserDiscussionAdapter
import com.team.todoktodok.presentation.view.serialization.SerializationMemberDiscussion

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

    private lateinit var discussionAdapter: UserDiscussionAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val discussions =
            arguments?.getParcelableArrayListCompat<SerializationMemberDiscussion>(
                ARG_CREATED_MEMBER_DISCUSSIONS,
            )

        discussionAdapter = UserDiscussionAdapter(userDiscussionAdapterHandler)
        binding.rvDiscussions.adapter = discussionAdapter

        discussions?.let {
            val discussions = it.map { discussion -> discussion.toDomain() }
            discussionAdapter.submitList(discussions)
        }
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
        private const val ARG_CREATED_MEMBER_DISCUSSIONS = "created_member_discussions"

        fun newInstance(discussions: List<SerializationMemberDiscussion>): CreatedDiscussionsRoomFragment =
            CreatedDiscussionsRoomFragment().apply {
                arguments = bundleOf(ARG_CREATED_MEMBER_DISCUSSIONS to discussions)
            }
    }
}
