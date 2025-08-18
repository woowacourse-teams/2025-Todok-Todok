package com.team.todoktodok.presentation.view.setting.manage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentManageBlockedMembersBinding
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.CommonDialog
import com.team.todoktodok.presentation.view.setting.manage.adapter.BlockedMembersAdapter
import com.team.todoktodok.presentation.view.setting.manage.vm.ManageBlockedMembersViewModel
import com.team.todoktodok.presentation.view.setting.manage.vm.ManageBlockedMembersViewModelFactory

class ManageBlockedMembersFragment : Fragment(R.layout.fragment_manage_blocked_members) {
    private val viewModel: ManageBlockedMembersViewModel by viewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        ManageBlockedMembersViewModelFactory(repositoryModule.memberRepository)
    }

    private lateinit var blockedMembersAdapter: BlockedMembersAdapter

    private val messageConverter: ExceptionMessageConverter by lazy {
        ExceptionMessageConverter()
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentManageBlockedMembersBinding.bind(view)

        setUpUiState()
        setUpUiEvent()
        initView(binding)
    }

    private fun initView(binding: FragmentManageBlockedMembersBinding) {
        blockedMembersAdapter = BlockedMembersAdapter(blockedMembersAdapterHandler)
        binding.rvBlockedMembers.adapter = blockedMembersAdapter

        parentFragmentManager.setFragmentResultListener(
            CommonDialog.REQUEST_KEY_COMMON_DIALOG,
            viewLifecycleOwner,
        ) { _, bundle ->
            val isConfirmed = bundle.getBoolean(CommonDialog.RESULT_KEY_COMMON_DIALOG, false)
            if (isConfirmed) viewModel.unblockMember()
        }
    }

    private val blockedMembersAdapterHandler =
        object : BlockedMembersAdapter.Handler {
            override fun onUnblockClicked(index: Int) {
                viewModel.onSelectMember(blockedMembersAdapter.currentList[index].memberId)
                CommonDialog
                    .newInstance(
                        message = getString(R.string.setting_unblock_content),
                        submitButtonText = getString(R.string.setting_unblock),
                    ).show(parentFragmentManager, CommonDialog.TAG)
            }
        }

    private fun setUpUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            blockedMembersAdapter.submitList(value.members)
        }
    }

    private fun setUpUiEvent() {
        viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is ManageBlockedMembersUiEvent.ShowErrorMessage -> {
                    messageConverter(event.exception)
                }
            }
        }
    }
}
