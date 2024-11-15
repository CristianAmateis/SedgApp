package org.antani.sedga

import androidx.room.*

@Dao
interface SedgaDao {
    @Query("SELECT * FROM sedghe ORDER BY timestamp DESC")
    suspend fun getAllSedghe(): List<SedgaEntity>

    @Insert
    suspend fun insertSedga(sedga: SedgaEntity)

    @Query("SELECT COUNT(*) FROM sedghe WHERE date = :date")
    suspend fun getSedgheCountForDay(date: String): Int

    @Query("SELECT COUNT(*) FROM sedghe")
    suspend fun getTotalSedghe(): Int

    @Query("SELECT AVG(duration) FROM sedghe")
    suspend fun getAverageDuration(): Long

    @Query("SELECT COUNT(*) FROM sedghe WHERE duration < 300000") // meno di 5 minuti
    suspend fun getSpeedSedghe(): Int

    @Query("SELECT COUNT(*) FROM sedghe WHERE duration > 1800000") // più di 30 minuti
    suspend fun getMarathonSedghe(): Int
}