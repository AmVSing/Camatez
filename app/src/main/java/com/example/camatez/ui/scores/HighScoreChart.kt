package com.example.camatez.ui.scores

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.camatez.data.scores.DailyScoreEntity
import kotlin.math.max

@Composable
fun HighScoresChart(
    scores: List<DailyScoreEntity>,
    modifier: Modifier = Modifier,
) {
    val chartColor = MaterialTheme.colorScheme.primary
    val axisColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
    val gridColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f)
    val pointColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onSurface

    val maxScore = max(1, scores.maxOfOrNull { it.score } ?: 1)
    val minScore = 0

    Box(modifier = modifier) {
        Canvas(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
        ) {
            val leftPadding = 56f
            val rightPadding = 20f
            val topPadding = 20f
            val bottomPadding = 44f

            val chartWidth = size.width - leftPadding - rightPadding
            val chartHeight = size.height - topPadding - bottomPadding

            if (chartWidth <= 0f || chartHeight <= 0f) return@Canvas

            val origin = Offset(leftPadding, size.height - bottomPadding)
            val xEnd = Offset(size.width - rightPadding, size.height - bottomPadding)
            val yEnd = Offset(leftPadding, topPadding)

            // Axes
            drawLine(
                color = axisColor,
                start = origin,
                end = xEnd,
                strokeWidth = 3f,
                cap = StrokeCap.Round,
            )
            drawLine(
                color = axisColor,
                start = origin,
                end = yEnd,
                strokeWidth = 3f,
                cap = StrokeCap.Round,
            )

            // Horizontal grid lines
            val gridLines = 4
            for (i in 0..gridLines) {
                val y = topPadding + chartHeight * (i / gridLines.toFloat())
                drawLine(
                    color = gridColor,
                    start = Offset(leftPadding, y),
                    end = Offset(size.width - rightPadding, y),
                    strokeWidth = 1.5f,
                )
            }

            if (scores.isEmpty()) return@Canvas

            val points =
                scores.mapIndexed { index, score ->
                    val x =
                        if (scores.size == 1) {
                            leftPadding + chartWidth / 2f
                        } else {
                            leftPadding + (index.toFloat() / (scores.lastIndex.toFloat())) * chartWidth
                        }

                    val fraction = (score.score - minScore).toFloat() / (maxScore - minScore).toFloat()
                    val y = origin.y - fraction * chartHeight

                    Offset(x, y)
                }

            val path =
                Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        lineTo(points[i].x, points[i].y)
                    }
                }

            drawPath(
                path = path,
                color = chartColor,
                style = Stroke(width = 4f, cap = StrokeCap.Round),
            )

            points.forEach { point ->
                drawCircle(
                    color = pointColor,
                    radius = 7f,
                    center = point,
                )
            }
        }

        // Y-axis labels
        ColumnLabels(
            minScore = minScore,
            maxScore = maxScore,
            modifier =
                Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 4.dp, top = 20.dp, bottom = 44.dp),
            textColor = textColor,
        )

        // X-axis labels
        if (scores.isNotEmpty()) {
            Row(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(start = 56.dp, end = 20.dp, bottom = 8.dp),
            ) {
                Text(
                    text = scores.first().date,
                    color = textColor,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f),
                )
                if (scores.size > 2) {
                    Text(
                        text = scores[scores.size / 2].date,
                        color = textColor,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f),
                    )
                }
                Text(
                    text = scores.last().date,
                    color = textColor,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun ColumnLabels(
    minScore: Int,
    maxScore: Int,
    modifier: Modifier = Modifier,
    textColor: androidx.compose.ui.graphics.Color,
) {
    androidx.compose.foundation.layout.Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = maxScore.toString(),
            color = textColor,
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = ((maxScore * 2) / 3).toString(),
            color = textColor,
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = (maxScore / 3).toString(),
            color = textColor,
            style = MaterialTheme.typography.bodySmall,
        )
        Text(
            text = minScore.toString(),
            color = textColor,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
