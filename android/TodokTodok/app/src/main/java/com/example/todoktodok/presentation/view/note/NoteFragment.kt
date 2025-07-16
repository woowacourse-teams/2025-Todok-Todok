package com.example.todoktodok.presentation.view.note

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.todoktodok.R
import com.example.todoktodok.databinding.FragmentNoteBinding

class NoteFragment : Fragment(R.layout.fragment_note) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentNoteBinding.bind(view)
    }
}
