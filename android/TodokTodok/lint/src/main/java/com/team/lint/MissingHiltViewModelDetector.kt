package com.team.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintFix
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UElement

/**
 * `ViewModel` 클래스에 `@HiltViewModel` 및 `@Inject` 생성자가 모두 존재하는지 검사하는 Lint Detector
 */
class MissingHiltViewModelDetector :
    Detector(),
    Detector.UastScanner {
    /** 검사 대상 타입을 지정합니다 — 여기서는 클래스(`UClass`)만 검사 */
    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(UClass::class.java)

    /**
     * 클래스 단위(`UClass`)로 방문하며 ViewModel 관련 Lint 검사 수행
     * @param context 현재 Lint 분석 컨텍스트
     */
    override fun createUastHandler(context: JavaContext): UElementHandler {
        return object : UElementHandler() {
            override fun visitClass(node: UClass) {
                if (!isViewModel(node) || shouldSkipClass(node)) return

                val hasHiltViewModelAnnotation = hasHiltViewModelAnnotation(node)
                val hasInjectAtPrimaryConstructor = hasInjectAnnotationAtPrimaryConstructor(node)

                adjustReport(
                    hasHiltViewModelAnnotation,
                    hasInjectAtPrimaryConstructor,
                    context,
                    node,
                )
            }
        }
    }

    /**
     * 클래스 이름이 "ViewModel"로 끝나는지 여부를 확인
     */
    private fun isViewModel(node: UClass): Boolean = node.name?.endsWith(VIEWMODEL_SUFFIX) == true

    /**
     * 특정 클래스를 검사 대상에서 제외할 조건을 정의
     *
     * 다음 조건을 만족하면 검사 제외
     * - 어노테이션 클래스 (`annotation class`)
     * - 추상 클래스 (abstract)
     * - androidx.lifecycle.ViewModel 자체
     * - @Suppress("MissingHiltViewModel")가 선언된 클래스
     */
    private fun shouldSkipClass(node: UClass): Boolean {
        if (node.isAnnotationType) return true
        if (isAbstract(node)) return true
        if (node.qualifiedName == ANDROIDX_VIEWMODEL_CLASS) return true
        if (hasSuppressAnnotation(node)) return true
        return false
    }

    /** 추상 클래스 여부를 확인 */
    private fun isAbstract(node: UClass): Boolean = node.hasModifierProperty(MODIFIER_ABSTRACT)

    /** `@Suppress("MissingHiltViewModel")` 어노테이션 존재 여부를 확인 */
    private fun hasSuppressAnnotation(node: UClass): Boolean = node.uAnnotations.any { it.qualifiedName == SUPPRESS_ANNOTATION }

    /** 클래스에 `@HiltViewModel` 어노테이션이 존재하는지 확인 */
    private fun hasHiltViewModelAnnotation(node: UClass): Boolean = node.annotations.any { it.qualifiedName == HILT_VIEWMODEL }

    /** 클래스 생성자 중 `@Inject` 어노테이션이 존재하는지 확인 */
    private fun hasInjectAnnotationAtPrimaryConstructor(node: UClass): Boolean =
        node.methods.any { method ->
            method.isConstructor &&
                method.uAnnotations.any { it.qualifiedName == INJECT_ANNOTATION }
        }

    /**
     * Hilt 관련 어노테이션 누락 여부를 평가하고 Lint Issue를 보고
     */
    private fun adjustReport(
        hasHiltViewModelAnnotation: Boolean,
        hasInjectAtPrimaryConstructor: Boolean,
        context: JavaContext,
        node: UClass,
    ) {
        if (!hasHiltViewModelAnnotation || !hasInjectAtPrimaryConstructor) {
            val fix =
                createQuickFix(
                    context,
                    node,
                    hasHiltViewModelAnnotation,
                    hasInjectAtPrimaryConstructor,
                )
            report(context, node, fix)
        }
    }

    /**
     * Lint Issue를 실제로 보고
     */
    private fun report(
        context: JavaContext,
        node: UClass,
        fix: LintFix?,
    ) {
        context.report(
            ISSUE_MISSING_HILT_VIEWMODEL,
            node,
            context.getNameLocation(node),
            String.format(ERROR_MESSAGE_TEMPLATE, node.qualifiedName),
            fix,
        )
    }

    /**
     * 누락된 어노테이션(`@HiltViewModel`, `@Inject`)을 자동으로 추가하기 위한 QuickFix 생성
     */
    private fun createQuickFix(
        context: JavaContext,
        node: UClass,
        hasHiltViewModel: Boolean,
        hasInject: Boolean,
    ): LintFix? {
        val fixes = mutableListOf<LintFix>()

        if (!hasHiltViewModel) addHiltFix(context, node)?.let(fixes::add)
        if (!hasInject) addInjectFix(context, node)?.let(fixes::add)

        return when {
            fixes.size > 1 ->
                fix()
                    .name(FIX_NAME_ADD_BOTH)
                    .composite(*fixes.toTypedArray())
            fixes.size == 1 -> fixes.first()
            else -> null
        }
    }

    /**
     * `@HiltViewModel` 어노테이션을 자동 추가하는 QuickFix 생성
     */
    private fun addHiltFix(
        context: JavaContext,
        node: UClass,
    ): LintFix? {
        val sourcePsi = node.sourcePsi ?: return null
        val location = context.getLocation(sourcePsi)
        return fix()
            .name(FIX_NAME_ADD_HILT)
            .replace()
            .range(location)
            .beginning()
            .with("$HILT_VIEWMODEL_ANNOTATION_TEXT\n")
            .reformat(true)
            .imports(HILT_VIEWMODEL)
            .autoFix()
            .build()
    }

    /**
     * 기본 생성자에 `@Inject` 어노테이션을 자동 추가하는 QuickFix 생성
     */
    private fun addInjectFix(
        context: JavaContext,
        node: UClass,
    ): LintFix? {
        val constructor = node.methods.firstOrNull { it.isConstructor } ?: return null
        val constructorPsi = constructor.sourcePsi ?: return null
        val location = context.getLocation(constructorPsi)
        val constructorText = constructorPsi.text

        return if (constructorText.contains(CONSTRUCTOR_KEYWORD)) {
            // 이미 constructor 키워드가 있는 경우
            fix()
                .name(FIX_NAME_ADD_INJECT)
                .replace()
                .range(location)
                .beginning()
                .with("$INJECT_ANNOTATION_TEXT ")
                .reformat(true)
                .imports(INJECT_ANNOTATION)
                .autoFix()
                .build()
        } else {
            // constructor 키워드가 없는 경우
            fix()
                .name(FIX_NAME_ADD_INJECT)
                .replace()
                .range(location)
                .beginning()
                .with(" $INJECT_ANNOTATION_TEXT $CONSTRUCTOR_KEYWORD")
                .reformat(true)
                .imports(INJECT_ANNOTATION)
                .autoFix()
                .build()
        }
    }

    companion object {
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

        // ====== 문자열 상수 ======
        private const val VIEWMODEL_SUFFIX = "ViewModel"
        private const val HILT_VIEWMODEL = "dagger.hilt.android.lifecycle.HiltViewModel"
        private const val INJECT_ANNOTATION = "javax.inject.Inject"
        private const val ANDROIDX_VIEWMODEL_CLASS = "androidx.lifecycle.ViewModel"
        private const val SUPPRESS_ANNOTATION = "kotlin.Suppress"
        private const val MODIFIER_ABSTRACT = "abstract"
        private const val CONSTRUCTOR_KEYWORD = "constructor"

        // ====== QuickFix 관련 상수 ======
        private const val FIX_NAME_ADD_HILT = "Add @HiltViewModel annotation"
        private const val FIX_NAME_ADD_INJECT = "Add @Inject constructor annotation"
        private const val FIX_NAME_ADD_BOTH = "Add missing @HiltViewModel and @Inject annotations"

        private const val HILT_VIEWMODEL_ANNOTATION_TEXT = "@HiltViewModel"
        private const val INJECT_ANNOTATION_TEXT = "@Inject"

        // ====== Issue 설명 관련 상수 ======
        private const val ISSUE_ID = "MissingHiltViewModel"
        private const val ISSUE_BRIEF_DESCRIPTION = "@HiltViewModel 누락"
        private const val ISSUE_EXPLANATION =
            "ViewModel 클래스는 @HiltViewModel 어노테이션과 @Inject 생성자를 함께 사용해야 합니다."
        private const val ISSUE_PRIORITY = 5
        private const val ERROR_MESSAGE_TEMPLATE =
            "ViewModel 클래스에 @HiltViewModel 또는 @Inject 생성자가 누락되어 있습니다: %s"
    }
}
