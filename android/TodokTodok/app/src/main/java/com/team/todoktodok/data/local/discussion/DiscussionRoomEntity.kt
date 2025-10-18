package com.team.todoktodok.data.local.discussion

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.team.domain.model.discussionroom.DiscussionRoom

@Entity(
    tableName = "discussion",
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["id"],
            childColumns = ["book_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class DiscussionRoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val opinion: String,
    @ColumnInfo(name = "book_id") val bookId: Long,
)

fun DiscussionRoomEntity.toDomain() =
    DiscussionRoom(
        id = id,
        title = title,
        opinion = opinion,
    )
