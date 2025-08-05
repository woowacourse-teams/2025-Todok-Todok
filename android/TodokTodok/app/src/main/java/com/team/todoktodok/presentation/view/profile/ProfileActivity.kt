package com.team.todoktodok.presentation.view.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.team.domain.model.Support
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivityProfileBinding
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
        initView(binding)
        setUpUiState()
        setUpUiEvent()
    }

    private fun setUpSystemBar() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initView(binding: ActivityProfileBinding) {
        val memberId: String? = intent?.getStringExtra(ARG_MEMBER_ID)
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
        viewModel.uiEvent.observe(this) { value ->
            when (value) {
                ProfileUiEvent.OnCompleteSupport -> {
                    Log.d("ProfileActivity", "OnCompleteSupport")
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
                startActivity(DiscussionsActivity.Intent(this@ProfileActivity))
                finish()
            }

            override fun onClickProfileImage() {
                // 이미지 수정 기능 추가
            }

            override fun onClickSupport(type: Support) {
                viewModel.supportMember(type)
            }
        }

    companion object {
        fun Intent(
            context: Context,
            memberId: String? = null,
        ): Intent =
            Intent(context, ProfileActivity::class.java).apply {
                memberId?.let { putExtra(ARG_MEMBER_ID, it) }
            }

        const val ARG_MEMBER_ID = "member_id"
    }
}
