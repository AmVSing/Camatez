package com.example.camatez.ui.setup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.camatez.domain.Config
import com.example.camatez.domain.Operation

class SetupViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SetupUIState())
    val uiState: StateFlow<SetupUIState> = _uiState.asStateFlow()

    fun setAddEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(addEnabled = enabled, errorMessage = null)
    }

    fun setSubtractEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(subtractEnabled = enabled, errorMessage = null)
    }

    fun setMultiplyEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(multiplyEnabled = enabled, errorMessage = null)
    }

    fun setDivideEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(divideEnabled = enabled, errorMessage = null)
    }

    fun setAddLhsMin(value: String) = update { it.copy(addLhsMin = value, errorMessage = null) }
    fun setAddLhsMax(value: String) = update { it.copy(addLhsMax = value, errorMessage = null) }
    fun setAddRhsMin(value: String) = update { it.copy(addRhsMin = value, errorMessage = null) }
    fun setAddRhsMax(value: String) = update { it.copy(addRhsMax = value, errorMessage = null) }

    fun setMulLhsMin(value: String) = update { it.copy(mulLhsMin = value, errorMessage = null) }
    fun setMulLhsMax(value: String) = update { it.copy(mulLhsMax = value, errorMessage = null) }
    fun setMulRhsMin(value: String) = update { it.copy(mulRhsMin = value, errorMessage = null) }
    fun setMulRhsMax(value: String) = update { it.copy(mulRhsMax = value, errorMessage = null) }

    fun setDuration(duration: Int) {
        _uiState.value = _uiState.value.copy(duration = duration, errorMessage = null)
    }

    fun buildConfigOrNull(): Config? {
        val state = _uiState.value

        val enabledOperations = buildSet {
            if (state.addEnabled) add(Operation.ADD)
            if (state.subtractEnabled) add(Operation.SUBTRACT)
            if (state.multiplyEnabled) add(Operation.MULTIPLY)
            if (state.divideEnabled) add(Operation.DIVIDE)
        }

        if (enabledOperations.isEmpty()) {
            setError("Enable at least one operation.")
            return null
        }

        val addLhsRange = parseRange(state.addLhsMin, state.addLhsMax, "Addition/Subtraction LHS") ?: return null
        val addRhsRange = parseRange(state.addRhsMin, state.addRhsMax, "Addition/Subtraction RHS") ?: return null
        val mulLhsRange = parseRange(state.mulLhsMin, state.mulLhsMax, "Multiplication/Division LHS") ?: return null
        val mulRhsRange = parseRange(state.mulRhsMin, state.mulRhsMax, "Multiplication/Division RHS") ?: return null

        return try {
            Config(
                enabledOperations = enabledOperations,
                addLhsRange = addLhsRange,
                addRhsRange = addRhsRange,
                mulLhsRange = mulLhsRange,
                mulRhsRange = mulRhsRange,
                duration = state.duration,
            )
        } catch (e: IllegalArgumentException) {
            setError(e.message ?: "Invalid configuration.")
            null
        }
    }

    private fun parseRange(minText: String, maxText: String, label: String): IntRange? {
        val min = minText.toIntOrNull()
        val max = maxText.toIntOrNull()

        if (min == null || max == null) {
            setError("$label range must contain valid integers.")
            return null
        }

        if (min > max) {
            setError("$label minimum must be less than or equal to maximum.")
            return null
        }

        return min..max
    }

    private fun setError(message: String) {
        _uiState.value = _uiState.value.copy(errorMessage = message)
    }

    private fun update(transform: (SetupUIState) -> SetupUIState) {
        _uiState.value = transform(_uiState.value)
    }
}