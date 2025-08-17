package com.team.todoktodok.presentation.core.ext

import android.content.Context
import com.team.todoktodok.R
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

private val KST: ZoneId = ZoneId.of("Asia/Seoul")

fun LocalDateTime.toRelativeString(
    context: Context,
    sourceZone: ZoneId = ZoneOffset.UTC,
    targetZone: ZoneId = KST,
    now: ZonedDateTime = ZonedDateTime.now(targetZone),
    justNowThresholdSeconds: Long = 5L,
): String {
    val then = this.atZone(sourceZone).withZoneSameInstant(targetZone)

    if (then.isAfter(now)) {
        val aheadSec = Duration.between(now, then).seconds
        return if (aheadSec <= justNowThresholdSeconds) {
            context.getString(R.string.all_relative_just_now)
        } else {
            context.getString(R.string.all_relative_soon)
        }
    }

    val years = ChronoUnit.YEARS.between(then, now).toInt()
    if (years > 0) return context.getString(R.string.all_relative_years, years)

    val months = ChronoUnit.MONTHS.between(then, now).toInt()
    if (months > 0) return context.getString(R.string.all_relative_months, months)

    val weeks = ChronoUnit.WEEKS.between(then, now).toInt()
    if (weeks > 0) return context.getString(R.string.all_relative_weeks, weeks)

    val days = ChronoUnit.DAYS.between(then, now).toInt()
    if (days > 0) return context.getString(R.string.all_relative_days, days)

    val hours = ChronoUnit.HOURS.between(then, now).toInt()
    if (hours > 0) return context.getString(R.string.all_relative_hours, hours)

    val minutes = ChronoUnit.MINUTES.between(then, now).toInt()
    if (minutes > 0) return context.getString(R.string.all_relative_minutes, minutes)

    val seconds = ChronoUnit.SECONDS.between(then, now).toInt()
    return if (seconds <= justNowThresholdSeconds) {
        context.getString(R.string.all_relative_just_now)
    } else {
        context.getString(R.string.all_relative_seconds, seconds)
    }
}
