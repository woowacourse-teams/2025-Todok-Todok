package com.team.todoktodok.data.local.discussion

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DiscussionRoomEntity::class, BookEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DiscussionDatabase : RoomDatabase() {
    abstract fun discussionDao(): DiscussionDao
}