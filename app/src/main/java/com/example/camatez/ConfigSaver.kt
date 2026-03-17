package com.example.camatez

import androidx.compose.runtime.saveable.Saver
import com.example.camatez.domain.Config
import com.example.camatez.domain.Operation

val ConfigSaver: Saver<Config?, Any> = Saver(
    save = { config ->
        if (config == null) return@Saver null

        listOf(
            config.enabledOperations.map { it.name },
            config.addLhsRange.first,
            config.addLhsRange.last,
            config.addRhsRange.first,
            config.addRhsRange.last,
            config.mulLhsRange.first,
            config.mulLhsRange.last,
            config.mulRhsRange.first,
            config.mulRhsRange.last,
            config.duration,
        )
    },
    restore = { value ->
        if (value == null) return@Saver null

        val list = value as List<*>

        @Suppress("UNCHECKED_CAST")
        val enabledOperations = (list[0] as List<String>)
            .map { Operation.valueOf(it) }
            .toSet()

        Config(
            enabledOperations = enabledOperations,
            addLhsRange = (list[1] as Int)..(list[2] as Int),
            addRhsRange = (list[3] as Int)..(list[4] as Int),
            mulLhsRange = (list[5] as Int)..(list[6] as Int),
            mulRhsRange = (list[7] as Int)..(list[8] as Int),
            duration = list[9] as Int,
        )
    },
)