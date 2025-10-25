package com.team.todoktodok.data.local.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore

val Context.dataStore: DataStore<UserSetting> by dataStore(
    fileName = "user-setting.json",
    serializer = UserSettingSerializer,
)
