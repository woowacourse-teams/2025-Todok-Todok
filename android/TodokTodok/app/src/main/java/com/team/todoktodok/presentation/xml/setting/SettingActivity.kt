package com.team.todoktodok.presentation.xml.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ActivitySettingBinding
import com.team.todoktodok.presentation.xml.setting.manage.ManageBlockedMembersFragment
import com.team.todoktodok.presentation.xml.setting.modify.ModifyProfileFragment
import com.team.todoktodok.presentation.xml.setting.vm.SettingViewModel
import com.team.todoktodok.presentation.xml.setting.vm.SettingViewModelFactory
import com.team.todoktodok.presentation.xml.setting.withdraw.WithdrawFragment
import kotlin.getValue

class SettingActivity : AppCompatActivity() {
    private val viewModel: SettingViewModel by viewModels { SettingViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpSystemBar()
        initView(binding)
        setUpUiState(binding)
    }

    private fun setUpSystemBar() {
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initView(binding: ActivitySettingBinding) {
        setUpActionBar(binding)
    }

    private fun setUpActionBar(binding: ActivitySettingBinding) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.toolbar.setNavigationOnClickListener {
            if (viewModel.uiState.value?.screen == SettingScreen.SETTING_MAIN) {
                setResult(RESULT_OK)
                finish()
            } else {
                viewModel.changeScreen(SettingScreen.SETTING_MAIN)
            }
        }
    }

    private fun setUpUiState(binding: ActivitySettingBinding) {
        viewModel.uiState.observe(this) { value ->
            val screen = value.screen
            changeToolbarTitle(binding, screen.toolbarTitle)
            changeFragment(screen)
        }
    }

    private fun changeToolbarTitle(
        binding: ActivitySettingBinding,
        @StringRes title: Int,
    ) {
        binding.tvTitle.text = getString(title)
    }

    private fun changeFragment(screen: SettingScreen) {
        val fragment =
            when (screen) {
                SettingScreen.SETTING_MAIN -> SettingFragment()
                SettingScreen.MODIFY_PROFILE -> ModifyProfileFragment()
                SettingScreen.MANAGE_BLOCKED_USERS -> ManageBlockedMembersFragment()
                SettingScreen.WITHDRAW -> WithdrawFragment.newInstance()
            }

        supportFragmentManager.commit {
            replace(R.id.fcv_setting, fragment)
            addToBackStack(null)
        }
    }

    companion object {
        fun Intent(context: Context): Intent = Intent(context, SettingActivity::class.java)
    }
}
