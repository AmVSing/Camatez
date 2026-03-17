package com.example.camatez.data.scores

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScoreRepository(
    private val dao: DailyScoreDao,
) {
    fun observeScoresForDuration(duration: Int): Flow<List<DailyScoreEntity>> =
        dao.observeScoresForDuration(duration)

    fun observeBestScoreForDuration(duration: Int): Flow<Int> =
        dao.observeBestScoreForDuration(duration).map { it ?: 0 }

    suspend fun recordTodayScore(
        duration: Int,
        score: Int,
    ) {
        val today = currentDateString()

        val todayEntry = dao.getScoreForDay(today, duration)
        if (todayEntry != null) {
            dao.insertOrReplace(
                todayEntry.copy(score = maxOf(todayEntry.score, score)),
            )
            return
        }

        val previousScore = dao.getLatestScoreBeforeDate(today, duration)?.score ?: 0
        val scoreToStore = maxOf(previousScore, score)

        dao.insertOrReplace(
            DailyScoreEntity(
                date = today,
                duration = duration,
                score = scoreToStore,
            ),
        )
    }

    private fun currentDateString(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return formatter.format(Calendar.getInstance().time)
    }
}