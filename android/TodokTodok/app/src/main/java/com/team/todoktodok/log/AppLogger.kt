package com.team.todoktodok.log

import com.orhanobut.logger.Logger
import com.team.todoktodok.BuildConfig
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 앱 전역에서 사용하는 로깅 유틸리티 객체.
 *
 * - 디버그 모드: Logger 라이브러리로 로그 출력
 * - 릴리즈 모드: Timber와 Crashlytics 연동 로그 출력
 * - 로그 메시지에는 시간, 호출 파일명, 라인 번호 포함
 *
 * ## 사용법
 *
 * ### 1. 디버그 로그
 *
 * 기본 태그로 디버그 로그 출력:
 * ```kotlin
 * AppLogger.d("사용자가 로그인했습니다")
 * ```
 *
 * 커스텀 태그로 디버그 로그 출력:
 * ```kotlin
 * AppLogger.d("네트워크 응답 성공", "NetworkModule")
 * ```
 *
 * ### 2. 경고 로그
 *
 * 경고 메시지 출력:
 * ```kotlin
 * AppLogger.w("네트워크 연결이 불안정합니다")
 * ```
 *
 * ### 3. 에러 로그
 *
 * 메시지만으로 에러 로그 출력:
 * ```kotlin
 * AppLogger.e("로그인 실패")
 * ```
 *
 * 예외 객체와 함께 에러 로그 출력:
 * ```kotlin
 * try {
 *     // 오류 발생 코드
 * } catch (e: Exception) {
 *     AppLogger.e("사용자 정보 불러오기 실패", e)
 * }
 * ```
*/
object AppLogger {
    private const val RELEASE_TAG = "release_tag"
    private const val DEBUG_TAG = "modongpe"
    private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    /**
     * 현재 시간을 지정한 포맷으로 반환한다.
     *
     * @return 현재 시간 문자열 (예: 2025-08-06 13:24:15)
     */
    private fun now(): String = dateFormatter.format(Date())

    /**
     * 로그를 호출한 파일명과 라인 번호를 "FileName.kt: 123" 형식으로 반환한다.
     *
     * @return 호출 위치 문자열
     */
    private fun getCallerInfo(): String {
        val stackTrace = Throwable().stackTrace
        for (element in stackTrace) {
            if (!element.className.contains(Logger::class.java.simpleName)) {
                val file = element.fileName ?: "UnknownFile"
                val line = element.lineNumber
                return "$file: $line"
            }
        }
        return "Unknown"
    }

    /**
     * 메시지에 시간과 호출 위치 정보를 포함하여 포맷팅한다.
     *
     * @param message 원본 로그 메시지
     * @return 포맷팅된 로그 메시지
     * 출력 예제: 2025-08-06 13:24:15 [MainActivity.kt: 25] 사용자가 로그인했습니다
     */
    private fun formatMessage(message: String): String {
        val time = now()
        val location = getCallerInfo()
        return "$time [$location] $message"
    }

    /**
     * 로그 태그를 결정한다.
     *
     * - 디버그 모드: 전달받은 태그가 있으면 사용, 없으면 DEBUG_TAG 사용
     * - 릴리즈 모드: RELEASE_TAG 고정 사용
     *
     * @param tag 전달된 태그 (nullable)
     * @return 최종 로그 태그
     */
    private fun adjustTag(tag: String?): String = if (BuildConfig.DEBUG) tag ?: DEBUG_TAG else RELEASE_TAG

    /**
     * 디버그 로그를 출력한다.
     *
     * @param message 로그 메시지
     * @param tag 로그 태그 (선택). 없으면 디버그 모드에서 DEBUG_TAG 사용
     */
    fun d(
        message: String,
        tag: String? = null,
    ) {
        val formatted = formatMessage(message)
        val resolvedTag = adjustTag(tag)
        if (BuildConfig.DEBUG) {
            Logger.t(resolvedTag).d(formatted)
        } else {
            Timber.tag(resolvedTag).d(formatted)
        }
    }

    /**
     * 경고 로그를 출력한다.
     *
     * @param message 로그 메시지
     */
    fun w(message: String) {
        val formatted = formatMessage(message)
        val resolvedTag = adjustTag(null)
        if (BuildConfig.DEBUG) {
            Logger.t(resolvedTag).w(formatted)
        } else {
            Timber.tag(resolvedTag).w(formatted)
        }
    }

    /**
     * 에러 로그를 출력한다.
     *
     * @param message 로그 메시지
     * @param throwable 예외 객체 (선택)
     */
    fun e(
        message: String,
        throwable: Throwable? = null,
    ) {
        val formatted = formatMessage(message)
        val resolvedTag = adjustTag(null)
        if (BuildConfig.DEBUG) {
            Logger.t(resolvedTag).e(formatted)
        } else {
            Timber.tag(resolvedTag)
            if (throwable != null) {
                Timber.e(throwable)
            } else {
                Timber.e(formatted)
            }
        }
    }
}
