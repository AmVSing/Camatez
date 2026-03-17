package com.example.camatez.domain

enum class Operation {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    ;

    override fun toString(): String =
        when (this) {
            ADD -> "+"
            SUBTRACT -> "-"
            MULTIPLY -> "×"
            DIVIDE -> "÷"
        }
}
