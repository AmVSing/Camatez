package com.example.camatez.data.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.camatez.domain.Operation
import com.example.camatez.domain.Settings
import com.example.camatez.domain.defaultSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsRepository(
    private val context: Context,
) {
    private companion object {
        val ADD_ENABLED = booleanPreferencesKey("add_enabled")
        val SUB_ENABLED = booleanPreferencesKey("sub_enabled")
        val MUL_ENABLED = booleanPreferencesKey("mul_enabled")
        val DIV_ENABLED = booleanPreferencesKey("div_enabled")

        val ADD_LHS_MIN = intPreferencesKey("add_lhs_min")
        val ADD_LHS_MAX = intPreferencesKey("add_lhs_max")
        val ADD_RHS_MIN = intPreferencesKey("add_rhs_min")
        val ADD_RHS_MAX = intPreferencesKey("add_rhs_max")

        val MUL_LHS_MIN = intPreferencesKey("mul_lhs_min")
        val MUL_LHS_MAX = intPreferencesKey("mul_lhs_max")
        val MUL_RHS_MIN = intPreferencesKey("mul_rhs_min")
        val MUL_RHS_MAX = intPreferencesKey("mul_rhs_max")

        val DURATION = intPreferencesKey("duration")
    }

    val settingsFlow: Flow<Settings> = context.dataStore.data.map { prefs ->
        val defaults = defaultSettings()

        val enabledOperations = buildSet {
            if (prefs[ADD_ENABLED] ?: (Operation.ADD in defaults.enabledOperations)) add(Operation.ADD)
            if (prefs[SUB_ENABLED] ?: (Operation.SUBTRACT in defaults.enabledOperations)) add(Operation.SUBTRACT)
            if (prefs[MUL_ENABLED] ?: (Operation.MULTIPLY in defaults.enabledOperations)) add(Operation.MULTIPLY)
            if (prefs[DIV_ENABLED] ?: (Operation.DIVIDE in defaults.enabledOperations)) add(Operation.DIVIDE)
        }

        Settings(
            enabledOperations = enabledOperations.ifEmpty { defaults.enabledOperations },
            addLhsRange = (prefs[ADD_LHS_MIN] ?: defaults.addLhsRange.first)..(prefs[ADD_LHS_MAX] ?: defaults.addLhsRange.last),
            addRhsRange = (prefs[ADD_RHS_MIN] ?: defaults.addRhsRange.first)..(prefs[ADD_RHS_MAX] ?: defaults.addRhsRange.last),
            mulLhsRange = (prefs[MUL_LHS_MIN] ?: defaults.mulLhsRange.first)..(prefs[MUL_LHS_MAX] ?: defaults.mulLhsRange.last),
            mulRhsRange = (prefs[MUL_RHS_MIN] ?: defaults.mulRhsRange.first)..(prefs[MUL_RHS_MAX] ?: defaults.mulRhsRange.last),
            duration = prefs[DURATION] ?: defaults.duration,
        )
    }

    suspend fun save(settings: Settings) {
        context.dataStore.edit { prefs ->
            prefs[ADD_ENABLED] = Operation.ADD in settings.enabledOperations
            prefs[SUB_ENABLED] = Operation.SUBTRACT in settings.enabledOperations
            prefs[MUL_ENABLED] = Operation.MULTIPLY in settings.enabledOperations
            prefs[DIV_ENABLED] = Operation.DIVIDE in settings.enabledOperations

            prefs[ADD_LHS_MIN] = settings.addLhsRange.first
            prefs[ADD_LHS_MAX] = settings.addLhsRange.last
            prefs[ADD_RHS_MIN] = settings.addRhsRange.first
            prefs[ADD_RHS_MAX] = settings.addRhsRange.last

            prefs[MUL_LHS_MIN] = settings.mulLhsRange.first
            prefs[MUL_LHS_MAX] = settings.mulLhsRange.last
            prefs[MUL_RHS_MIN] = settings.mulRhsRange.first
            prefs[MUL_RHS_MAX] = settings.mulRhsRange.last

            prefs[DURATION] = settings.duration
        }
    }
}