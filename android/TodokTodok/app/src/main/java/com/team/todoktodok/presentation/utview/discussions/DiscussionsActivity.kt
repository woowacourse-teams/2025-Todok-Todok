package com.team.todoktodok.presentation.utview.discussions

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.tabs.TabLayout
import com.team.domain.model.DiscussionFilter
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivityDiscussionsBinding
import com.team.todoktodok.presentation.core.ext.clearHintOnFocus
import com.team.todoktodok.presentation.utview.discussion.CreateDiscussionRoomActivity
import com.team.todoktodok.presentation.utview.discussions.all.AllDiscussionFragment
import com.team.todoktodok.presentation.utview.discussions.my.MyDiscussionFragment
import com.team.todoktodok.presentation.utview.discussions.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.utview.discussions.vm.DiscussionsViewModelFactory

class DiscussionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiscussionsBinding

    private val viewModel: DiscussionsViewModel by viewModels {
        val repositoryModule = (application as App).container.repositoryModule
        DiscussionsViewModelFactory(repositoryModule.discussionRepository)
    }

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
                etSearchDiscussion.text?.let { viewModel.loadSearchedDiscussions(it.toString()) }
            }

            etSearchDiscussion.setOnEditorActionListener { v, actionId, event ->
                triggerSearch()
                true
            }

            etSearchDiscussion.doAfterTextChanged {
                if (it?.isEmpty() == true) {
                    viewModel.loadSearchedDiscussions(it.toString())
                }
            }

            tabLayout.addOnTabSelectedListener(
                object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        changeTab(tab ?: return)
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {}

                    override fun onTabReselected(tab: TabLayout.Tab?) {}
                },
            )

            ivDiscussionNavigation.setOnClickListener {
                startActivity(CreateDiscussionRoomActivity.Intent(this@DiscussionsActivity))
            }
        }
    }

    private fun triggerSearch() {
        val editableText = binding.etSearchDiscussion.text
        val keyword = editableText?.toString()?.trim()
        val isKeywordNotEmpty = !keyword.isNullOrEmpty()
        if (isKeywordNotEmpty) viewModel.loadSearchedDiscussions(keyword)
    }

    private fun changeTab(tab: TabLayout.Tab) {
        val index = tab.position
        val selectedFilter = DiscussionFilter.entries[index]
        viewModel.updateTab(selectedFilter)
        changeFragment(
            showFragment = if (selectedFilter == DiscussionFilter.ALL) allDiscussionFragment else myDiscussionFragment,
            hideFragment = if (selectedFilter == DiscussionFilter.ALL) myDiscussionFragment else allDiscussionFragment,
        )
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
