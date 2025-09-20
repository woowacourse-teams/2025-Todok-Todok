package com.team.todoktodok.presentation.view.discussions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.view.book.SelectBookActivity
import com.team.todoktodok.presentation.view.discussions.all.AllDiscussionFragment
import com.team.todoktodok.presentation.view.discussions.hot.HotDiscussionFragment
import com.team.todoktodok.presentation.view.discussions.my.MyDiscussionFragment
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModelFactory
import com.team.todoktodok.presentation.view.profile.ProfileActivity

class DiscussionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiscussionsBinding

    private val viewModel: DiscussionsViewModel by viewModels {
        val container = (application as App).container
        val repositoryModule = container.repositoryModule
        DiscussionsViewModelFactory(
            repositoryModule.discussionRepository,
            repositoryModule.memberRepository,
            container.connectivityObserver,
        )
    }

    private val messageConverter: ExceptionMessageConverter by lazy {
        ExceptionMessageConverter()
    }

    private lateinit var manager: InputMethodManager
    private lateinit var hotDiscussionFragment: HotDiscussionFragment
    private lateinit var allDiscussionFragment: AllDiscussionFragment
    private lateinit var myDiscussionFragment: MyDiscussionFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscussionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        setSupportActionBar(binding.toolbar)
        setupSystemBars()
        initFragments()
        setUpLoadingState()
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
        val fm = supportFragmentManager

        hotDiscussionFragment =
            fm.findFragmentByTag(HOT_DISCUSSION_FRAGMENT_TAG) as? HotDiscussionFragment
                ?: HotDiscussionFragment().also {
                    fm.commit { add(R.id.fragmentContainerView, it, HOT_DISCUSSION_FRAGMENT_TAG) }
                }

        allDiscussionFragment =
            fm.findFragmentByTag(ALL_DISCUSSION_FRAGMENT_TAG) as? AllDiscussionFragment
                ?: AllDiscussionFragment().also {
                    fm.commit { add(R.id.fragmentContainerView, it, ALL_DISCUSSION_FRAGMENT_TAG) }
                }

        myDiscussionFragment =
            fm.findFragmentByTag(MY_DISCUSSION_FRAGMENT_TAG) as? MyDiscussionFragment
                ?: MyDiscussionFragment().also {
                    fm.commit { add(R.id.fragmentContainerView, it, MY_DISCUSSION_FRAGMENT_TAG) }
                }

        fm.commit {
            hide(allDiscussionFragment)
            hide(myDiscussionFragment)
            show(hotDiscussionFragment)
        }
    }

    private fun setUpLoadingState() {
        viewModel.baseUiState.observe(this) { value ->
            if (value.isLoading) {
                binding.progressBar.show()
            } else {
                binding.progressBar.hide()
            }

            if (value.isRestoring) {
                AlertSnackBar(
                    binding.root,
                    R.string.network_try_connection,
                ).show()
            }
        }
    }

    private fun setupUiEvent() {
        viewModel.uiEvent.observe(this) { event ->
            when (event) {
                is DiscussionsUiEvent.ShowErrorMessage -> {
                    AlertSnackBar(binding.root, messageConverter(event.exception)).show()
                }

                DiscussionsUiEvent.ShowSearchResult -> {
                    moveToLatestDiscussionTab()
                    allDiscussionFragment.showSearchResults()
                }

                DiscussionsUiEvent.ClearSearchResult -> {
                    binding.etSearchDiscussion.text = null
                }
            }
        }
    }

    private fun initView() {
        setupSearchBar()
        setupTabLayout()
        setupNavigationButton()
    }

    private fun setupSearchBar() =
        with(binding) {
            var lastSearchTime = 0L

            etSearchDiscussion.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (actionId == EditorInfo.IME_NULL && event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
                ) {
                    val now = System.currentTimeMillis()
                    if (now - lastSearchTime > SEARCH_DURATION) {
                        lastSearchTime = now
                        triggerSearch()
                    }
                    true
                } else {
                    false
                }
            }

            etSearchDiscussion.doAfterTextChanged { text ->
                if (text.isNullOrEmpty()) {
                    viewModel.clearSearchResult()
                    allDiscussionFragment.showLatestDiscussions()
                }
            }
        }

    private fun triggerSearch() =
        with(binding) {
            val newKeyword = etSearchDiscussion.text?.toString()?.trim()
            val latestKeyword =
                viewModel.uiState.value
                    ?.searchDiscussion
                    ?.searchKeyword
            val isSameKeyword = newKeyword == latestKeyword

            if (!newKeyword.isNullOrEmpty()) {
                if (isSameKeyword) return@with
                viewModel.loadSearchedDiscussions(newKeyword)
                hideSoftKeyboard()
            } else {
                AlertSnackBar(
                    root,
                    R.string.discussion_search_bar_hint,
                ).show()
            }
        }

    private fun setupNavigationButton() =
        with(binding) {
            ivDiscussionNavigation.setOnClickListener {
                val intent = SelectBookActivity.Intent(this@DiscussionsActivity)
                startActivity(intent)
            }
        }

    private fun setupTabLayout() =
        with(binding) {
            tabLayout.addOnTabSelectedListener(
                object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        changeTab(tab ?: return)
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {}

                    override fun onTabReselected(tab: TabLayout.Tab?) {}
                },
            )
        }

    private fun moveToLatestDiscussionTab() =
        with(binding) {
            if (tabLayout.selectedTabPosition != ALL_DISCUSSION_TAB_POSITION) {
                tabLayout.getTabAt(ALL_DISCUSSION_TAB_POSITION)?.select()
            }
        }

    private fun changeTab(tab: TabLayout.Tab) {
        val index = tab.position
        val selectedFilter = DiscussionFilter.entries[index]

        when (selectedFilter) {
            DiscussionFilter.HOT -> changeFragment(hotDiscussionFragment)
            DiscussionFilter.ALL -> changeFragment(allDiscussionFragment)
            DiscussionFilter.MINE -> changeFragment(myDiscussionFragment)
        }
    }

    private fun changeFragment(showFragment: Fragment) {
        supportFragmentManager.commit {
            listOf(
                hotDiscussionFragment,
                allDiscussionFragment,
                myDiscussionFragment,
            ).forEach { fragment ->
                if (fragment == showFragment) show(fragment) else hide(fragment)
            }
        }
    }

    private fun hideSoftKeyboard() {
        manager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS,
        )
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

    override fun onResume() {
        super.onResume()
        binding.etSearchDiscussion.clearFocus()
    }

    companion object {
        private const val HOT_DISCUSSION_FRAGMENT_TAG = "HOT"

        private const val ALL_DISCUSSION_TAB_POSITION = 1
        private const val ALL_DISCUSSION_FRAGMENT_TAG = "ALL"

        private const val MY_DISCUSSION_FRAGMENT_TAG = "MY"
        private const val SEARCH_DURATION = 300

        fun Intent(context: Context) = Intent(context, DiscussionsActivity::class.java)
    }
}
