package com.team.todoktodok.presentation.core.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.team.todoktodok.databinding.ViewResourceNotFoundBinding

class ResourceNotFoundView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {
    val binding = ViewResourceNotFoundBinding.inflate(LayoutInflater.from(context), this, true)

    fun show(
        title: CharSequence,
        subtitle: CharSequence? = null,
    ) {
        if (!isVisible) isVisible = true
        with(binding) {
            tvEmptyResourceTitle.text = title
            tvEmptyResourceSubtitle.isVisible = !subtitle.isNullOrBlank()
        }
    }

    fun hide() {
        visibility = GONE
    }
}
