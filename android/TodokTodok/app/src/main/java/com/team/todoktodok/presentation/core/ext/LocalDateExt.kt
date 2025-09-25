package com.team.todoktodok.presentation.core.ext

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.formatDot(): String = this.format(DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.getDefault()))
