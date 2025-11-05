package com.team.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest

private const val HILT_VIEWMODEL_PACKAGE = "package dagger.hilt.android.lifecycle"
private const val VIEWMODEL_PACKAGE = "package androidx.lifecycle"
private const val JAVA_INJECT_PACKAGE = "package javax.inject"

val hiltViewModelStub =
    LintDetectorTest.kotlin(
        """
        $HILT_VIEWMODEL_PACKAGE

        annotation class HiltViewModel
        """.trimIndent(),
    )

val viewModelStub =
    LintDetectorTest.kotlin(
        """
        $VIEWMODEL_PACKAGE

        open class ViewModel
        """.trimIndent(),
    )

val injectStub =
    LintDetectorTest.kotlin(
        """
        $JAVA_INJECT_PACKAGE

        annotation class Inject
        """.trimIndent(),
    )
