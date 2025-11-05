package com.team.ui_xml.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.team.core.ExceptionMessageConverter
import com.team.core.extension.getSerializableCompat
import com.team.core.extension.registerReportResultListener
import com.team.core.navigation.MainRoute
import com.team.domain.model.Support
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.model.member.MemberId.Companion.DEFAULT_MEMBER_ID
import com.team.ui_xml.R
import com.team.ui_xml.component.AlertSnackBar.Companion.AlertSnackBar
import com.team.ui_xml.component.ReportUserDialog
import com.team.ui_xml.databinding.ActivityProfileBinding
import com.team.ui_xml.profile.adapter.ContentPagerAdapter
import com.team.ui_xml.profile.adapter.ProfileAdapter
import com.team.ui_xml.profile.vm.ProfileViewModel
import com.team.ui_xml.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.team.core.R as coreR

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var messageConverter: ExceptionMessageConverter

    @Inject
    lateinit var mainNavigation: MainRoute

    private lateinit var profileAdapter: ProfileAdapter
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

        initView(binding)
        setUpUiState()
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
            viewModel.supportMember(result, "")
        }
        supportFragmentManager.registerReportResultListener(
            this,
            USER_REPORT_DIALOG_REQUEST_KEY,
            ReportUserDialog.RESULT_KEY_REPORT_USER,
        ) { reason ->
            viewModel.supportMember(Support.REPORT, reason)
        }
    }

    private fun setUpUiState() {
        viewModel.uiState.observe(this) { value ->
            val profileItems = value.items
            profileAdapter.submitList(profileItems)
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

    private fun initView(binding: ActivityProfileBinding) {
        val initialTab =
            intent?.getSerializableCompat<UserProfileTab>(ARG_INITIAL_TAB)
                ?: UserProfileTab.ACTIVATED_BOOKS

        val memberId: Long? = intent?.getLongExtra(ARG_MEMBER_ID, DEFAULT_MEMBER_ID)
        requireNotNull(memberId) { MEMBER_ID_NOT_FOUND }

        val viewPagerAdapter = ContentPagerAdapter(supportFragmentManager, lifecycle)
        profileAdapter = ProfileAdapter(profileAdapterHandler, viewPagerAdapter, initialTab)
        binding.rvProfile.adapter = profileAdapter

        viewModel.loadProfile(memberId)
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
                Support.BLOCK -> coreR.string.all_complete_block
                Support.REPORT -> coreR.string.all_complete_report
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
                    mainNavigation.navigateToMain(this@ProfileActivity) {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    }
                } else {
                    finish()
                }
            }

            override fun onClickSupport(type: Support) {
                when (type) {
                    Support.BLOCK -> {
                        val dialog = SupportMemberDialog.newInstance(type)
                        dialog.show(supportFragmentManager, SupportMemberDialog.TAG)
                    }

                    Support.REPORT -> {
                        val dialog = ReportUserDialog.newInstance(USER_REPORT_DIALOG_REQUEST_KEY)
                        dialog.show(supportFragmentManager, ReportUserDialog.TAG)
                    }
                }
            }
        }

    override fun onResume() {
        super.onResume()
        viewModel.refreshProfile()
    }

    companion object {
        fun Intent(
            context: Context,
            memberId: Long? = null,
            initialTab: UserProfileTab = UserProfileTab.ACTIVATED_BOOKS,
        ): Intent =
            Intent(context, ProfileActivity::class.java).apply {
                memberId?.let { putExtra(ARG_MEMBER_ID, it) }
                putExtra(ARG_INITIAL_TAB, initialTab)
            }

        const val ARG_MEMBER_ID = "member_id"
        const val ARG_INITIAL_TAB = "initial_tab"
        const val MEMBER_ID_NOT_FOUND = "멤버 아이디가 존재하지 않습니다"

        private const val USER_REPORT_DIALOG_REQUEST_KEY = "comment_report_dialog_request_key"
    }
}
