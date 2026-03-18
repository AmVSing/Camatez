package com.example.camatez.data.scores

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyScoreDao {
    @Query(
        """
        SELECT * FROM daily_scores
        WHERE date = :date AND duration = :duration
        LIMIT 1
        """,
    )
    suspend fun getScoreForDay(
        date: String,
        duration: Int,
    ): DailyScoreEntity?

    @Query(
        """
        SELECT * FROM daily_scores
        WHERE duration = :duration AND date < :date
        ORDER BY date DESC
        LIMIT 1
        """,
    )
    suspend fun getLatestScoreBeforeDate(
        date: String,
        duration: Int,
    ): DailyScoreEntity?

    @Query(
        """
        SELECT * FROM daily_scores
        WHERE duration = :duration
        ORDER BY date ASC
        """,
    )
    fun observeScoresForDuration(duration: Int): Flow<List<DailyScoreEntity>>

    @Query(
        """
        SELECT MAX(score) FROM daily_scores
        WHERE duration = :duration
        """,
    )
    fun observeBestScoreForDuration(duration: Int): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(score: DailyScoreEntity)
}
