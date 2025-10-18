package com.team.todoktodok.presentation.core.ext

import android.view.View
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

fun EditText.clearHintOnFocus(
    parent: TextInputLayout,
    originalHint: String,
) {
    this.onFocusChangeListener =
        View.OnFocusChangeListener { _, hasFocus ->
            parent.hint = if (hasFocus) null else originalHint
        }
}
