package com.example.todoktodok.presentation.view.start.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.todoktodok.R
import com.example.todoktodok.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLoginBinding.bind(view)
    }
}
