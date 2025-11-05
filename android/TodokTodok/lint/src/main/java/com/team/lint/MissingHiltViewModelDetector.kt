package com.team.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement

/**
 * `ViewModel` 클래스에 `@HiltViewModel` 및 `@Inject` 생성자가 모두 존재하는지 검사합니다.
 *
 * - `ViewModel` 클래스 이름이 [VIEWMODEL_SUFFIX]로 끝나면 대상이 됩니다.
 * - `@HiltViewModel` 어노테이션이 없거나,
 *   기본 생성자(primary constructor)에 `@Inject` 어노테이션이 없을 경우
 *   Lint 오류를 발생시킵니다.
 *
 * 이 규칙은 Hilt 기반의 ViewModel 주입 규칙을 강제하기 위한 것으로,
 * ViewModel이 의존성 주입을 올바르게 설정했는지 확인합니다.
 */
class MissingHiltViewModelDetector :
    Detector(),
    Detector.UastScanner {
    /**
     * 검사할 UAST 타입을 지정합니다.
     * 여기서는 클래스(`UClass`)만 검사 대상입니다.
     */
    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(UClass::class.java)

    /**
     * 실제 UAST 노드(`UClass`)를 방문하며 Lint 검사를 수행하는 핸들러를 생성합니다.
     *
     * @param context Lint 분석 중 제공되는 [JavaContext]
     */
    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            /**
             * 클래스 단위로 방문하며, ViewModel 규칙 위반 여부를 검사합니다.
             *
             * - 클래스 이름이 [VIEWMODEL_SUFFIX]로 끝나는 경우만 검사합니다.
             * - [HILT_VIEWMODEL] 또는 [INJECT_ANNOTATION] 생성자가 없을 경우 Issue를 리포트합니다.
             *
             * @param node 검사 중인 클래스 노드
             */
            override fun visitClass(node: UClass) {
                if (!isViewModel(node) || shouldSkipClass(node)) return

                val hasHiltViewModelAnnotation = hasHiltViewModelAnnotation(node)
                val hasInjectAtPrimaryConstructor = hasInjectAnnotationAtPrimaryConstructor(node)

                if (!hasHiltViewModelAnnotation || !hasInjectAtPrimaryConstructor) {
                    context.report(
                        ISSUE_MISSING_HILT_VIEWMODEL,
                        node,
                        context.getNameLocation(node),
                        String.format(
                            ERROR_MESSAGE_TEMPLATE,
                            node.qualifiedName,
                        ),
                    )
                }
            }
        }
    }

    /**
     * 클래스 이름이 [VIEWMODEL_SUFFIX]로 끝나는지 확인합니다.
     *
     * @param node 검사 중인 클래스
     * @return ViewModel 클래스 여부
     */
    private fun isViewModel(node: UClass): Boolean = node.name?.endsWith(VIEWMODEL_SUFFIX) == true

    /**
     * 특정 클래스를 검사 대상에서 제외할 조건을 정의합니다.
     *
     * - 어노테이션 클래스(`annotation class`)
     * - [ANDROIDX_VIEWMODEL_CLASS] 자체
     * - [BASE_VIEWMODEL_CLASS] 자체
     *
     * @param node 검사 중인 클래스
     * @return 검사 제외 여부
     */
    private fun shouldSkipClass(node: UClass): Boolean {
        if (node.isAnnotationType) return true
        val qualifiedName = node.qualifiedName
        return qualifiedName == ANDROIDX_VIEWMODEL_CLASS ||
            qualifiedName == BASE_VIEWMODEL_CLASS
    }

    /**
     * 클래스에 [HILT_VIEWMODEL] 어노테이션이 존재하는지 확인합니다.
     *
     * @param node 검사 중인 클래스
     * @return 어노테이션 존재 여부
     */
    private fun hasHiltViewModelAnnotation(node: UClass): Boolean =
        node.annotations.any { annotation ->
            annotation.qualifiedName == HILT_VIEWMODEL
        }

    /**
     * 클래스의 생성자 중 [INJECT_ANNOTATION] 어노테이션이 달린 생성자가 있는지 확인합니다.
     *
     * @param node 검사 중인 클래스
     * @return `@Inject`가 달린 생성자 존재 여부
     */
    private fun hasInjectAnnotationAtPrimaryConstructor(node: UClass): Boolean =
        node.methods.any { method ->
            method.isConstructor &&
                method.uAnnotations.any {
                    it.qualifiedName == INJECT_ANNOTATION
                }
        }

    companion object {
        /**
         * Lint Issue 정의:
         * ViewModel 클래스에 `@HiltViewModel` 또는 `@Inject` 생성자가 누락된 경우 발생합니다.
         */
        val ISSUE_MISSING_HILT_VIEWMODEL: Issue =
            Issue.create(
                id = ISSUE_ID,
                briefDescription = ISSUE_BRIEF_DESCRIPTION,
                explanation = ISSUE_EXPLANATION,
                category = Category.CORRECTNESS,
                priority = ISSUE_PRIORITY,
                severity = Severity.FATAL,
                implementation =
                    Implementation(
                        MissingHiltViewModelDetector::class.java,
                        Scope.JAVA_FILE_SCOPE,
                    ),
            )

        private const val VIEWMODEL_SUFFIX = "ViewModel"
        private const val HILT_VIEWMODEL = "dagger.hilt.android.lifecycle.HiltViewModel"
        private const val INJECT_ANNOTATION = "javax.inject.Inject"
        private const val ANDROIDX_VIEWMODEL_CLASS = "androidx.lifecycle.ViewModel"
        private const val BASE_VIEWMODEL_CLASS = "com.team.todoktodok.presentation.core.base.BaseViewModel"
        private const val ISSUE_ID = "MissingHiltViewModel"
        private const val ISSUE_BRIEF_DESCRIPTION = "Hilt ViewModel 누락"
        private const val ISSUE_EXPLANATION =
            "ViewModel 클래스는 @HiltViewModel 어노테이션과 @Inject 생성자를 함께 사용해야 합니다."
        private const val ISSUE_PRIORITY = 5
        private const val ERROR_MESSAGE_TEMPLATE =
            "ViewModel 클래스에 @HiltViewModel 또는 @Inject 생성자가 누락되어 있습니다: %s"
    }
}
