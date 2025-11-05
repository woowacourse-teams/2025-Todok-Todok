package com.team.ui_xml.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.team.ui_xml.databinding.ViewResourceNotFoundBinding

class ResourceNotFoundView
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
    ) : FrameLayout(context, attrs, defStyleAttr) {
        val binding = ViewResourceNotFoundBinding.inflate(LayoutInflater.from(context), this, true)

        /**
         * 리소스가 없을 때 표시할 뷰를 보여줍니다.
         *
         * @param title 화면에 표시할 메인 제목 텍스트
         * @param subtitle 선택적으로 표시할 부제목 텍스트
         * @param actionTitle 액션 버튼에 표시할 텍스트 (예: "새로 만들기")
         * @param onActionClick 액션 버튼 클릭 시 실행할 동작. null이면 클릭 이벤트가 발생하지 않습니다.
         *
         */
        fun show(
            title: CharSequence,
            subtitle: CharSequence? = null,
            actionTitle: CharSequence? = null,
            onActionClick: (() -> Unit)? = null,
        ) {
            if (!isVisible) isVisible = true
            with(binding) {
                tvEmptyResourceTitle.text = title
                tvEmptyResourceSubtitle.text = subtitle

                tvAction.text = actionTitle
                tvAction.paint.isUnderlineText = true
                tvAction.setOnClickListener { onActionClick?.invoke() }
            }
        }

        fun hide() {
            visibility = GONE
        }
    }
