package com.team.todoktodok.presentation.core.ext

fun String.extractSubtitle(): String = split("-").first()

fun String.extractAuthor(): String = split("(").first().trim()
