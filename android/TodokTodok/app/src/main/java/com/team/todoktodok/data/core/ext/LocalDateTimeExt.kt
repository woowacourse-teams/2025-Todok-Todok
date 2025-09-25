package com.team.todoktodok.data.core.ext

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toLocalDateTime(): LocalDateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

fun String.toLocalDate(): LocalDate =
    LocalDateTime
        .parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"))
        .toLocalDate()
