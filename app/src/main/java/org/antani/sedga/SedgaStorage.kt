package org.antani.sedga

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object SedgaStorage {
    private const val FILENAME = "sedga_history.json"

    fun saveSedga(context: Context, timestamp: Long, duration: Long) {
        val sedghe = loadSedghe(context).toMutableList()
        sedghe.add(0, SedgaEntry(timestamp, duration))
        saveSedghe(context, sedghe)
    }

    fun loadSedghe(context: Context): List<SedgaEntry> {
        try {
            val file = context.getFileStreamPath(FILENAME)
            if (!file.exists()) return emptyList()

            val json = context.openFileInput(FILENAME).bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(json)
            return List(jsonArray.length()) { i ->
                val obj = jsonArray.getJSONObject(i)
                SedgaEntry(
                    obj.getLong("timestamp"),
                    obj.getLong("duration")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    private fun saveSedghe(context: Context, sedghe: List<SedgaEntry>) {
        try {
            val jsonArray = JSONArray()
            sedghe.forEach { sedga ->
                val jsonObject = JSONObject()
                jsonObject.put("timestamp", sedga.timestamp)
                jsonObject.put("duration", sedga.duration)
                jsonArray.put(jsonObject)
            }

            context.openFileOutput(FILENAME, Context.MODE_PRIVATE).use {
                it.write(jsonArray.toString().toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}