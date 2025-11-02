package com.team.todoktodok.presentation.xml.draft

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.team.todoktodok.databinding.ActivityDraftsBinding
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.xml.draft.adapter.DraftsAdapter
import com.team.todoktodok.presentation.xml.draft.vm.DraftsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DraftsActivity : AppCompatActivity() {
    private val viewModel by viewModels<DraftsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityDraftsBinding.inflate(layoutInflater)
        val adapter = DraftsAdapter { position -> viewModel.selectDraft(position) }

        setContentView(binding.root)
        initSystemBar(binding)
        initView(binding, adapter)
        setUpUiState(adapter)
        setUpUiEvent(binding)
    }

    private fun setUpUiEvent(binding: ActivityDraftsBinding) {
        viewModel.uiEvent.observe(this) { event ->
            when (event) {
                is DraftUiEvent.NavigateToCreateDiscussionRoom -> navigateToSelectedDraft(event)
                is DraftUiEvent.ShowToast -> AlertSnackBar(binding.root, event.error).show()
            }
        }
    }

    private fun navigateToSelectedDraft(event: DraftUiEvent.NavigateToCreateDiscussionRoom) {
        val data =
            Intent().apply {
                putExtra(KEY_SELECTED_DRAFTS, event.id)
            }
        setResult(RESULT_OK, data)
        finish()
    }

    private fun setUpUiState(adapter: DraftsAdapter) {
        viewModel.drafts.observe(this) {
            adapter.submitList(it)
        }
    }

    private fun initSystemBar(binding: ActivityDraftsBinding) {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initView(
        binding: ActivityDraftsBinding,
        adapter: DraftsAdapter,
    ) {
        binding.btnBack.setOnClickListener { finish() }
        binding.rvDrafts.adapter = adapter
    }

    companion object {
        const val KEY_SELECTED_DRAFTS: String = "selected_draft"

        fun Intent(context: Context): Intent = Intent(context, DraftsActivity::class.java)
    }
}
