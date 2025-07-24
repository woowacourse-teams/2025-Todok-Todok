package com.example.todoktodok.presentation.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.todoktodok.R
import com.example.todoktodok.databinding.ActivityMainBinding
import com.example.todoktodok.presentation.view.discussion.discussions.DiscussionFragment
import com.example.todoktodok.presentation.view.library.LibraryFragment
import com.example.todoktodok.presentation.view.note.NoteFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        initSystemBar()
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        binding.bnvContainer.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_note -> navigateScreen(MainScreen.NOTE)
                R.id.menu_discussion -> navigateScreen(MainScreen.DISCUSSION)
                R.id.menu_library -> navigateScreen(MainScreen.LIBRARY)
            }
            true
        }
    }

    private fun initSystemBar() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.bnvContainer.setOnApplyWindowInsetsListener { view, insets ->
            view.updatePadding(bottom = 0)
            insets
        }
    }

    private fun navigateScreen(mainScreen: MainScreen) {
        supportFragmentManager.commit {
            when (mainScreen) {
                MainScreen.NOTE -> replace<NoteFragment>(R.id.fcv_container)
                MainScreen.DISCUSSION -> replace<DiscussionFragment>(R.id.fcv_container)
                MainScreen.LIBRARY -> replace<LibraryFragment>(R.id.fcv_container)
            }
        }
    }
}
