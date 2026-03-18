package com.example.camatez.domain

data class Settings(
    val enabledOperations: Set<Operation>,
    val addLhsRange: IntRange,
    val addRhsRange: IntRange,
    val mulLhsRange: IntRange,
    val mulRhsRange: IntRange,
    val duration: Int,
) {
    init {
        require(enabledOperations.isNotEmpty()) {
            "One or more operations must be enabled!"
        }
        require(!addLhsRange.isEmpty() && !addRhsRange.isEmpty()) {
            "Addition ranges must not be empty!"
        }
        require(!mulLhsRange.isEmpty() && !mulRhsRange.isEmpty()) {
            "Multiplication ranges must not be empty!"
        }
    }
}
