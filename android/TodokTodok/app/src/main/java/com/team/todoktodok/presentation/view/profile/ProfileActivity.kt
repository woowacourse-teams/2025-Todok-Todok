package com.team.todoktodok.presentation.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.team.domain.model.Support
import com.team.domain.model.member.MemberId.Companion.INVALID_MEMBER_ID
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivityProfileBinding
import com.team.todoktodok.presentation.core.ext.getSerializableCompat
import com.team.todoktodok.presentation.view.discussions.DiscussionsActivity
import com.team.todoktodok.presentation.view.profile.adapter.ContentPagerAdapter
import com.team.todoktodok.presentation.view.profile.adapter.ProfileAdapter
import com.team.todoktodok.presentation.view.profile.vm.ProfileViewModel
import com.team.todoktodok.presentation.view.profile.vm.ProfileViewModelFactory

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val viewModel: ProfileViewModel by viewModels {
        val repositoryModule = (application as App).container.repositoryModule
        ProfileViewModelFactory(repositoryModule.memberRepository)
    }
    private lateinit var profileAdapter: ProfileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpSystemBar()
        setUpDialogResultListener()
        setUpUiEvent()
        initView(binding)
        setUpUiState()
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
    }

    private fun initView(binding: ActivityProfileBinding) {
        val memberId: Long? = intent?.getLongExtra(ARG_MEMBER_ID, INVALID_MEMBER_ID)
        requireNotNull(memberId) { MEMBER_ID_NOT_FOUND }
        viewModel.loadProfile(memberId)

        val viewPagerAdapter = ContentPagerAdapter(memberId, supportFragmentManager, lifecycle)
        profileAdapter = ProfileAdapter(profileAdapterHandler, viewPagerAdapter)

        with(binding) {
            rvProfile.adapter = profileAdapter
        }
    }

    private fun setUpUiState() {
        viewModel.uiState.observe(this) { value ->
            profileAdapter.submitList(value.items)
        }
    }

    private fun setUpUiEvent() {
        viewModel.uiEvent.observe(this) { event ->
            when (event) {
                is ProfileUiEvent.OnCompleteSupport -> {
                    val message = getString(R.string.profile_complete_support).format(event.type.name)
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val profileAdapterHandler =
        object : ProfileAdapter.Handler {
            override fun onClickSetting() {
                // 설정 화면 이동 기능 추가
            }

            override fun onClickLogo() {
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
                val dialog = SupportMemberDialog.newInstance(type)
                dialog.show(supportFragmentManager, SupportMemberDialog.TAG)
            }
        }

    companion object {
        fun Intent(
            context: Context,
            memberId: Long? = null,
        ): Intent =
            Intent(context, ProfileActivity::class.java).apply {
                memberId?.let { putExtra(ARG_MEMBER_ID, it) }
            }

        const val ARG_MEMBER_ID = "member_id"
        const val MEMBER_ID_NOT_FOUND = "멤버 아이디가 존재하지 않습니다"
    }
}
