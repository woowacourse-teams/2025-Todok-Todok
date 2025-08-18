package com.team.todoktodok.presentation.view.discussions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.team.domain.model.DiscussionFilter
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivityDiscussionsBinding
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.core.ext.clearHintOnFocus
import com.team.todoktodok.presentation.view.book.SelectBookActivity
import com.team.todoktodok.presentation.view.discussions.all.AllDiscussionFragment
import com.team.todoktodok.presentation.view.discussions.my.MyDiscussionFragment
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModelFactory
import com.team.todoktodok.presentation.view.profile.ProfileActivity

class DiscussionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiscussionsBinding

    private val viewModel: DiscussionsViewModel by viewModels {
        val repositoryModule = (application as App).container.repositoryModule
        DiscussionsViewModelFactory(repositoryModule.discussionRepository)
    }

    private val messageConverter: ExceptionMessageConverter by lazy {
        ExceptionMessageConverter()
    }

    private lateinit var manager: InputMethodManager

    private val allDiscussionFragment = AllDiscussionFragment()
    private val myDiscussionFragment = MyDiscussionFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscussionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        setSupportActionBar(binding.toolbar)
        setupSystemBars()
        initFragments()
        setupUiState()
        setupUiEvent()
        initView()
    }

    private fun setupSystemBars() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
    }

    private fun initFragments() {
        supportFragmentManager.commit {
            add(R.id.fragmentContainerView, allDiscussionFragment, ALL_DISCUSSION_FRAGMENT_TAG)
            add(R.id.fragmentContainerView, myDiscussionFragment, MY_DISCUSSION_FRAGMENT_TAG)
            hide(myDiscussionFragment)
        }
    }

    private fun setupUiState() {
        viewModel.uiState.observe(this) { state ->
            updateTabs(state.allDiscussionsSize, state.myDiscussionsSize)
            updateLoadingState(state.isLoading)
        }
    }

    private fun setupUiEvent() {
        viewModel.uiEvent.observe(this) { event ->
            when (event) {
                is DiscussionsUiEvent.ShowErrorMessage -> {
                    AlertSnackBar(binding.root, messageConverter(event.exception)).show()
                }

                else -> Unit
            }
        }
    }

    private fun updateTabs(
        allDiscussionSize: Int,
        myDiscussionSize: Int,
    ) = with(binding.tabLayout) {
        getTabAt(ALL_DISCUSSION_TAB_POSITION)?.text =
            getString(R.string.discussion_tab_title_all).format(allDiscussionSize)

        getTabAt(MY_DISCUSSION_TAB_POSITION)?.text =
            getString(R.string.discussion_tab_title_my).format(myDiscussionSize)
    }

    private fun updateLoadingState(isLoading: Boolean) {
        when (isLoading) {
            true -> binding.progressBar.show()
            false -> binding.progressBar.hide()
        }
    }

    private fun initView() {
        with(binding) {
            val hint = getString(R.string.discussion_search_bar_hint)
            etSearchDiscussion.clearHintOnFocus(binding.etSearchDiscussionLayout, hint)

            etSearchDiscussion.setOnEditorActionListener { v, actionId, event ->
                triggerSearch()
                true
            }

            etSearchDiscussion.doAfterTextChanged {
                if (it.isNullOrEmpty()) {
                    viewModel.loadSearchedDiscussions(it.toString())
                    hideSoftKeyboard()
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
                val intent = SelectBookActivity.Intent(this@DiscussionsActivity)
                startActivity(intent)
            }
        }
    }

    private fun triggerSearch() {
        val editableText = binding.etSearchDiscussion.text
        val keyword = editableText?.toString()?.trim()
        val isKeywordNotEmpty = !keyword.isNullOrEmpty()
        if (isKeywordNotEmpty) {
            viewModel.loadSearchedDiscussions(keyword)
            hideSoftKeyboard()
        }
    }

    private fun changeTab(tab: TabLayout.Tab) {
        val index = tab.position
        val selectedFilter = DiscussionFilter.entries[index]
        viewModel.updateTab(selectedFilter, THROTTLE_DURATION)
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

    private fun hideSoftKeyboard() {
        manager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS,
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDiscussions()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.discussion_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            R.id.item_profile -> {
                startActivity(ProfileActivity.Intent(this))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    companion object {
        private const val ALL_DISCUSSION_TAB_POSITION = 0
        private const val ALL_DISCUSSION_FRAGMENT_TAG = "ALL"

        private const val MY_DISCUSSION_TAB_POSITION = 1
        private const val MY_DISCUSSION_FRAGMENT_TAG = "MY"

        private const val THROTTLE_DURATION = 500L

        fun Intent(context: Context) = Intent(context, DiscussionsActivity::class.java)
    }
}
