package com.example.camatez.data.scores

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DailyScoreEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class CamatezDatabase : RoomDatabase() {
    abstract fun dailyScoreDao(): DailyScoreDao

    companion object {
        @Volatile
        private var INSTANCE: CamatezDatabase? = null

        fun getInstance(context: Context): CamatezDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room
                    .databaseBuilder(
                        context.applicationContext,
                        CamatezDatabase::class.java,
                        "camatez.db",
                    ).build()
                    .also { INSTANCE = it }
            }
    }
}
