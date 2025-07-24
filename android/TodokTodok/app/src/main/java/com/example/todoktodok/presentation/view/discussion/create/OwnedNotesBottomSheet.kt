package com.example.todoktodok.presentation.view.discussion.create

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.todoktodok.R
import com.example.todoktodok.databinding.BottomSheetOwnedNotesBinding
import com.example.todoktodok.presentation.view.discussion.create.adapter.NoteAdapter
import com.example.todoktodok.presentation.view.discussion.create.vm.DiscussionCreateViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OwnedNotesBottomSheet : BottomSheetDialogFragment(R.layout.owned_books_bottom_sheet) {
    private val viewModel by activityViewModels<DiscussionCreateViewModel>()
    private val noteAdapter: NoteAdapter = NoteAdapter()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        setOnShowDialogListener(dialog)
        return dialog
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = BottomSheetOwnedNotesBinding.bind(view)
        initView(binding)
    }

    private fun initView(binding: BottomSheetOwnedNotesBinding) {
        binding.rvBooks.adapter = noteAdapter
        viewModel.notes.observe(viewLifecycleOwner) { value ->
            noteAdapter.submitList(value)
        }
    }

    private fun setOnShowDialogListener(dialog: BottomSheetDialog) {
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                val halfScreenHeight = getHalfScreenHeight()
                setUpBehavior(behavior, halfScreenHeight)

                bottomSheet.layoutParams.height = halfScreenHeight
            }
        }
    }

    private fun getHalfScreenHeight(): Int {
        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val halfScreenHeight = screenHeight / 2

        return halfScreenHeight
    }

    private fun setUpBehavior(
        behavior: BottomSheetBehavior<View>,
        halfScreenHeight: Int,
    ) {
        behavior.peekHeight = halfScreenHeight
    }
}
