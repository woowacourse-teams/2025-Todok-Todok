package com.team.todoktodok.presentation.view.discussion.create

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.team.todoktodok.databinding.ActivityCreateDiscussionRoomBinding

class CreateDiscussionRoomActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCreateDiscussionRoomBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initSystemBar()
    }

    private fun initSystemBar() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                binding.root.paddingLeft,
                systemBars.top,
                binding.root.paddingRight,
                systemBars.bottom,
            )
            insets
        }
    }
}
