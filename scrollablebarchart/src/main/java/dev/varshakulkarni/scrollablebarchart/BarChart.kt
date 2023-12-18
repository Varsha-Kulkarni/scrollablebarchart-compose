/*
 * Copyright 2023 Varsha Kulkarni
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.varshakulkarni.scrollablebarchart

import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp

const val SPACING_SMALL = 16f
const val SPACING_MEDIUM = 32f
const val SPACING_LARGE = 48f
const val DASH_PATH_ON_INTERVAL = 10f
const val DASH_PATH_OFF_INTERVAL = 4f
const val DASH_PATH_PHASE_VALUE = 0f

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun BarChart(
    chartDataCollection: ChartDataCollection,
    modifier: Modifier,
    chartColors: ChartColors,
    chartWidth: Float,
    chartHeight: Float,
    chartStrokeWidth: Float,
    barWidth: Float,
    barCornerRadius: CornerRadius,
    visibleBarCount: Int,
    dataTextSize: Float,
    yLineStrokeWidth: Float,
    yLinesCount: Int,
    target: Number,
    targetSet: Boolean,
    isAnimated: Boolean,
    scrollInit: Float,
    xPos: Float,
    barOffset: Float,
    yAxisXOffset: Float,
    xAxisYOffset: Float,
) {
    val state = rememberSaveableChartState(
        scrollOffset = scrollInit,
        target = target.toFloat(),
        targetSet = targetSet,
        noOfVisibleBarCount = visibleBarCount,
        yLinesCount = yLinesCount,
        barData = chartDataCollection.chartData
    )

    state.setViewSize(chartWidth, chartHeight)

    val scaleFactor = rememberSaveable(state.visibleBars) {
        state.getScaleFactor()
    }

    val yLines = rememberSaveable(state.target) {
        state.getYLines()
    }

    val animatableBar = remember { Animatable(0f) }
    val animateFactor = if (isAnimated) animatableBar.value else 1f

    val bounds = Rect()
    val textPaint = Paint().apply {
        isAntiAlias = true
        textSize = dataTextSize
        color = chartColors.chartColor().toArgb()
        textAlign = Paint.Align.CENTER
    }

    LaunchedEffect(animatableBar) {
        animatableBar.animateTo(
            1f,
            animationSpec = tween(400, easing = LinearEasing)
        )
    }

    val chartColor = chartColors.chartColor()
    val barColor = chartColors.barColor()
    val barColorLow = chartColors.barColorLow()
    val backgroundColor = chartColors.backgroundColor()

    Box(
        modifier = modifier
            .background(backgroundColor)
            .width(chartWidth.dp)
            .height(chartHeight.dp)
    ) {

        Canvas(
            modifier = modifier.fillMaxSize()
                .scrollable(state.scrollableState, Orientation.Horizontal)
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_UP -> {
                            state.showBarLabels = !state.showBarLabels
                            false
                        }

                        else -> {
                            true
                        }
                    }
                }
        ) {
            // draw x-axis line
            drawLine(
                color = chartColor,
                strokeWidth = chartStrokeWidth,
                start = Offset(0f, chartHeight),
                end = Offset(xAxisYOffset, chartHeight),
            )
            // draw y-axis line
            drawLine(
                color = chartColor,
                strokeWidth = chartStrokeWidth,
                start = Offset(yAxisXOffset, 0f),
                end = Offset(yAxisXOffset, chartHeight),
            )

            yLines.forEach { value: Float ->
                drawLine(
                    color = chartColor,
                    strokeWidth = yLineStrokeWidth,
                    start = Offset(0f, chartHeight - value * scaleFactor),
                    end = Offset(xAxisYOffset, chartHeight - value * scaleFactor),
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(DASH_PATH_ON_INTERVAL, DASH_PATH_OFF_INTERVAL),
                        phase = DASH_PATH_PHASE_VALUE
                    )
                )
                drawIntoCanvas {
                    val text = value.toString()
                    textPaint.getTextBounds(text, 0, text.length, bounds)
                    it.nativeCanvas.drawText(
                        text,
                        xPos,
                        chartHeight - value * scaleFactor + bounds.height() / 2,
                        textPaint
                    )
                }
            }

            state.visibleBars.forEach { bar ->
                val xOffset = chartWidth * state.visibleBars.indexOf(bar) / visibleBarCount
                if (bar.yValue.toDouble() >= target.toDouble()) {
                    drawRoundRect(
                        color = barColor,
                        topLeft = Offset(
                            xOffset + barOffset,
                            chartHeight - bar.yValue.toFloat() * scaleFactor * animateFactor
                        ),
                        size = Size(barWidth, bar.yValue.toFloat() * scaleFactor * animateFactor),
                        cornerRadius = barCornerRadius
                    )
                } else {
                    drawRoundRect(
                        color = barColorLow,
                        topLeft = Offset(
                            xOffset + barOffset,
                            chartHeight - bar.yValue.toFloat() * scaleFactor * animateFactor

                        ),
                        size = Size(barWidth, bar.yValue.toFloat() * scaleFactor * animateFactor),
                        cornerRadius = barCornerRadius
                    )
                }
                if (bar.yValue != 0 && state.showBarLabels) {
                    drawIntoCanvas {
                        val text = bar.yValue.toString()
                        textPaint.getTextBounds(text, 0, text.length, bounds)
                        it.nativeCanvas.drawText(
                            text,
                            xOffset + barOffset + barWidth / 2,
                            chartHeight - SPACING_SMALL - bar.yValue.toFloat() * scaleFactor,
                            textPaint
                        )
                    }
                }
                drawIntoCanvas {
                    val text = "${bar.xValue}"
                    textPaint.getTextBounds(text, 0, text.length, bounds)
                    val textHeight = bounds.height()
                    it.nativeCanvas.drawText(
                        text,
                        xOffset + barOffset + barWidth / 2,
                        chartHeight + SPACING_MEDIUM + textHeight / 2,
                        textPaint
                    )
                }
            }
        }
    }
}
