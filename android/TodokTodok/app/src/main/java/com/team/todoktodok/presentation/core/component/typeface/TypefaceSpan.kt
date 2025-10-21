package com.team.todoktodok.presentation.core.component.typeface

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

class TypefaceSpan(
    private val newType: Typeface,
) : MetricAffectingSpan() {
    override fun updateDrawState(textPaint: TextPaint) {
        apply(textPaint)
    }

    override fun updateMeasureState(paint: TextPaint) {
        apply(paint)
    }

    private fun apply(paint: Paint) {
        paint.typeface = newType
    }
}
