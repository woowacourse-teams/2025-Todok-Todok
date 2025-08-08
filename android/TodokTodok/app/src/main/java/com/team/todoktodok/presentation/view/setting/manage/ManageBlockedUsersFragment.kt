package com.team.todoktodok.presentation.view.setting.manage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentManageBlockedUsersBinding

class ManageBlockedUsersFragment : Fragment(R.layout.fragment_manage_blocked_users) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentManageBlockedUsersBinding.bind(view)
    }
}
