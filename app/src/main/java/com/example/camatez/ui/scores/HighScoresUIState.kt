package com.example.camatez.ui.scores

import com.example.camatez.data.scores.DailyScoreEntity

data class HighScoresUIState(
    val selectedDuration: Int = 30,
    val scores: List<DailyScoreEntity> = emptyList(),
)