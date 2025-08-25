package com.team.todoktodok.presentation.view.discussions

import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.team.todoktodok.App
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModelFactory

abstract class BaseDiscussionsFragment(
    @LayoutRes layoutId: Int,
) : Fragment(layoutId) {
    protected val viewModel: DiscussionsViewModel by activityViewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        DiscussionsViewModelFactory(
            repositoryModule.discussionRepository,
            repositoryModule.memberRepository,
        )
    }

    protected val discussionDetailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data
                ?.getLongExtra(
                    EXTRA_DELETE_DISCUSSION_ID,
                    DEFAULT_DELETE_DISCUSSION_ID,
                )?.let {
                    viewModel.removeDiscussion(it)
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
        const val EXTRA_DELETE_DISCUSSION_ID = "delete_discussion_id"
        private const val DEFAULT_DELETE_DISCUSSION_ID = -1L
    }
}
