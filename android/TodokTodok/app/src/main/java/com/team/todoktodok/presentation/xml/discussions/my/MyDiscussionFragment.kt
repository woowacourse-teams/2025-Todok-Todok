package com.team.todoktodok.presentation.xml.discussions.my

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentMyDiscussionBinding
import com.team.todoktodok.presentation.compose.discussion.my.MyDiscussionsScreen
import com.team.todoktodok.presentation.compose.theme.TodoktodokTheme
import com.team.todoktodok.presentation.xml.discussions.BaseDiscussionsFragment
import com.team.todoktodok.presentation.xml.profile.ProfileActivity
import com.team.todoktodok.presentation.xml.profile.UserProfileTab

class MyDiscussionFragment : BaseDiscussionsFragment(R.layout.fragment_my_discussion) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = FragmentMyDiscussionBinding.inflate(inflater, container, false)
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TodoktodokTheme {
                    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

                    MyDiscussionsScreen(
                        onClick = { discussionId ->
                            moveToDiscussionDetail(discussionId)
                        },
                        onClickHeader = { userProfileTab ->
                            when (userProfileTab) {
                                UserProfileTab.ACTIVATED_BOOKS -> Unit
                                UserProfileTab.CREATED_DISCUSSIONS -> moveToMyCreatedDiscussion()
                                UserProfileTab.PARTICIPATED_DISCUSSIONS -> moveMyParticipatedDiscussion()
                            }
                        },
                        uiState = uiState.value.myDiscussion,
                    )
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        viewModel.loadMyDiscussions()
    }

    private fun moveToMyCreatedDiscussion() {
        startActivity(
            ProfileActivity.Intent(
                requireContext(),
                initialTab = UserProfileTab.CREATED_DISCUSSIONS,
            ),
        )
    }

    private fun moveMyParticipatedDiscussion() {
        startActivity(
            ProfileActivity.Intent(
                requireContext(),
                initialTab = UserProfileTab.PARTICIPATED_DISCUSSIONS,
            ),
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMyDiscussions()
    }
}
