package com.team.todoktodok.presentation.xml.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.team.todoktodok.App
import com.team.todoktodok.databinding.ActivityNotificationBinding
import com.team.todoktodok.presentation.xml.notification.adapter.NotificationAdapter
import com.team.todoktodok.presentation.xml.notification.adapter.NotificationGroup
import com.team.todoktodok.presentation.xml.notification.vm.NotificationViewModel
import com.team.todoktodok.presentation.xml.notification.vm.NotificationViewModelFactory

class NotificationActivity : AppCompatActivity() {
    private val viewModel by viewModels<NotificationViewModel> {
        val repositoryModule = (application as App).container.repositoryModule
        NotificationViewModelFactory(
            repositoryModule.notificationRepository,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityNotificationBinding.inflate(layoutInflater)
        val adapter = NotificationAdapter()

        setContentView(binding.root)
        initSystemBar(binding)
        initView(binding, adapter)
        viewModel.uiState.observe(this) { event ->
            observeIsLoading(event.isLoading, binding)
            updateNotifications(event.notificationGroup, adapter)
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
            btnBack.setOnClickListener { finish() }
            rvNotifications.adapter = adapter
        }
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
