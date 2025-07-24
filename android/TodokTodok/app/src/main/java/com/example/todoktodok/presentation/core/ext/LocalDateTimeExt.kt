package com.example.todoktodok.presentation.core.ext

import android.content.Context
import androidx.annotation.StringRes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.formatWithResource(
    context: Context,
    @StringRes patternResId: Int,
    locale: Locale = Locale.KOREA,
): String {
    val pattern = context.getString(patternResId)
    val formatter = DateTimeFormatter.ofPattern(pattern, locale)
    return this.format(formatter)
}
