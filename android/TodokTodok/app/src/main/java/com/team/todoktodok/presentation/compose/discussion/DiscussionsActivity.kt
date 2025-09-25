package com.team.todoktodok.presentation.compose.discussion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.team.todoktodok.App
import com.team.todoktodok.presentation.compose.discussion.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.compose.discussion.vm.DiscussionsViewModelFactory
import com.team.todoktodok.presentation.compose.theme.TodoktodokTheme
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.ext.getParcelableCompat
import com.team.todoktodok.presentation.view.serialization.SerializationNotificationType
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.xml.notification.NotificationActivity
import com.team.todoktodok.presentation.xml.profile.ProfileActivity
import com.team.todoktodok.presentation.xml.profile.UserProfileTab
import com.team.todoktodok.presentation.xml.serialization.SerializationFcmNotification

class DiscussionsActivity : ComponentActivity() {
    private val viewModel: DiscussionsViewModel by viewModels {
        val container = (application as App).container
        val repositoryModule = container.repositoryModule
        DiscussionsViewModelFactory(
            repositoryModule.discussionRepository,
            repositoryModule.memberRepository,
            repositoryModule.notificationRepository,
            container.connectivityObserver,
        )
    }

    private val discussionDetailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
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

    private var lastBackPressed = 0L
    private val timeout = 2000L

    private val messageConverter: ExceptionMessageConverter = ExceptionMessageConverter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoktodokTheme {
                DiscussionsScreen(
                    viewModel = viewModel,
                    exceptionMessageConverter = messageConverter,
                    onDiscussionClick = ::moveToDiscussionDetail,
                    onClickNotification = ::moveToNotification,
                    onClickMyDiscussionHeader = ::moveToProfile,
                    onClickProfile = ::moveToProfile,
                )
            }
        }
        handleNotificationDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationDeepLink(intent)
    }

    private fun moveToNotification() {
        startActivity(NotificationActivity.Intent(this))
    }

    private fun moveToDiscussionDetail(discussionId: Long) {
        discussionDetailLauncher.launch(
            DiscussionDetailActivity.Companion.Intent(
                this,
                discussionId,
            ),
        )
    }

    private fun moveToProfile() {
        startActivity(ProfileActivity.Intent(this))
    }

    private fun moveToProfile(tab: UserProfileTab) {
        when (tab) {
            UserProfileTab.ACTIVATED_BOOKS -> Unit
            UserProfileTab.CREATED_DISCUSSIONS -> moveToMyCreatedDiscussion()
            UserProfileTab.PARTICIPATED_DISCUSSIONS -> moveMyParticipatedDiscussion()
        }
    }

    private fun moveToMyCreatedDiscussion() {
        startActivity(
            ProfileActivity.Intent(this, initialTab = UserProfileTab.CREATED_DISCUSSIONS),
        )
    }

    private fun moveMyParticipatedDiscussion() {
        startActivity(
            ProfileActivity.Intent(this, initialTab = UserProfileTab.PARTICIPATED_DISCUSSIONS),
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMyDiscussions()
    }

    private fun handleNotificationDeepLink(intent: Intent) {
        val notification: SerializationFcmNotification? =
            intent.getParcelableCompat<SerializationFcmNotification>("notification") as? SerializationFcmNotification
                ?: null

        triggerToMoveDiscussionDetail(notification)
    }

    private fun DiscussionsActivity.triggerToMoveDiscussionDetail(notification: SerializationFcmNotification?) {
        if (notification != null) {
            when (notification.type) {
                SerializationNotificationType.LIKE -> {
                    val detailIntent =
                        DiscussionDetailActivity.Intent(
                            this,
                            notification.discussionId,
                        )
                    startActivity(detailIntent)
                }

                SerializationNotificationType.COMMENT -> {
                    val detailIntent =
                        DiscussionDetailActivity.Intent(
                            this,
                            notification.discussionId,
                        )
                    startActivity(detailIntent)
                }

                SerializationNotificationType.REPLY -> {
                    val detailIntent =
                        DiscussionDetailActivity.Intent(
                            this,
                            notification.discussionId,
                        )
                    startActivity(detailIntent)
                }
            }
        }
    }

    companion object {
        const val EXTRA_DELETE_DISCUSSION = "delete_discussion"
        const val EXTRA_WATCHED_DISCUSSION_ID = "watched_discussion_id"
        private const val DEFAULT_DISCUSSION_ID = -1L

        fun Intent(context: Context) = Intent(context, DiscussionsActivity::class.java)
    }
}
