package org.antani.sedga

data class SedgaEntry(
    val timestamp: Long,
    val duration: Long
) {
    fun getFormattedDate(): String {
        val date = java.util.Date(timestamp)
        return android.text.format.DateFormat.format("dd/MM/yyyy HH:mm", date).toString()
    }

    fun getFormattedDuration(): String {
        val minutes = duration / 1000 / 60
        val seconds = (duration / 1000) % 60
        return "${minutes}m ${seconds}s"
    }
}