package com.team.todoktodok.presentation.view.notification

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.team.todoktodok.App
import com.team.todoktodok.databinding.ActivityNotificationBinding
import com.team.todoktodok.presentation.view.book.vm.SelectBookViewModel
import com.team.todoktodok.presentation.view.notification.vm.NotificationViewModelFactory

class NotificationActivity : AppCompatActivity() {
    private val viewModel by viewModels<SelectBookViewModel> {
        val repositoryModule = (application as App).container.repositoryModule
        NotificationViewModelFactory(
            repositoryModule.notificationRepository,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
