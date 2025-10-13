package com.team.todoktodok.presentation.xml.profile

import android.app.Activity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.team.todoktodok.App
import com.team.todoktodok.presentation.xml.profile.vm.ProfileViewModel
import com.team.todoktodok.presentation.xml.profile.vm.ProfileViewModelFactory
import kotlin.getValue

abstract class BaseProfileFragment(
    @LayoutRes layoutId: Int,
) : Fragment(layoutId) {
    protected val viewModel: ProfileViewModel by activityViewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        ProfileViewModelFactory(repositoryModule.memberRepository, repositoryModule.tokenRepository)
    }

    protected val discussionDetailLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.refreshUserActivities()
            }
        }
}
