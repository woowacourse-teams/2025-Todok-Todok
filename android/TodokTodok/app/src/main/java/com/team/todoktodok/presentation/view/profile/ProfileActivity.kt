package com.team.todoktodok.presentation.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.team.domain.model.Support
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.model.member.MemberId.Companion.DEFAULT_MEMBER_ID
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivityProfileBinding
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.todoktodok.presentation.core.component.ReportDialog
import com.team.todoktodok.presentation.core.ext.getSerializableCompat
import com.team.todoktodok.presentation.core.ext.registerReportResultListener
import com.team.todoktodok.presentation.view.discussions.DiscussionsActivity
import com.team.todoktodok.presentation.view.profile.adapter.ContentPagerAdapter
import com.team.todoktodok.presentation.view.profile.adapter.ProfileAdapter
import com.team.todoktodok.presentation.view.profile.adapter.ProfileItems
import com.team.todoktodok.presentation.view.profile.vm.ProfileViewModel
import com.team.todoktodok.presentation.view.profile.vm.ProfileViewModelFactory
import com.team.todoktodok.presentation.view.serialization.SerializationBook
import com.team.todoktodok.presentation.view.serialization.SerializationMemberDiscussion
import com.team.todoktodok.presentation.view.setting.SettingActivity

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels {
        val repositoryModule = (application as App).container.repositoryModule
        ProfileViewModelFactory(repositoryModule.memberRepository)
    }
    private lateinit var messageConverter: ExceptionMessageConverter

    private val launcher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.refreshProfile()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val memberId: Long? = intent?.getLongExtra(ARG_MEMBER_ID, DEFAULT_MEMBER_ID)
        requireNotNull(memberId) { MEMBER_ID_NOT_FOUND }

        val initialTab = intent?.getSerializableCompat<UserProfileTab>(ARG_INITIAL_TAB) ?: UserProfileTab.ACTIVATED_BOOKS

        messageConverter = ExceptionMessageConverter()

        viewModel.setMemberId(memberId)
        viewModel.initState()

        setUpUiState(initialTab)
        setUpUiEvent()
        setUpSystemBar()
        setUpDialogResultListener()
    }

    private fun setUpSystemBar() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setUpDialogResultListener() {
        supportFragmentManager.setFragmentResultListener(
            SupportMemberDialog.REQUEST_KEY_SUPPORT,
            this@ProfileActivity,
        ) { _, bundle ->
            val result =
                bundle.getSerializableCompat<Support>(SupportMemberDialog.RESULT_KEY_SUPPORT)
            viewModel.supportMember(result)
        }
        supportFragmentManager.registerReportResultListener(
            this,
            USER_REPORT_DIALOG_REQUEST_KEY,
            ReportDialog.RESULT_KEY_REPORT,
        ) { reportReason ->
            viewModel.supportMember(Support.REPORT)
        }
    }

    private fun setUpUiState(initialTab: UserProfileTab) {
        viewModel.uiState.observe(this) { value ->
            initView(
                binding,
                value.items,
                value.activatedBooks,
                value.createdDiscussions,
                value.participatedDiscussions,
                initialTab,
            )
            setupLoading(value.isLoading)
        }
    }

    private fun setupLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.show()
        } else {
            binding.progressBar.hide()
        }
    }

    private fun initView(
        binding: ActivityProfileBinding,
        profileItems: List<ProfileItems>,
        activatedBooks: List<SerializationBook>,
        createdDiscussions: List<SerializationMemberDiscussion>,
        participatedDiscussions: List<SerializationMemberDiscussion>,
        initialTab: UserProfileTab,
    ) {
        val viewPagerAdapter =
            ContentPagerAdapter(
                activatedBooks,
                createdDiscussions,
                participatedDiscussions,
                supportFragmentManager,
                lifecycle,
            )
        val profileAdapter = ProfileAdapter(profileAdapterHandler, viewPagerAdapter, initialTab)
        binding.rvProfile.adapter = profileAdapter
        profileAdapter.submitList(profileItems)
    }

    private fun setUpUiEvent() {
        viewModel.uiEvent.observe(this) { event ->
            when (event) {
                is ProfileUiEvent.OnCompleteSupport -> {
                    showSupportCompeteMessage(event.type)
                }

                is ProfileUiEvent.ShowErrorMessage -> {
                    showErrorMessage(event.exceptions)
                }
            }
        }
    }

    private fun showSupportCompeteMessage(type: Support) {
        val messageResource =
            when (type) {
                Support.BLOCK -> R.string.profile_complete_block
                Support.REPORT -> R.string.profile_complete_report
            }
        AlertSnackBar(binding.root, messageResource).show()
    }

    private fun showErrorMessage(exception: TodokTodokExceptions) {
        AlertSnackBar(binding.root, messageConverter(exception)).show()
    }

    private val profileAdapterHandler =
        object : ProfileAdapter.Handler {
            override fun onClickSetting() {
                launcher.launch(SettingActivity.Intent(this@ProfileActivity))
            }

            override fun onClickBack() {
                if (viewModel.uiState.value?.isMyProfilePage == true) {
                    finish()
                } else {
                    startActivity(DiscussionsActivity.Intent(this@ProfileActivity))
                    finish()
                }
            }

            override fun onClickProfileImage() {
                // 이미지 수정 기능 추가
            }

            override fun onClickSupport(type: Support) {
                when (type) {
                    Support.BLOCK -> {
                        val dialog = SupportMemberDialog.newInstance(type)
                        dialog.show(supportFragmentManager, SupportMemberDialog.TAG)
                    }

                    Support.REPORT -> {
                        val dialog = ReportDialog.newInstance(USER_REPORT_DIALOG_REQUEST_KEY)
                        dialog.show(supportFragmentManager, ReportDialog.TAG)
                    }
                }
            }
        }

    companion object {
        fun Intent(
            context: Context,
            memberId: Long? = null,
            initialTab: UserProfileTab? = null,
        ): Intent =
            Intent(context, ProfileActivity::class.java).apply {
                memberId?.let { putExtra(ARG_MEMBER_ID, it) }
                initialTab?.let { putExtra(ARG_INITIAL_TAB, it) }
            }

        const val ARG_MEMBER_ID = "member_id"
        const val ARG_INITIAL_TAB = "initial_tab"
        const val MEMBER_ID_NOT_FOUND = "멤버 아이디가 존재하지 않습니다"

        private const val USER_REPORT_DIALOG_REQUEST_KEY = "comment_report_dialog_request_key"
    }
}
