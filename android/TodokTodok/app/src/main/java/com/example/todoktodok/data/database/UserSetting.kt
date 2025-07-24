package com.example.todoktodok.data.database

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class UserSetting(
    val accessToken: String = "",
    val refreshToken: String = "",
)

object UserSettingSerializer : Serializer<UserSetting> {
    override val defaultValue: UserSetting = UserSetting()

    override suspend fun readFrom(input: InputStream): UserSetting =
        try {
            Json.decodeFromString(
                deserializer = UserSetting.serializer(),
                string = input.readBytes().decodeToString(),
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }

    override suspend fun writeTo(
        t: UserSetting,
        output: OutputStream,
    ) {
        withContext(Dispatchers.IO) {
            output.write(
                Json
                    .encodeToString(
                        serializer = UserSetting.serializer(),
                        value = t,
                    ).encodeToByteArray(),
            )
        }
    }
}
