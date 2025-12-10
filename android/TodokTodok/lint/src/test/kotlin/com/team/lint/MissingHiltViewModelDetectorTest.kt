package com.team.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestLintTask
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue

class MissingHiltViewModelDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = MissingHiltViewModelDetector()

    override fun getIssues(): List<Issue> = listOf(MissingHiltViewModelDetector.ISSUE_MISSING_HILT_VIEWMODEL)

    override fun lint(): TestLintTask =
        super
            .lint()
            .allowMissingSdk()

    fun testMissingHiltViewModel() {
        val viewModelPackage = "package com.team.todoktodok.presentation.vm"
        val viewmodelImport = "import androidx.lifecycle.ViewModel"

        lint()
            .files(
                hiltViewModelStub,
                viewModelStub,
                injectStub,
                kotlin(
                    """
                    $viewModelPackage

                    $viewmodelImport

                    class TestViewModel : ViewModel()
                    """.trimIndent(),
                ).indented(),
            ).issues(MissingHiltViewModelDetector.ISSUE_MISSING_HILT_VIEWMODEL)
            .run()
            .expect(
                """
                src/com/team/todoktodok/presentation/vm/TestViewModel.kt:5: Error: ViewModel 클래스에 @HiltViewModel 또는 @Inject 생성자가 누락되어 있습니다: com.team.todoktodok.presentation.vm.TestViewModel [MissingHiltViewModel]
                class TestViewModel : ViewModel()
                      ~~~~~~~~~~~~~
                1 error
                """.trimIndent(),
            )
    }

    fun testAutoFixBothAnnotationsMissing() {
        val viewModelPackage = "package com.team.todoktodok.presentation.vm"
        val viewmodelImport = "import androidx.lifecycle.ViewModel"

        lint()
            .files(
                hiltViewModelStub,
                viewModelStub,
                injectStub,
                kotlin(
                    """
                    $viewModelPackage

                    $viewmodelImport

                    class TestViewModel constructor() : ViewModel()
                    """.trimIndent(),
                ).indented(),
            ).issues(MissingHiltViewModelDetector.ISSUE_MISSING_HILT_VIEWMODEL)
            .run()
            .expect(
                """
                src/com/team/todoktodok/presentation/vm/TestViewModel.kt:5: Error: ViewModel 클래스에 @HiltViewModel 또는 @Inject 생성자가 누락되어 있습니다: com.team.todoktodok.presentation.vm.TestViewModel [MissingHiltViewModel]
                class TestViewModel constructor() : ViewModel()
                      ~~~~~~~~~~~~~
                1 error
                """.trimIndent(),
            ).checkFix(
                null,
                kotlin(
                    """
                    $viewModelPackage

                    $viewmodelImport
                    import javax.inject.Inject
                    import dagger.hilt.android.lifecycle.HiltViewModel

                    @HiltViewModel
                    class TestViewModel @Inject constructor() : ViewModel()
                    """.trimIndent(),
                ).indented(),
            )
    }

    fun testAutoFixOnlyHiltViewModelMissing() {
        val viewModelPackage = "package com.team.todoktodok.presentation.vm"
        val viewmodelImport = "import androidx.lifecycle.ViewModel"
        val injectImport = "import javax.inject.Inject"

        lint()
            .files(
                hiltViewModelStub,
                viewModelStub,
                injectStub,
                kotlin(
                    """
                    $viewModelPackage

                    $viewmodelImport
                    $injectImport

                    class TestViewModel @Inject constructor() : ViewModel()
                    """.trimIndent(),
                ).indented(),
            ).issues(MissingHiltViewModelDetector.ISSUE_MISSING_HILT_VIEWMODEL)
            .run()
            .expect(
                """
                src/com/team/todoktodok/presentation/vm/TestViewModel.kt:6: Error: ViewModel 클래스에 @HiltViewModel 또는 @Inject 생성자가 누락되어 있습니다: com.team.todoktodok.presentation.vm.TestViewModel [MissingHiltViewModel]
                class TestViewModel @Inject constructor() : ViewModel()
                      ~~~~~~~~~~~~~
                1 error
                """.trimIndent(),
            ).checkFix(
                null,
                kotlin(
                    """
                    $viewModelPackage

                    $viewmodelImport
                    import dagger.hilt.android.lifecycle.HiltViewModel
                    $injectImport

                    @HiltViewModel
                    class TestViewModel @Inject constructor() : ViewModel()
                    """.trimIndent(),
                ).indented(),
            )
    }

    fun testAutoFixOnlyInjectMissing() {
        val viewModelPackage = "package com.team.todoktodok.presentation.vm"
        val viewmodelImport = "import androidx.lifecycle.ViewModel"
        val hiltViewModelImport = "import dagger.hilt.android.lifecycle.HiltViewModel"

        lint()
            .files(
                hiltViewModelStub,
                viewModelStub,
                injectStub,
                kotlin(
                    """
                    $viewModelPackage

                    $viewmodelImport
                    $hiltViewModelImport

                    @HiltViewModel
                    class TestViewModel constructor() : ViewModel()
                    """.trimIndent(),
                ).indented(),
            ).issues(MissingHiltViewModelDetector.ISSUE_MISSING_HILT_VIEWMODEL)
            .run()
            .expect(
                """
                src/com/team/todoktodok/presentation/vm/TestViewModel.kt:7: Error: ViewModel 클래스에 @HiltViewModel 또는 @Inject 생성자가 누락되어 있습니다: com.team.todoktodok.presentation.vm.TestViewModel [MissingHiltViewModel]
                class TestViewModel constructor() : ViewModel()
                      ~~~~~~~~~~~~~
                1 error
                """.trimIndent(),
            ).checkFix(
                null,
                kotlin(
                    """
                    $viewModelPackage

                    $viewmodelImport
                    $hiltViewModelImport
                    import javax.inject.Inject

                    @HiltViewModel
                    class TestViewModel @Inject constructor() : ViewModel()
                    """.trimIndent(),
                ).indented(),
            )
    }

    fun testAutoFixWithoutConstructorKeyword() {
        val viewModelPackage = "package com.team.todoktodok.presentation.vm"
        val viewmodelImport = "import androidx.lifecycle.ViewModel"

        lint()
            .files(
                hiltViewModelStub,
                viewModelStub,
                injectStub,
                kotlin(
                    """
                    $viewModelPackage

                    $viewmodelImport

                    class TestViewModel() : ViewModel()
                    """.trimIndent(),
                ).indented(),
            ).issues(MissingHiltViewModelDetector.ISSUE_MISSING_HILT_VIEWMODEL)
            .run()
            .expect(
                """
                src/com/team/todoktodok/presentation/vm/TestViewModel.kt:5: Error: ViewModel 클래스에 @HiltViewModel 또는 @Inject 생성자가 누락되어 있습니다: com.team.todoktodok.presentation.vm.TestViewModel [MissingHiltViewModel]
                class TestViewModel() : ViewModel()
                      ~~~~~~~~~~~~~
                1 error
                """.trimIndent(),
            ).checkFix(
                null,
                kotlin(
                    """
                    $viewModelPackage

                    $viewmodelImport
                    import javax.inject.Inject
                    import dagger.hilt.android.lifecycle.HiltViewModel

                    @HiltViewModel
                    class TestViewModel @Inject constructor() : ViewModel()
                    """.trimIndent(),
                ).indented(),
            )
    }

    fun testAbstractViewModelIsSkipped() {
        val viewModelPackage = "package com.team.todoktodok.presentation.vm"
        val viewmodelImport = "import androidx.lifecycle.ViewModel"

        lint()
            .files(
                hiltViewModelStub,
                viewModelStub,
                injectStub,
                kotlin(
                    """
                    $viewModelPackage

                    $viewmodelImport

                    abstract class BaseViewModel : ViewModel()
                    """.trimIndent(),
                ).indented(),
            ).issues(MissingHiltViewModelDetector.ISSUE_MISSING_HILT_VIEWMODEL)
            .run()
            .expectClean()
    }
}
