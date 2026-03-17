package com.example.camatez.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumberPad(
    onDigitPressed: (Int) -> Unit,
    onBackspacePressed: () -> Unit,
    onClearPressed: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        NumberPadRow(listOf("1", "2", "3"), onDigitPressed, onBackspacePressed, onClearPressed)
        NumberPadRow(listOf("4", "5", "6"), onDigitPressed, onBackspacePressed, onClearPressed)
        NumberPadRow(listOf("7", "8", "9"), onDigitPressed, onBackspacePressed, onClearPressed)
        NumberPadRow(listOf("C", "0", "⌫"), onDigitPressed, onBackspacePressed, onClearPressed)
    }
}

@Composable
private fun NumberPadRow(
    labels: List<String>,
    onDigitPressed: (Int) -> Unit,
    onBackspacePressed: () -> Unit,
    onClearPressed: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        for (label in labels) {
            Button(
                onClick = {
                    when (label) {
                        "C" -> onClearPressed()
                        "⌫" -> onBackspacePressed()
                        else -> onDigitPressed(label.toInt())
                    }
                },
                modifier =
                    Modifier
                        .weight(1f)
                        .height(72.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
            ) {
                Text(
                    text = label,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
