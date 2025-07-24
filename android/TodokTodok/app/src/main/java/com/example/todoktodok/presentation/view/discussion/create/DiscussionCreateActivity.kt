package com.example.todoktodok.presentation.view.discussion.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.todoktodok.App
import com.example.todoktodok.databinding.ActivityDiscussionCreateBinding
import com.example.todoktodok.presentation.view.discussion.create.vm.DiscussionCreateViewModel
import com.example.todoktodok.presentation.view.discussion.create.vm.DiscussionCreateViewModelFactory
import kotlinx.coroutines.launch

class DiscussionCreateActivity : AppCompatActivity() {
    private val viewModel by viewModels<DiscussionCreateViewModel> {
        val repositoryModule = (application as App).container.repositoryModule
        DiscussionCreateViewModelFactory(
            repositoryModule.discussionRepository,
            repositoryModule.noteRepository,
        )
    }
    val binding: ActivityDiscussionCreateBinding by lazy {
        ActivityDiscussionCreateBinding.inflate(layoutInflater)
    }

    private var bottomSheet: OwnedNotesBottomSheet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        binding.ivDiscussionCreateBack.setOnClickListener { finish() }
        setOnClickSearchNotes()
        setupObservers()
    }

    private fun initView() {
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
    }

    private fun setOnClickSearchNotes() {
        binding.tvNoteSearchBar.setOnClickListener {
            viewModel.onUiEvent(DiscussionCreateUiEvent.ShowOwnedNotes)
        }
    }

    private fun setupObservers() {
        viewModel.uiEvent.observe(this) { value ->
            handleEvent(value)
        }
        viewModel.selectedNote.observe(this) { value ->
            binding.tvNoteSearchBar.text = value.snap
        }
    }

    private fun handleEvent(discussionCreateUiEvent: DiscussionCreateUiEvent) {
        when (discussionCreateUiEvent) {
            DiscussionCreateUiEvent.CreateDiscussion -> viewModel.notes
            DiscussionCreateUiEvent.ShowOwnedNotes -> {
                this.lifecycleScope.launch {
                    viewModel.loadNotes()
                    showBottomSheet()
                }
            }

            is DiscussionCreateUiEvent.SelectNote -> {
                viewModel.selectNote(discussionCreateUiEvent.note)
                dismissBottomSheet()
            }
        }
    }

    private fun showBottomSheet() {
        if (bottomSheet?.isVisible != true) {
            bottomSheet = OwnedNotesBottomSheet()
            bottomSheet?.show(supportFragmentManager, "owned_notes")
        }
    }

    private fun dismissBottomSheet() {
        bottomSheet?.dismissAllowingStateLoss()
        bottomSheet = null
    }

    companion object {
        fun Intent(context: Context): Intent = Intent(context, DiscussionCreateActivity::class.java)
    }
}
