package com.team.data.network.response.discussion

import com.team.domain.model.Note
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NoteResponse(
    @SerialName("book")
    val book: BookResponse,
    @SerialName("memo")
    val memo: String,
    @SerialName("noteId")
    val noteId: Long,
    @SerialName("snap")
    val snap: String,
)

fun NoteResponse.toDomain() = Note(id = noteId, snap = snap, memo = memo, book = book.toDomain())
