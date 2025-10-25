package com.team.todoktodok.data.local.discussion

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DiscussionRoomEntity::class, BookEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class DiscussionDatabase : RoomDatabase() {
    abstract fun discussionDao(): DiscussionDao

    companion object {
        private const val DISCUSSION_DATABASE = "discussion"

        @Volatile
        private var instance: DiscussionDatabase? = null

        fun getInstance(context: Context): DiscussionDatabase =
            instance ?: synchronized(this) {
                val newInstance =
                    Room
                        .databaseBuilder(
                            context.applicationContext,
                            DiscussionDatabase::class.java,
                            DISCUSSION_DATABASE,
                        ).build()
                instance = newInstance
                newInstance
            }
    }
}
