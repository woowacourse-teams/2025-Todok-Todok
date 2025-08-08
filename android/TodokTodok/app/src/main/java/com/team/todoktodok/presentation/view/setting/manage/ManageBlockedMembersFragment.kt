package com.team.todoktodok.presentation.view.setting.manage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentManageBlockedMembersBinding
import com.team.todoktodok.log.AppLogger
import com.team.todoktodok.presentation.view.setting.manage.dapter.BlockedMembersAdapter
import com.team.todoktodok.presentation.view.setting.manage.vm.ManageBlockedMembersViewModel
import com.team.todoktodok.presentation.view.setting.manage.vm.ManageBlockedMembersViewModelFactory

class ManageBlockedMembersFragment : Fragment(R.layout.fragment_manage_blocked_members) {
    private val viewModel: ManageBlockedMembersViewModel by viewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        ManageBlockedMembersViewModelFactory(repositoryModule.memberRepository)
    }

    private lateinit var unBlockDialog: UnBlockingDialog

    private lateinit var blockedMembersAdapter: BlockedMembersAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentManageBlockedMembersBinding.bind(view)

        setUpUiState()
        initView(binding)
    }

    private fun initView(binding: FragmentManageBlockedMembersBinding) {
        unBlockDialog = UnBlockingDialog()

        blockedMembersAdapter = BlockedMembersAdapter(blockedMembersAdapterHandler)
        binding.rvBlockedMembers.adapter = blockedMembersAdapter

        parentFragmentManager.setFragmentResultListener(
            UnBlockingDialog.REQUEST_KEY_SUPPORT,
            viewLifecycleOwner,
        ) { _, bundle ->
            AppLogger.d("$bundle")
            val isConfirmed = bundle.getBoolean(UnBlockingDialog.RESULT_KEY_SUPPORT, false)
            if (isConfirmed) viewModel.unblockMember()
        }
    }

    private val blockedMembersAdapterHandler =
        object : BlockedMembersAdapter.Handler {
            override fun onUnblockClicked(index: Int) {
                unBlockDialog.show(parentFragmentManager, UnBlockingDialog.TAG)
                viewModel.findMember(index)
            }
        }

    private fun setUpUiState() {
        viewModel.blockedMembers.observe(viewLifecycleOwner) {
            blockedMembersAdapter.submitList(it)
        }
    }
}
