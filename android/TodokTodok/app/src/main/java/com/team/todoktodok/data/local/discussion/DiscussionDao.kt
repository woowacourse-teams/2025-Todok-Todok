package com.team.todoktodok.data.local.discussion

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
interface DiscussionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBook(bookEntity: BookEntity): Long

    @Upsert
    suspend fun upsertDiscussion(discussionEntity: DiscussionRoomEntity): Long

    @Query("""
        DELETE FROM discussion
        WHERE id IN (
            SELECT id FROM discussion
            ORDER BY id DESC
            LIMIT 1
        )
    """)
    suspend fun deleteDiscussion()

    @Transaction
    suspend fun saveDiscussionWithBook(
        discussion: DiscussionRoomEntity,
        book: BookEntity,
    ) {
        val bookPk = insertBook(book)
        upsertDiscussion(discussion.copy(bookId = bookPk))
    }

    @Query("SELECT EXISTS(SELECT 1 FROM discussion LIMIT 1)")
    suspend fun hasDiscussion(): Boolean

    @Transaction
    @Query(
        """
    SELECT * FROM discussion
    ORDER BY id DESC
    LIMIT 1
""",
    )
    suspend fun getDiscussionWithBook(): DiscussionWithBook?

    @Query(
        """
    SELECT * FROM book
    WHERE id = (
        SELECT book_id FROM discussion
        ORDER BY id DESC
        LIMIT 1
    )
""",
    )
    suspend fun getBook(): BookEntity
}
