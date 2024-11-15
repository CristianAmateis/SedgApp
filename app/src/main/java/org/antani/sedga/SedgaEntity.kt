package org.antani.sedga

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "sedghe")
data class SedgaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timestamp: Long,
    val duration: Long,
    val date: String = Date(timestamp).toString()
)