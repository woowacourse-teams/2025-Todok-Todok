package com.team.todoktodok.presentation.vm

import com.team.domain.model.member.Profile
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.CoroutinesTestExtension
import com.team.todoktodok.InstantTaskExecutorExtension
import com.team.todoktodok.presentation.view.profile.vm.ProfileViewModel
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(CoroutinesTestExtension::class)
@ExtendWith(InstantTaskExecutorExtension::class)
class ProfileViewModelTest {
    private lateinit var repository: MemberRepository
    private lateinit var viewModel: ProfileViewModel

    @BeforeEach
    fun setUp() {
        repository = mockk(relaxed = true)
        viewModel = ProfileViewModel(repository)
    }

    @Test
    fun `사용자의 프로필을 불러온다`() =
        runTest {
            // given
            val response = Profile(1, "페토", "나나를 좋아하는", "")

            coEvery { repository.getProfile(any()) } returns response

            // when
            viewModel.loadProfile(1)

            // then
            val expected = Profile(1, "페토", "나나를 좋아하는", "")

            expected shouldBe response
        }
}
