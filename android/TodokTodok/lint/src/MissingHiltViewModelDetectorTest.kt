package com.team.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

@Suppress("UnstableApiUsage")
class MissingHiltViewModelDetectorTest {

    private val hiltViewModelStub = LintDetectorTest.kotlin(
        """
        package dagger.hilt.android.lifecycle
        
        annotation class HiltViewModel
        """
    ).indented()

    private val viewModelStub = LintDetectorTest.kotlin(
        """
        package androidx.lifecycle
        
        open class ViewModel
        """
    ).indented()

    private val injectStub = LintDetectorTest.kotlin(
        """
        package javax.inject

        annotation class Inject
        """
    ).indented()

    @Test
    fun `Given a ViewModel without @HiltViewModel and @Inject, When the lint check is run, Then it should report an error`() {
        lint()
            .files(
                hiltViewModelStub,
                viewModelStub,
                injectStub,
                LintDetectorTest.kotlin(
                    """
                    package com.team.todoktodok.presentation.viewmodel

                    import androidx.lifecycle.ViewModel

                    class TestViewModel : ViewModel()
                    """
                ).indented()
            )
            .issues(MissingHiltViewModelDetector.ISSUE_MISSING_HILT_VIEWMODEL)
            .run()
            .expect(
                """
                src/com/team/todoktodok/presentation/viewmodel/TestViewModel.kt:5: Error: @HiltViewModel and/or @Inject constructor is missing for ViewModel class com.team.todoktodok.presentation.viewmodel.TestViewModel [MissingHiltViewModel]
                class TestViewModel : ViewModel()
                      ~~~~~~~~~~~~~
                1 errors, 0 warnings
                """
            )
    }

    @Test
    fun `Given a ViewModel with @HiltViewModel but no @Inject, When the lint check is run, Then it should report an error`() {
        lint()
            .files(
                hiltViewModelStub,
                viewModelStub,
                injectStub,
                LintDetectorTest.kotlin(
                    """
                    package com.team.todoktodok.presentation.viewmodel

                    import androidx.lifecycle.ViewModel
                    import dagger.hilt.android.lifecycle.HiltViewModel

                    @HiltViewModel
                    class TestViewModel : ViewModel()
                    """
                ).indented()
            )
            .issues(MissingHiltViewModelDetector.ISSUE_MISSING_HILT_VIEWMODEL)
            .run()
            .expect(
                """
                src/com/team/todoktodok/presentation/viewmodel/TestViewModel.kt:7: Error: @HiltViewModel and/or @Inject constructor is missing for ViewModel class com.team.todoktodok.presentation.viewmodel.TestViewModel [MissingHiltViewModel]
                class TestViewModel : ViewModel()
                      ~~~~~~~~~~~~~
                1 errors, 0 warnings
                """
            )
    }

    @Test
    fun `Given a ViewModel with @Inject but no @HiltViewModel, When the lint check is run, Then it should report an error`() {
        lint()
            .files(
                hiltViewModelStub,
                viewModelStub,
                injectStub,
                LintDetectorTest.kotlin(
                    """
                    package com.team.todoktodok.presentation.viewmodel

                    import androidx.lifecycle.ViewModel
                    import javax.inject.Inject

                    class TestViewModel @Inject constructor() : ViewModel()
                    """
                ).indented()
            )
            .issues(MissingHiltViewModelDetector.ISSUE_MISSING_HILT_VIEWMODEL)
            .run()
            .expect(
                """
                src/com/team/todoktodok/presentation/viewmodel/TestViewModel.kt:6: Error: @HiltViewModel and/or @Inject constructor is missing for ViewModel class com.team.todoktodok.presentation.viewmodel.TestViewModel [MissingHiltViewModel]
                class TestViewModel @Inject constructor() : ViewModel()
                      ~~~~~~~~~~~~~
                1 errors, 0 warnings
                """
            )
    }

    @Test
    fun `Given a ViewModel with @HiltViewModel and @Inject, When the lint check is run, Then it should pass`() {
        lint()
            .files(
                hiltViewModelStub,
                viewModelStub,
                injectStub,
                LintDetectorTest.kotlin(
                    """
                    package com.team.todoktodok.presentation.viewmodel

                    import androidx.lifecycle.ViewModel
                    import dagger.hilt.android.lifecycle.HiltViewModel
                    import javax.inject.Inject

                    @HiltViewModel
                    class TestViewModel @Inject constructor() : ViewModel()
                    """
                ).indented()
            )
            .issues(MissingHiltViewModelDetector.ISSUE_MISSING_HILT_VIEWMODEL)
            .run()
            .expectClean()
    }

    @Test
    fun `Given a class that is not a ViewModel, When the lint check is run, Then it should pass`() {
        lint()
            .files(
                hiltViewModelStub,
                viewModelStub,
                injectStub,
                LintDetectorTest.kotlin(
                    """
                    package com.team.todoktodok.presentation.viewmodel

                    class NotAViewModel
                    """
                ).indented()
            )
            .issues(MissingHiltViewModelDetector.ISSUE_MISSING_HILT_VIEWMODEL)
            .run()
            .expectClean()
    }
}
