package com.team.todoktodok.presentation.xml.setting.manage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.domain.model.member.BlockedMember
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentManageBlockedMembersBinding
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.core.component.CommonDialog
import com.team.todoktodok.presentation.xml.setting.manage.adapter.BlockedMembersAdapter
import com.team.todoktodok.presentation.xml.setting.manage.vm.ManageBlockedMembersViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ManageBlockedMembersFragment : Fragment(R.layout.fragment_manage_blocked_members) {
    private val viewModel: ManageBlockedMembersViewModel by viewModels()

    private lateinit var blockedMembersAdapter: BlockedMembersAdapter

    @Inject
    lateinit var messageConverter: ExceptionMessageConverter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentManageBlockedMembersBinding.bind(view)

        setUpUiState(binding)
        setUpUiEvent(binding)
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

    private fun setUpUiState(binding: FragmentManageBlockedMembersBinding) {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            blockedMembersAdapter.submitList(value.members)
            displayResultView(binding, value.members)
            setUpLoading(value.isLoading, binding)
        }
    }

    private fun displayResultView(
        binding: FragmentManageBlockedMembersBinding,
        members: List<BlockedMember>,
    ) {
        if (members.isEmpty()) {
            binding.viewResourceNotFound.show(getString(R.string.setting_manage_blocked_members_empty_message))
        } else {
            binding.viewResourceNotFound.hide()
        }
    }

    private fun setUpLoading(
        isLoading: Boolean,
        binding: FragmentManageBlockedMembersBinding,
    ) {
        if (isLoading) {
            binding.progressBar.show()
        } else {
            binding.progressBar.hide()
        }
    }

    private fun setUpUiEvent(binding: FragmentManageBlockedMembersBinding) {
        viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is ManageBlockedMembersUiEvent.ShowErrorMessage -> {
                    AlertSnackBar(
                        binding.root,
                        messageConverter(event.exception),
                    ).show()
                }
            }
        }
    }
}
