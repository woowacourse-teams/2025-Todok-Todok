package com.team.todoktodok.presentation.view.serialization

import android.os.Parcelable
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class SerializationUser(
    val id: Long,
    val nickname: String,
    val profileImage: String,
) : Parcelable {
    fun toDomain(): User = User(id, Nickname(nickname), profileImage)
}

fun User.toSerialization(): SerializationUser = SerializationUser(id, nickname.value, profileImage)
