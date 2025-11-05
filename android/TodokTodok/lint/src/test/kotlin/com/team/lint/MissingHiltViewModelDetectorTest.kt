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
                src/com/team/todoktodok/presentation/vm/TestViewModel.kt:5: 
                Error: ViewModel 클래스에 @HiltViewModel 또는 @Inject 생성자가 누락되어 있습니다: com.team.todoktodok.presentation.vm.TestViewModel [MissingHiltViewModel]
                class TestViewModel : ViewModel()
                      ~~~~~~~~~~~~~
                1 errors, 0 warnings
                """.trimIndent(),
            )
    }
}
