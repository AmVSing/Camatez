package com.example.camatez.domain

fun defaultConfig(): Config =
    Config(
        enabledOperations = setOf(Operation.ADD, Operation.SUBTRACT),
        addLhsRange = 2..100,
        addRhsRange = 2..100,
        mulLhsRange = 2..12,
        mulRhsRange = 2..100,
        duration = 30,
    )