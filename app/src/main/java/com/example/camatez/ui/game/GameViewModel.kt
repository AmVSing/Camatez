package com.example.camatez.ui.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.camatez.data.HighScores
import com.example.camatez.domain.Config
import com.example.camatez.domain.QuestionGenerator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val questionGenerator = QuestionGenerator()
    private val HighScores = HighScores(application)

    private val _uiState = MutableStateFlow(GameUIState())
    val uiState: StateFlow<GameUIState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var currentConfig: Config? = null

    init {
        viewModelScope.launch {
            HighScores.highScoreFlow.collect { highScore ->
                _uiState.value = _uiState.value.copy(highScore = highScore)
            }
        }
    }

    fun startGame(config: Config) {
        currentConfig = config
        _uiState.value = GameUIState(
            currentQuestion = questionGenerator.generate(config),
            currentInput = "",
            score = 0,
            highScore = _uiState.value.highScore,
            timeRemaining = config.duration,
            isRunning = true,
            isFinished = false,
        )
        startTimer()
    }

    fun pressDigit(digit: Int) {
        val state = _uiState.value
        if (!state.isRunning) return

        _uiState.value = state.copy(
            currentInput = state.currentInput + digit.toString(),
        )
        checkAnswer()
    }

    fun backspace() {
        val state = _uiState.value
        if (!state.isRunning || state.currentInput.isEmpty()) return

        _uiState.value = state.copy(
            currentInput = state.currentInput.dropLast(1),
        )
    }

    fun clearInput() {
        val state = _uiState.value
        if (!state.isRunning) return

        _uiState.value = state.copy(currentInput = "")
    }

    private fun checkAnswer() {
        val state = _uiState.value
        val question = state.currentQuestion ?: return
        val typed = state.currentInput.toIntOrNull() ?: return
        val config = currentConfig ?: return

        if (typed == question.ans) {
            _uiState.value = state.copy(
                score = state.score + 1,
                currentInput = "",
                currentQuestion = questionGenerator.generate(config),
            )
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.timeRemaining > 0) {
                delay(1000)
                val state = _uiState.value
                if (!state.isRunning) return@launch

                _uiState.value = state.copy(
                    timeRemaining = state.timeRemaining - 1,
                )
            }
            finishGame()
        }
    }

    private fun finishGame() {
        timerJob?.cancel()

        val finalScore = _uiState.value.score

        _uiState.value = _uiState.value.copy(
            isRunning = false,
            isFinished = true,
            highScore = maxOf(_uiState.value.highScore, finalScore),
        )

        viewModelScope.launch {
            HighScores.saveIfHigher(finalScore)
        }
    }

    fun dismissGameOver() {
        _uiState.value = _uiState.value.copy(isFinished = false)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}