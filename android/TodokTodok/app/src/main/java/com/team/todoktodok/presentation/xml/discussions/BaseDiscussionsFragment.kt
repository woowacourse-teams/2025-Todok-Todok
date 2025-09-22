package com.team.todoktodok.presentation.xml.discussions

import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.team.todoktodok.App
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.xml.discussions.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.xml.discussions.vm.DiscussionsViewModelFactory

abstract class BaseDiscussionsFragment(
    @LayoutRes layoutId: Int,
) : Fragment(layoutId) {
    protected val viewModel: DiscussionsViewModel by activityViewModels {
        val container = (requireActivity().application as App).container
        val repositoryModule = container.repositoryModule
        DiscussionsViewModelFactory(
            repositoryModule.discussionRepository,
            repositoryModule.memberRepository,
            repositoryModule.notificationRepository,
            container.connectivityObserver,
        )
    }

    protected val discussionDetailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    when {
                        data.hasExtra(EXTRA_DELETE_DISCUSSION) -> {
                            val deletedId =
                                data.getLongExtra(
                                    EXTRA_DELETE_DISCUSSION,
                                    DEFAULT_DISCUSSION_ID,
                                )
                            if (deletedId != DEFAULT_DISCUSSION_ID) {
                                viewModel.removeDiscussion(deletedId)
                            }
                        }

                        data.hasExtra(EXTRA_WATCHED_DISCUSSION_ID) -> {
                            val discussionId =
                                data.getLongExtra(
                                    EXTRA_WATCHED_DISCUSSION_ID,
                                    DEFAULT_DISCUSSION_ID,
                                )
                            if (discussionId != DEFAULT_DISCUSSION_ID) {
                                viewModel.modifyDiscussion(discussionId)
                            }
                        }
                    }
                }
            }
        }

    protected fun moveToDiscussionDetail(discussionId: Long) {
        discussionDetailLauncher.launch(
            DiscussionDetailActivity.Intent(
                requireContext(),
                discussionId,
            ),
        )
    }

    companion object {
        const val EXTRA_DELETE_DISCUSSION = "delete_discussion"
        const val EXTRA_WATCHED_DISCUSSION_ID = "watched_discussion_id"
        private const val DEFAULT_DISCUSSION_ID = -1L
    }
}
