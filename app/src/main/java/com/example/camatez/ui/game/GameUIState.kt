package com.example.camatez.ui.game

import com.example.camatez.domain.Question

data class GameUIState(
    val currentQuestion: Question? = null,
    val currentInput: String = "",
    val score: Int = 0,
    val timeRemaining: Int = 0,
    val isRunning: Boolean = false,
    val isFinished: Boolean = false,
    val highScore: Int = 0,
)
