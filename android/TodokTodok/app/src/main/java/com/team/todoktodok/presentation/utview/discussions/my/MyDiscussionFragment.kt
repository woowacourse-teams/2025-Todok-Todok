package com.team.todoktodok.presentation.utview.discussions.my

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentMyDiscussionBinding

class MyDiscussionFragment : Fragment(R.layout.fragment_my_discussion) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        val binding = FragmentMyDiscussionBinding.bind(view)
    }
}
