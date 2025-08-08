package com.team.todoktodok.presentation.core.ext

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDate.formatDot(): String = this.format(DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.getDefault()))
