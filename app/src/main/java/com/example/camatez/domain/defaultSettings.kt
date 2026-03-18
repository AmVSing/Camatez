package com.example.camatez.domain

fun defaultSettings(): Settings =
    Settings(
        enabledOperations = setOf(Operation.ADD, Operation.SUBTRACT),
        addLhsRange = 2..100,
        addRhsRange = 2..100,
        mulLhsRange = 2..12,
        mulRhsRange = 2..100,
        duration = 30,
    )
