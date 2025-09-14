package com.team.todoktodok.data.local.nofitication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.team.domain.model.member.Nickname
import com.team.domain.model.notification.Notification
import com.team.domain.model.notification.NotificationTarget
import com.team.domain.model.notification.NotificationType

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "notification_id") val notificationId: Long,
    @ColumnInfo(name = "discussion_id") val discussionId: Long,
    @ColumnInfo(name = "comment_id") val commentId: Long?,
    @ColumnInfo(name = "reply_id") val replyId: Long?,
    @ColumnInfo(name = "nickname") val nickname: String,
    @ColumnInfo(name = "discussion_title") val discussionTitle: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "type") val type: NotificationType,
    @ColumnInfo(name = "target") val target: NotificationTarget,
    @ColumnInfo(name = "received_at") val receivedAt: Long,
)

fun Notification.toEntity() =
    NotificationEntity(
        notificationId = id,
        discussionId = discussionId,
        commentId = commentId,
        replyId = replyId,
        nickname = nickname.value,
        discussionTitle = discussionTitle,
        content = content,
        type = type,
        target = target,
        receivedAt = receivedAt,
    )

fun NotificationEntity.toDomain() =
    Notification(
        id = notificationId,
        discussionId = discussionId,
        commentId = commentId,
        replyId = replyId,
        nickname = Nickname(nickname),
        discussionTitle = discussionTitle,
        content = content,
        type = type,
        target = target,
        receivedAt = receivedAt,
    )
