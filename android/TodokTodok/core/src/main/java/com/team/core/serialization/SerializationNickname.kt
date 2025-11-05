package com.team.todoktodok.presentation.view.serialization

import android.os.Parcelable
import com.team.domain.model.member.Nickname
import kotlinx.parcelize.Parcelize

@Parcelize
class SerializationNickname(
    val value: String,
) : Parcelable {
    fun toDomain(): Nickname = Nickname(value)
}

fun Nickname.toSerialization(): SerializationNickname = SerializationNickname(value)
