package com.example.todoktodok.presentation.view.discussion.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todoktodok.databinding.ActivityDiscussionCreateBinding

class DiscussionCreateActivity : AppCompatActivity() {
    val binding: ActivityDiscussionCreateBinding by lazy {
        ActivityDiscussionCreateBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
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
        binding.ivDiscussionCreateBack.setOnClickListener { finish() }
    }

    companion object {
        fun Intent(context: Context): Intent = Intent(context, DiscussionCreateActivity::class.java)
    }
}
