package com.team.todoktodok.presentation.view.profile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        val viewPagerAdapter = ContentPagerAdapter(supportFragmentManager, lifecycle)
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
        }
}
