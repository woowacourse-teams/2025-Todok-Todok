package com.team.todoktodok.presentation.view.discussions

import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.team.todoktodok.App
import com.team.todoktodok.presentation.core.ext.getParcelableCompat
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModelFactory
import com.team.todoktodok.presentation.view.serialization.SerializationDiscussion

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
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val deletedId =
                        data.getLongExtra(EXTRA_DELETE_DISCUSSION_ID, DEFAULT_DELETE_DISCUSSION_ID)
                    if (deletedId != DEFAULT_DELETE_DISCUSSION_ID) {
                        viewModel.removeDiscussion(deletedId)
                    }

                    val modifiedDiscussion =
                        data.getParcelableCompat<SerializationDiscussion>(EXTRA_MODIFIED_DISCUSSION)
                    viewModel.modifyDiscussion(modifiedDiscussion)
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
        const val EXTRA_DELETE_DISCUSSION_ID = "delete_discussion_id"
        const val EXTRA_MODIFIED_DISCUSSION = "modified_discussion"
        private const val DEFAULT_DELETE_DISCUSSION_ID = -1L
    }
}
