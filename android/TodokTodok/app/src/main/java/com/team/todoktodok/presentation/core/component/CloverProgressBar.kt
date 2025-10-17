package com.team.todoktodok.presentation.core.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ViewCloverProgressBarBinding

class CloverProgressBar
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : FrameLayout(context, attrs, defStyleAttr) {
        private val binding: ViewCloverProgressBarBinding =
            ViewCloverProgressBarBinding.inflate(LayoutInflater.from(context), this, true)

        init {
            visibility = GONE
        }

        fun show() {
            clearAnimation()
            if (visibility != VISIBLE) {
                startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in))
                visibility = VISIBLE
            }
        }

        fun hide() {
            clearAnimation()
            val anim = AnimationUtils.loadAnimation(context, R.anim.fade_out)
            anim.setAnimationListener(
                object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        visibility = GONE
                    }

                    override fun onAnimationRepeat(animation: Animation?) {}
                },
            )
            startAnimation(anim)
        }
    }
