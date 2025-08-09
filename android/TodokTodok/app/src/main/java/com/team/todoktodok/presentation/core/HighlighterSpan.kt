package com.team.todoktodok.presentation.core

import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan

/**
 * 텍스트 일부에 배경 색상을 칠해서 하이라이팅하는 커스텀 Span 클래스
 *
 * @param highlightingColor 하이라이팅에 사용할 배경색
 */
class HighlighterSpan(
    private val highlightingColor: Int,
) : ReplacementSpan() {
    /**
     * 이 Span이 차지할 텍스트의 너비를 반환하는 메서드
     * @param paint 텍스트를 그릴 때 사용하는 페인트 객체
     * @param text 텍스트 전체
     * @param start 하이라이트 시작 인덱스
     * @param end 하이라이트 끝 인덱스
     * @param fm 폰트 메트릭 정보 (높이, ascent 등)
     * @return 이 Span이 차지하는 너비(px)
     */
    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?,
    ): Int = paint.measureText(text, start, end).toInt()

    /**
     * 실제로 텍스트를 그릴 때 호출되는 메서드
     * @param canvas 텍스트를 그릴 캔버스
     * @param text 텍스트 전체
     * @param start 하이라이트 시작 인덱스
     * @param end 하이라이트 끝 인덱스
     * @param x 텍스트가 그려질 시작 x 좌표
     * @param top 텍스트 영역의 top 위치
     * @param y 텍스트 베이스라인 y 위치 (글자가 올라가는 기준선)
     * @param bottom 텍스트 영역의 bottom 위치
     * @param paint 텍스트를 그릴 페인트 객체
     */
    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint,
    ) {
        // 하이라이트 할 텍스트 구간 너비 계산
        val width = paint.measureText(text, start, end)
        // 기존 텍스트 색상 저장
        val oldColor = paint.color

        // 페인트 색상을 하이라이팅 색으로 변경하고 배경 채우기 스타일로 설정
        paint.color = highlightingColor
        paint.style = Paint.Style.FILL

        // 텍스트 뒤 배경 사각형을 그림
        // y + paint.fontMetrics.ascent + 8 : 텍스트 상단보다 약간 아래로 내림 (윗 여백)
        // y + paint.fontMetrics.descent - 4 : 텍스트 하단보다 약간 위로 올림 (아래 여백)
        canvas.drawRect(
            x,
            y + paint.fontMetrics.ascent + 8,
            x + width,
            y + paint.fontMetrics.descent - 4,
            paint,
        )

        // 원래 텍스트 색상으로 복원
        paint.color = oldColor

        // 하이라이트된 영역 위에 원래 텍스트를 그림 (텍스트가 배경 위에 보이도록)
        canvas.drawText(text, start, end, x, y.toFloat(), paint)
    }
}
