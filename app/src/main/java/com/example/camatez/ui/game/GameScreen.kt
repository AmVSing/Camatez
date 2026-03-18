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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.camatez.domain.Settings

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
        GameOverScreen(
            score = uiState.score,
            best = uiState.highScore,
            duration = settings.duration,
            onPlayAgain = {
                viewModel.startGame(settings)
            },
            onMainMenu = onGoToMenuAfterGame,
        )
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
            ) {
                TextButton(onClick = onQuitToMenu) {
                    Text("Quit")
                }
            }

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
                modifier = Modifier
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

private fun handleGameKeyEvent(
    event: KeyEvent,
    onDigit: (Int) -> Unit,
    onBackspace: () -> Unit,
    onClear: () -> Unit,
    onQuit: () -> Unit,
): Boolean {
    if (event.type != KeyEventType.KeyDown) return false

    return when (event.key) {
        Key.Zero, Key.NumPad0 -> {
            onDigit(0)
            true
        }
        Key.One, Key.NumPad1 -> {
            onDigit(1)
            true
        }
        Key.Two, Key.NumPad2 -> {
            onDigit(2)
            true
        }
        Key.Three, Key.NumPad3 -> {
            onDigit(3)
            true
        }
        Key.Four, Key.NumPad4 -> {
            onDigit(4)
            true
        }
        Key.Five, Key.NumPad5 -> {
            onDigit(5)
            true
        }
        Key.Six, Key.NumPad6 -> {
            onDigit(6)
            true
        }
        Key.Seven, Key.NumPad7 -> {
            onDigit(7)
            true
        }
        Key.Eight, Key.NumPad8 -> {
            onDigit(8)
            true
        }
        Key.Nine, Key.NumPad9 -> {
            onDigit(9)
            true
        }
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