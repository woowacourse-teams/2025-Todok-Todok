package com.team.todoktodok.data.core.ext

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun String.toLocalDateTime(): LocalDateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

fun String.toLocalDate(): LocalDate = Instant.parse(this).atZone(ZoneId.systemDefault()).toLocalDate()
