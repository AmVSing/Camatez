package com.example.camatez.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.camatez.domain.Settings
import android.view.KeyEvent as AndroidKeyEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    settings: Settings,
    onQuitToMenu: () -> Unit,
    onGoToMenuAfterGame: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(settings) {
        viewModel.startGame(settings)
        focusRequester.requestFocus()
    }

    if (uiState.isFinished) {
        AlertDialog(
            onDismissRequest = onGoToMenuAfterGame,
            title = {
                Text(
                    text = "Time's up",
                    style = MaterialTheme.typography.headlineSmall,
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Score: ${uiState.score}")
                    Text("Best: ${uiState.highScore}")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.startGame(settings) },
                ) {
                    Text("Play again")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onGoToMenuAfterGame,
                ) {
                    Text("Main menu")
                }
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Camatez") },
                actions = {
                    TextButton(onClick = onQuitToMenu) {
                        Text("Quit")
                    }
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(16.dp)
                    .focusRequester(focusRequester)
                    .focusable()
                    .onPreviewKeyEvent { event ->
                        handleGameKeyEvent(
                            event = event,
                            onDigit = viewModel::pressDigit,
                            onBackspace = viewModel::backspace,
                            onClear = viewModel::clearInput,
                            onQuit = onQuitToMenu,
                        )
                    },
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Score ${uiState.score}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Text(
                    text = "Best ${uiState.highScore}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Text(
                    text = "${uiState.timeRemaining}s",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = uiState.currentQuestion?.toString() ?: "",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(72.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 0.dp,
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = uiState.currentInput,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            NumberPad(
                onDigitPressed = {
                    viewModel.pressDigit(it)
                    focusRequester.requestFocus()
                },
                onBackspacePressed = {
                    viewModel.backspace()
                    focusRequester.requestFocus()
                },
                onClearPressed = {
                    viewModel.clearInput()
                    focusRequester.requestFocus()
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private fun handleGameKeyEvent(
    event: KeyEvent,
    onDigit: (Int) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit,
    onQuit: () -> Unit,
): Boolean {
    if (event.nativeKeyEvent.action != AndroidKeyEvent.ACTION_DOWN) return false

    val unicodeChar = event.nativeKeyEvent.unicodeChar
    if (unicodeChar != 0) {
        val char = unicodeChar.toChar()
        if (char.isDigit()) {
            onDigit(char.digitToInt())
            return true
        }
    }

    return when (event.key) {
        Key.Backspace -> {
            onBackspace()
            true
        }
        Key.Delete -> {
            onClear()
            true
        }
        Key.Escape -> {
            onQuit()
            true
        }
        else -> false
    }
}
