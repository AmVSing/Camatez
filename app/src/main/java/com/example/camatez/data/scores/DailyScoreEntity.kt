package com.example.camatez.data.scores

import androidx.room.Entity

@Entity(
    tableName = "daily_scores",
    primaryKeys = ["date", "duration"],
)
data class DailyScoreEntity(
    val date: String,
    val duration: Int,
    val score: Int,
)
