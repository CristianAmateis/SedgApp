package org.antani.sedga

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SedgaEntity::class], version = 1)
abstract class SedgaDatabase : RoomDatabase() {
    abstract fun sedgaDao(): SedgaDao

    companion object {
        @Volatile
        private var INSTANCE: SedgaDatabase? = null

        fun getDatabase(context: Context): SedgaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SedgaDatabase::class.java,
                    "sedga_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}