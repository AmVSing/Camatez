package com.example.camatez.ui.setup

data class SetupUIState(
    val addEnabled: Boolean = true,
    val subtractEnabled: Boolean = true,
    val multiplyEnabled: Boolean = false,
    val divideEnabled: Boolean = false,

    val addLhsMin: String = "2",
    val addLhsMax: String = "100",
    val addRhsMin: String = "2",
    val addRhsMax: String = "100",

    val mulLhsMin: String = "2",
    val mulLhsMax: String = "12",
    val mulRhsMin: String = "2",
    val mulRhsMax: String = "100",

    val duration: Int = 30,

    val errorMessage: String? = null,
)