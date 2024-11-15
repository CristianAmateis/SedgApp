package org.antani.sedga

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class TelegramService {
    companion object {
        private const val TOKEN = "7986247835:AAEybhYykMihnk_zp8q04ggDMffWLGy_K9E"
        private const val CHANNEL_ID = "-1002478369589"
        private const val MESSAGE = "Gigi si sta facendo un sedgone"

        private val client = OkHttpClient()
        private val JSON = "application/json; charset=utf-8".toMediaType()

        fun sendMessage(callback: (Boolean) -> Unit) {
            val json = JSONObject().apply {
                put("chat_id", CHANNEL_ID)
                put("text", MESSAGE)
            }

            val request = Request.Builder()
                .url("https://api.telegram.org/bot$TOKEN/sendMessage")
                .post(json.toString().toRequestBody(JSON))
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback(false)
                }

                override fun onResponse(call: Call, response: Response) {
                    callback(response.isSuccessful)
                }
            })
        }
    }
}