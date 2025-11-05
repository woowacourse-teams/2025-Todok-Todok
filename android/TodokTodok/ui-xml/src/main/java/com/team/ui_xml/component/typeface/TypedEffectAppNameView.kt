package com.team.ui_xml.component.typeface

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.team.ui_xml.R
import com.team.core.R as coreR

class TypedEffectAppNameView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : AppCompatTextView(context, attrs, defStyleAttr) {
        private val appName = context.getString(R.string.app_name_kr)
        private val greenColor = ContextCompat.getColor(context, R.color.green_1A)
        private val blackColor = ContextCompat.getColor(context, R.color.black_18)

        private var textIndex = 0
        private var showCursor = true
        private val handler = Handler(Looper.getMainLooper())

        init {
            text = EMPTY_CURSOR
            textSize = 50f
            gravity = Gravity.CENTER
            typeface = resources.getFont(coreR.font.bmhanna_11yrs)
            startTypingAnimation()
        }

        private fun startTypingAnimation() {
            repeatCursorBlink {
                startTyping()
            }
        }

        private fun repeatCursorBlink(onComplete: () -> Unit) {
            var count = 0
            val blink =
                object : Runnable {
                    override fun run() {
                        showCursor = !showCursor
                        text = if (showCursor) CURSOR else EMPTY_CURSOR
                        count++
                        if (count < 2) {
                            handler.postDelayed(this, 300)
                        } else {
                            onComplete()
                        }
                    }
                }
            handler.post(blink)
        }

        private fun startTyping() {
            handler.postDelayed(
                object : Runnable {
                    override fun run() {
                        if (textIndex < appName.length) {
                            setColoredText(appName.substring(0, textIndex + 1))
                            textIndex++
                            handler.postDelayed(this, TYPING_INTERVAL)
                        } else {
                            keepBlinking()
                        }
                    }
                },
                TYPING_INTERVAL,
            )
        }

        private fun keepBlinking() {
            handler.postDelayed(
                object : Runnable {
                    override fun run() {
                        showCursor = !showCursor
                        setColoredText(appName)
                        handler.postDelayed(this, CURSOR_BLINK_INTERVAL)
                    }
                },
                CURSOR_BLINK_INTERVAL,
            )
        }

        private fun setColoredText(base: String) {
            val builder = SpannableStringBuilder()
            builder.append(base)
            builder.setSpan(
                ForegroundColorSpan(greenColor),
                0,
                base.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )

            handleCursor(builder, builder.length)
            text = builder
        }

        private fun handleCursor(
            builder: SpannableStringBuilder,
            start: Int,
        ) {
            builder.append(CURSOR)
            builder.setSpan(
                ForegroundColorSpan(if (showCursor) blackColor else Color.TRANSPARENT),
                start,
                start + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            setColorTypeFace(builder, start)
        }

        private fun setColorTypeFace(
            builder: SpannableStringBuilder,
            start: Int,
        ) {
            val cursorTypeface = resources.getFont(coreR.font.pretendard_regular)
            builder.setSpan(
                TypefaceSpan(cursorTypeface),
                start,
                start + 1,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }

        override fun onDetachedFromWindow() {
            super.onDetachedFromWindow()
            handler.removeCallbacksAndMessages(null)
        }

        companion object {
            private const val CURSOR = "|"
            private const val EMPTY_CURSOR = ""
            private const val CURSOR_BLINK_INTERVAL = 500L
            private const val TYPING_INTERVAL = 100L
        }
    }
