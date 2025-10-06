package com.team.todoktodok.presentation.core.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ViewAlertSnackBarBinding

class AlertSnackBar private constructor(
    private val snackBar: Snackbar,
) {
    fun show() {
        snackBar.show()
    }

    companion object {
        fun AlertSnackBar(
            view: View,
            @StringRes messageRes: Int,
            @DrawableRes iconRes: Int = R.drawable.ic_alert,
        ): AlertSnackBar {
            val context = view.context
            val snackBar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
            val inflater = LayoutInflater.from(context)
            val binding = ViewAlertSnackBarBinding.inflate(inflater, null, false)

            (snackBar.view as? ViewGroup)?.let { parent ->
                parent.removeAllViews()
                parent.setBackgroundColor(0)
                parent.addView(binding.root, 0)
            }

            binding.tvSnackBarMessage.text = context.getString(messageRes)
            binding.ivIconSnackBar.setImageResource(iconRes)

            return AlertSnackBar(snackBar)
        }
    }
}
