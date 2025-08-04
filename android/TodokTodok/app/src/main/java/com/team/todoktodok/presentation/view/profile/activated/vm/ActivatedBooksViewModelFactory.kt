package com.team.todoktodok.presentation.view.profile.activated.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ActivatedBooksViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivatedBooksViewModel::class.java)) {
            return ActivatedBooksViewModel() as T
        }
        throw IllegalArgumentException()
    }
}
