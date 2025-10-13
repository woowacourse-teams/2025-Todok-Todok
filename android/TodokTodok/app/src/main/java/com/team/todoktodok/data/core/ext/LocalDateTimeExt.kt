package com.team.todoktodok.data.core.ext

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toLocalDateTime(): LocalDateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
