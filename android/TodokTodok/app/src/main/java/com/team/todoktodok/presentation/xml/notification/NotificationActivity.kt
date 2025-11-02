package com.team.todoktodok.presentation.xml.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ActivityNotificationBinding
import com.team.todoktodok.presentation.compose.main.MainActivity.Companion.KEY_REFRESH_NOTIFICATION
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.xml.notification.adapter.NotificationAdapter
import com.team.todoktodok.presentation.xml.notification.adapter.NotificationGroup
import com.team.todoktodok.presentation.xml.notification.vm.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {
    private val viewModel by viewModels<NotificationViewModel>()

    private val launcher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.initNotifications()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityNotificationBinding.inflate(layoutInflater)
        val adapter =
            NotificationAdapter { position ->
                viewModel.updateUnReadStatus(position = position)
            }

        setContentView(binding.root)
        initSystemBar(binding)
        initView(binding, adapter)
        setUpUiState(binding, adapter)
        setUpUiEvent(binding)
    }

    private fun setUpUiState(
        binding: ActivityNotificationBinding,
        adapter: NotificationAdapter,
    ) {
        viewModel.uiState.observe(this) { state ->
            observeIsLoading(state.isLoading, binding)
            updateNotifications(state.notificationGroup, adapter)
        }
    }

    private fun setUpUiEvent(binding: ActivityNotificationBinding) {
        viewModel.uiEvent.observe(this) { uiEvent ->
            when (uiEvent) {
                is NotificationUiEvent.NavigateToDiscussionRoom -> {
                    val intent = DiscussionDetailActivity.Intent(this, uiEvent.discussionRoomId)
                    launcher.launch(intent)
                }

                is NotificationUiEvent.ShowException -> {
                    val messageConverter = ExceptionMessageConverter()
                    AlertSnackBar(binding.root, messageConverter(uiEvent.exception)).show()
                }
            }
        }
    }

    private fun updateNotifications(
        notifications: List<NotificationGroup>,
        adapter: NotificationAdapter,
    ) {
        adapter.submitList(notifications)
    }

    private fun observeIsLoading(
        isLoading: Boolean,
        binding: ActivityNotificationBinding,
    ) {
        if (isLoading) {
            binding.rvNotifications.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.rvNotifications.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun initView(
        binding: ActivityNotificationBinding,
        adapter: NotificationAdapter,
    ) {
        binding.apply {
            btnBack.setOnClickListener { navigationToMain() }
            onBackPressedDispatcher.addCallback(
                this@NotificationActivity,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        navigationToMain()
                    }
                },
            )

            rvNotifications.adapter = adapter
            val touchHelper =
                ItemTouchHelper(
                    object : ItemTouchHelper.SimpleCallback(
                        0,
                        ItemTouchHelper.LEFT,
                    ) {
                        override fun onMove(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder,
                        ): Boolean = false

                        override fun getMovementFlags(
                            recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                        ): Int {
                            val position = viewHolder.bindingAdapterPosition
                            val item = adapter.currentList.getOrNull(position)
                            return if (item is NotificationGroup.Notification) {
                                makeMovementFlags(0, ItemTouchHelper.LEFT)
                            } else {
                                makeMovementFlags(0, 0)
                            }
                        }

                        override fun onSwiped(
                            viewHolder: RecyclerView.ViewHolder,
                            direction: Int,
                        ) {
                            val position = viewHolder.bindingAdapterPosition
                            val current = adapter.currentList
                            if (position !in current.indices) {
                                adapter.notifyItemChanged(position)
                                return
                            }

                            val item = current[position]
                            if (item !is NotificationGroup.Notification) {
                                adapter.notifyItemChanged(position)
                                return
                            }

                            val newList = current.toMutableList().apply { removeAt(position) }
                            adapter.submitList(newList)

                            viewModel.deleteNotification(position)
                        }

                        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) = 0.5f
                    },
                )
            touchHelper.attachToRecyclerView(rvNotifications)
        }
    }

    private fun navigationToMain() {
        val intent =
            Intent().apply {
                putExtra(KEY_REFRESH_NOTIFICATION, true)
            }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun initSystemBar(binding: ActivityNotificationBinding) {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        fun Intent(context: Context): Intent = Intent(context, NotificationActivity::class.java)
    }
}
