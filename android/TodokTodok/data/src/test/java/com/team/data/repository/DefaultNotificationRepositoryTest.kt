package com.team.data.repository

import com.team.data.datasource.notification.NotificationLocalDataSource
import com.team.data.datasource.notification.NotificationRemoteDataSource
import com.team.data.network.response.notification.ExistNotificationResponse
import com.team.data.network.response.notification.NotificationContentResponse
import com.team.data.network.response.notification.NotificationItemResponse
import com.team.data.network.response.notification.NotificationResponse
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultNotificationRepositoryTest {
    private lateinit var notificationRemoteDataSource: NotificationRemoteDataSource
    private lateinit var notificationLocalDataSource: NotificationLocalDataSource
    private lateinit var defaultNotificationRepository: DefaultNotificationRepository

    @BeforeEach
    fun setUp() {
        notificationRemoteDataSource = mockk()
        notificationLocalDataSource = mockk()
        defaultNotificationRepository =
            DefaultNotificationRepository(
                notificationRemoteDataSource,
                notificationLocalDataSource,
            )
    }

    @Test
    fun `로컬에 토큰과 FId가 있으면 푸시 알림을 등록한다`() =
        runTest {
            // given
            val fcmToken = "test-fcm-token"
            val fId = "test-fid"
            coEvery { notificationLocalDataSource.getFcmToken() } returns fcmToken
            coEvery { notificationLocalDataSource.getFId() } returns fId
            coEvery {
                notificationRemoteDataSource.saveFcmToken(fcmToken, fId)
            } returns NetworkResult.Success(Unit)

            // when
            val result = defaultNotificationRepository.registerPushNotification()

            // then
            assertTrue(result is NetworkResult.Success)
            coVerify { notificationRemoteDataSource.saveFcmToken(fcmToken, fId) }
        }

    @Test
    fun `로컬에 토큰이 없으면 등록을 건너뛴다`() =
        runTest {
            // given
            coEvery { notificationLocalDataSource.getFcmToken() } returns null
            coEvery { notificationLocalDataSource.getFId() } returns "test-fid"

            // when
            val result = defaultNotificationRepository.registerPushNotification()

            // then
            assertTrue(result is NetworkResult.Success)
            coVerify(exactly = 0) { notificationRemoteDataSource.saveFcmToken(any(), any()) }
        }

    @Test
    fun `새로운 토큰이 기존 토큰과 다르면 등록한다`() =
        runTest {
            // given
            val oldToken = "old-token"
            val newToken = "new-token"
            val fId = "test-fid"
            coEvery { notificationLocalDataSource.getFcmToken() } returns oldToken
            coEvery { notificationLocalDataSource.getFId() } returns fId
            coEvery { notificationLocalDataSource.saveFcmToken(newToken) } returns Unit
            coEvery { notificationLocalDataSource.saveFId(fId) } returns Unit
            coEvery {
                notificationRemoteDataSource.saveFcmToken(newToken, fId)
            } returns NetworkResult.Success(Unit)

            // when
            val result = defaultNotificationRepository.registerPushNotification(newToken, fId)

            // then
            assertTrue(result is NetworkResult.Success)
            coVerify { notificationLocalDataSource.saveFcmToken(newToken) }
            coVerify { notificationRemoteDataSource.saveFcmToken(newToken, fId) }
        }

    @Test
    fun `새로운 토큰이 기존 토큰과 같으면 등록을 건너뛴다`() =
        runTest {
            // given
            val token = "same-token"
            val fId = "test-fid"
            coEvery { notificationLocalDataSource.getFcmToken() } returns token
            coEvery { notificationLocalDataSource.getFId() } returns fId

            // when
            val result = defaultNotificationRepository.registerPushNotification(token, fId)

            // then
            assertTrue(result is NetworkResult.Success)
            coVerify(exactly = 0) { notificationRemoteDataSource.saveFcmToken(any(), any()) }
        }

    @Test
    fun `알림 목록 조회 시 읽지 않은 개수와 알림 목록을 반환한다`() =
        runTest {
            // given
            val notificationResponse =
                NotificationResponse(
                    unreadCount = 3,
                    notifications =
                        listOf(
                            NotificationItemResponse(
                                data =
                                    NotificationContentResponse(
                                        notificationId = 1L,
                                        discussionId = 1L,
                                        commentId = null,
                                        replyId = null,
                                        memberNickname = "테스트유저",
                                        discussionTitle = "토론 제목",
                                        content = "새 댓글이 달렸습니다",
                                        type = "COMMENT",
                                        target = "DISCUSSION",
                                    ),
                                isRead = false,
                                createdAt = "2024-01-01T00:00:00",
                            ),
                            NotificationItemResponse(
                                data =
                                    NotificationContentResponse(
                                        notificationId = 2L,
                                        discussionId = 2L,
                                        commentId = null,
                                        replyId = null,
                                        memberNickname = "테스트유저",
                                        discussionTitle = "토론 제목",
                                        content = "좋아요를 받았습니다",
                                        type = "LIKE",
                                        target = "DISCUSSION",
                                    ),
                                isRead = false,
                                createdAt = "2024-01-01T01:00:00",
                            ),
                        ),
                )
            coEvery {
                notificationRemoteDataSource.getNotification()
            } returns NetworkResult.Success(notificationResponse)

            // when
            val result = defaultNotificationRepository.getNotifications()

            // then
            assertTrue(result is NetworkResult.Success)
            val (unreadCount, notifications) = (result as NetworkResult.Success).data
            assertEquals(3, unreadCount)
            assertEquals(2, notifications.size)
        }

    @Test
    fun `읽지 않은 알림 존재 여부를 확인한다`() =
        runTest {
            // given
            coEvery {
                notificationRemoteDataSource.getUnreadNotificationsCount()
            } returns NetworkResult.Success(ExistNotificationResponse(exist = true))

            // when
            val result = defaultNotificationRepository.getUnreadNotificationsCount()

            // then
            assertTrue(result is NetworkResult.Success)
            val hasUnread = (result as NetworkResult.Success).data
            assertTrue(hasUnread)
        }

    @Test
    fun `알림 삭제를 수행한다`() =
        runTest {
            // given
            val notificationId = 1L
            coEvery {
                notificationRemoteDataSource.deleteNotification(notificationId)
            } returns NetworkResult.Success(Unit)

            // when
            val result = defaultNotificationRepository.deleteNotification(notificationId)

            // then
            assertTrue(result is NetworkResult.Success)
            coVerify { notificationRemoteDataSource.deleteNotification(notificationId) }
        }

    @Test
    fun `알림을 읽음 처리한다`() =
        runTest {
            // given
            val notificationId = 1L
            coEvery {
                notificationRemoteDataSource.readNotification(notificationId)
            } returns NetworkResult.Success(Unit)

            // when
            val result = defaultNotificationRepository.readNotification(notificationId)

            // then
            assertTrue(result is NetworkResult.Success)
            coVerify { notificationRemoteDataSource.readNotification(notificationId) }
        }

    @Test
    fun `알림 허용 설정을 로컬에 저장한다`() =
        runTest {
            // given
            coEvery { notificationLocalDataSource.allowedNotification(false) } returns Unit

            // when
            defaultNotificationRepository.allowedNotification(false)

            // then
            coVerify { notificationLocalDataSource.allowedNotification(false) }
        }

    @Test
    fun `알림 허용 설정을 로컬에서 가져온다`() =
        runTest {
            // given
            coEvery { notificationLocalDataSource.getIsNotificationAllowed() } returns true

            // when
            val result = defaultNotificationRepository.getIsNotificationAllowed()

            // then
            assertEquals(true, result)
        }

    @Test
    fun `원격 데이터소스 실패 시 실패가 전파된다`() =
        runTest {
            // given
            coEvery {
                notificationRemoteDataSource.getNotification()
            } returns NetworkResult.Failure(TodokTodokExceptions.UnknownException(Exception("Network error")))

            // when
            val result = defaultNotificationRepository.getNotifications()

            // then
            assertTrue(result is NetworkResult.Failure)
        }
}
