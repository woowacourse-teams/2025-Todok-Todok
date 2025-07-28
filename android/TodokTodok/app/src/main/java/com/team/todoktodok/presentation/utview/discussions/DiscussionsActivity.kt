package com.team.todoktodok.presentation.utview.discussions

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivityDiscussionsBinding
import com.team.todoktodok.presentation.core.ext.clearHintOnFocus

class DiscussionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiscussionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscussionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpSystemBars()
        initView(binding)
    }

    private fun setUpSystemBars() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initView(binding: ActivityDiscussionsBinding) {
        with(binding) {
            val hint = getString(R.string.ut_discussion_search_bar_hint)
            etSearchDiscussion.clearHintOnFocus(binding.etSearchDiscussionLayout, hint)

            btnSearch.setOnClickListener {
                setUpTab(binding)
            }
        }
    }

    private fun setUpTab(binding: ActivityDiscussionsBinding) {
        with(binding) {
            val changeAbleTab = tabLayout.getTabAt(CHANGEABLE_TAB_POSITION)

            val currentTab = tabLayout.selectedTabPosition
            val searchText = etSearchDiscussion.text.toString()
            if (searchText.isNotEmpty()) {
                changeAbleTab?.text = searchText
                if (currentTab != CHANGEABLE_TAB_POSITION) {
                    tabLayout.selectTab(changeAbleTab)
                }
            }
        }
    }

    companion object {
        private const val CHANGEABLE_TAB_POSITION = 0
    }
}
