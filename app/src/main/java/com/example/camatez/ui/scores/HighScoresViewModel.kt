package com.example.camatez.ui.scores

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.camatez.data.scores.CamatezDatabase
import com.example.camatez.data.scores.ScoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HighScoresViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val scoreRepository =
        ScoreRepository(CamatezDatabase.getInstance(application).dailyScoreDao())

    private val selectedDuration = MutableStateFlow(30)

    val uiState: StateFlow<HighScoresUIState> =
        selectedDuration
            .flatMapLatest { duration ->
                scoreRepository.observeScoresForDuration(duration).map { scores ->
                    HighScoresUIState(
                        selectedDuration = duration,
                        scores = scores,
                    )
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = HighScoresUIState(),
            )

    fun setDuration(duration: Int) {
        selectedDuration.value = duration
    }
}
