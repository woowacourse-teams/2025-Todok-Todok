package com.team.todoktodok.presentation.utview.discussions

import android.os.Bundle
import android.view.KeyEvent
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.tabs.TabLayout
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivityDiscussionsBinding
import com.team.todoktodok.presentation.core.ext.clearHintOnFocus
import com.team.todoktodok.presentation.utview.discussions.all.AllDiscussionFragment
import com.team.todoktodok.presentation.utview.discussions.my.MyDiscussionFragment

class DiscussionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiscussionsBinding
    private val allDiscussionFragment = AllDiscussionFragment()
    private val myDiscussionFragment = MyDiscussionFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscussionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpSystemBars()
        initFragments()
        initView(binding)
    }

    private fun initFragments() {
        supportFragmentManager.commit {
            add(R.id.fragmentContainerView, allDiscussionFragment, "ALL")
            add(R.id.fragmentContainerView, myDiscussionFragment, "MY")
            hide(myDiscussionFragment)
        }
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
                // TODO : 검색 기능 구현
            }

            etSearchDiscussion.setOnEditorActionListener { v, actionId, event -> TODO("검색 기능 구현") }
            tabLayout.addOnTabSelectedListener(
                object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        when (tab?.position) {
                            0 -> changeFragment(allDiscussionFragment, myDiscussionFragment)
                            1 -> changeFragment(myDiscussionFragment, allDiscussionFragment)
                        }
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {}

                    override fun onTabReselected(tab: TabLayout.Tab?) {}
                },
            )

        }
    }

    private fun changeFragment(
        showFragment: Fragment,
        hideFragment: Fragment,
    ) {
        supportFragmentManager.commit {
            show(showFragment)
            hide(hideFragment)
        }
    }
}
