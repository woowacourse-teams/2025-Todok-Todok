package com.team.todoktodok.presentation.core.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isGone
import com.team.todoktodok.databinding.ViewResourceNotFoundBinding

class ResourceNotFoundView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : FrameLayout(context, attrs) {
        val binding = ViewResourceNotFoundBinding.inflate(LayoutInflater.from(context), this, true)

        fun show(
            title: String,
            subtitle: String,
        ) {
            if (isGone) visibility = VISIBLE

            with(binding) {
                tvEmptyResourceTitle.text = title
                tvEmptyResourceSubtitle.text = subtitle
            }
        }

        fun hide() {
            visibility = GONE
        }
    }
