package com.team.todoktodok.presentation.core.ext

import android.content.Context
import com.team.todoktodok.R
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
): String = toRelative(sourceZone, targetZone, now, justNowThresholdSeconds).format(context)

private fun RelativeTime.format(context: Context): String =
    when (this) {
        RelativeTime.JustNow -> context.getString(R.string.all_relative_just_now)
        is RelativeTime.Years -> context.getString(R.string.all_relative_years, value)
        is RelativeTime.Months -> context.getString(R.string.all_relative_months, value)
        is RelativeTime.Weeks -> context.getString(R.string.all_relative_weeks, value)
        is RelativeTime.Days -> context.getString(R.string.all_relative_days, value)
        is RelativeTime.Hours -> context.getString(R.string.all_relative_hours, value)
        is RelativeTime.Minutes -> context.getString(R.string.all_relative_minutes, value)
        is RelativeTime.Seconds -> context.getString(R.string.all_relative_seconds, value)
    }

private fun LocalDateTime.toRelative(
    sourceZone: ZoneId = ZoneOffset.UTC,
    targetZone: ZoneId = KST,
    now: ZonedDateTime = ZonedDateTime.now(targetZone),
    justNowThresholdSeconds: Long = 5L,
): RelativeTime {
    val then: ZonedDateTime = this.atZone(sourceZone).withZoneSameInstant(targetZone)

    if (then.isAfter(now)) {
        return RelativeTime.JustNow
    }

    ChronoUnit.YEARS
        .between(then, now)
        .toInt()
        .let { if (it > 0) return RelativeTime.Years(it) }
    ChronoUnit.MONTHS
        .between(then, now)
        .toInt()
        .let { if (it > 0) return RelativeTime.Months(it) }
    ChronoUnit.WEEKS
        .between(then, now)
        .toInt()
        .let { if (it > 0) return RelativeTime.Weeks(it) }
    ChronoUnit.DAYS
        .between(then, now)
        .toInt()
        .let { if (it > 0) return RelativeTime.Days(it) }
    ChronoUnit.HOURS
        .between(then, now)
        .toInt()
        .let { if (it > 0) return RelativeTime.Hours(it) }
    ChronoUnit.MINUTES
        .between(then, now)
        .toInt()
        .let { if (it > 0) return RelativeTime.Minutes(it) }

    val seconds = ChronoUnit.SECONDS.between(then, now).toInt()
    return if (seconds <= justNowThresholdSeconds) {
        RelativeTime.JustNow
    } else {
        RelativeTime.Seconds(
            seconds,
        )
    }
}

sealed interface RelativeTime {
    data object JustNow : RelativeTime

    @JvmInline
    value class Years(
        val value: Int,
    ) : RelativeTime

    @JvmInline
    value class Months(
        val value: Int,
    ) : RelativeTime

    @JvmInline
    value class Weeks(
        val value: Int,
    ) : RelativeTime

    @JvmInline
    value class Days(
        val value: Int,
    ) : RelativeTime

    @JvmInline
    value class Hours(
        val value: Int,
    ) : RelativeTime

    @JvmInline
    value class Minutes(
        val value: Int,
    ) : RelativeTime

    @JvmInline
    value class Seconds(
        val value: Int,
    ) : RelativeTime
}
