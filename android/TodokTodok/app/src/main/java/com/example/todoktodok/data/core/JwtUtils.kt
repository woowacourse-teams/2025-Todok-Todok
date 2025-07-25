package com.example.todoktodok.data.core

import android.util.Base64
import org.json.JSONObject

object JwtUtils {
    private const val KEY = "role"

    fun getRoleFromJwt(jwt: String): String? {
        return try {
            val parts = jwt.split(".")
            val validPartitionSize = 3
            if (parts.size != validPartitionSize) return null

            val payload = parts[1]
            val decoded = String(Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP))
            val json = JSONObject(decoded)
            json.getString(KEY)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
