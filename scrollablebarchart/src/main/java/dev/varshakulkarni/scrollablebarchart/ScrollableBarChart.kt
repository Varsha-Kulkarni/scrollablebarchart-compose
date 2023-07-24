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
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

const val PADDING_SMALL = 16f
const val PADDING_MEDIUM = 24f
const val DASH_PATH_ON_INTERVAL = 10f
const val DASH_PATH_OFF_INTERVAL = 4f
const val DASH_PATH_PHASE_VALUE = 0f

@Composable
fun ScrollableBarChart(
    chartData: List<ChartData>,
    modifier: Modifier = Modifier,
    chartWidth: Float = with(LocalContext.current) { resources.displayMetrics.widthPixels.toFloat() },
    chartHeight: Float = with(LocalContext.current) { resources.displayMetrics.heightPixels.toFloat() },
    chartDirection: ChartDirection = ChartDirection.LEFT_TO_RIGHT,
    chartStrokeWidth: Float = 2f,
    chartColor: Color = MaterialTheme.colorScheme.onBackground,
    barColor: Color = Color.Green,
    barColorLow: Color = Color.Gray,
    barWidth: Float = 30f,
    visibleBarCount: Int = 6,
    dataTextSize: Float = 35.sp.value,
    yLineStrokeWidth: Float = 1f,
    yLinesCount: Int = 2,
    target: Number = 8000,
    horizontalInset: Float = 40.dp.value,
    verticalInset: Float = 40.dp.value,
    isAnimated: Boolean = true,
) {
    val scrollInit: Float
    val barOffset: Float
    val yAxisXOffset: Float
    val xAxisYOffset: Float
    val data: List<ChartData>
    val hInset: Float
    val width: Float
    val height: Float
    val yLabelXPos: Float

    val bounds = Rect()
    val textPaint = Paint().apply {
        isAntiAlias = true
        textSize = dataTextSize
        color = chartColor.toArgb()
        textAlign = Paint.Align.CENTER
    }
    val text = target.toString()
    textPaint.getTextBounds(text, 0, text.length, bounds)

    if (chartDirection == ChartDirection.LEFT_TO_RIGHT) {
        hInset = horizontalInset + bounds.width()
        width = chartWidth * 8 / 9 - hInset
        height = chartHeight * 9 / 10 - verticalInset
        data = chartData
        scrollInit = 0f
        barOffset = width / visibleBarCount
        yAxisXOffset = 0f
        xAxisYOffset = width + barWidth
        yLabelXPos = 0f - bounds.width().toFloat() / 2 - PADDING_MEDIUM
    } else {
        data = chartData.reversed()
        hInset = horizontalInset
        width = chartWidth * 8 / 9 - bounds.width()
        height = chartHeight * 9 / 10 - verticalInset
        scrollInit = chartData.size.toFloat() - visibleBarCount
        barOffset = 0f
        yAxisXOffset = width
        xAxisYOffset = width
        yLabelXPos = width + bounds.width() * 3 / 4
    }

    ChartContent(
        chartData = data,
        modifier = modifier,
        chartWidth = width,
        chartHeight = height,
        chartStrokeWidth = chartStrokeWidth,
        chartColor = chartColor,
        barColor = barColor,
        barColorLow = barColorLow,
        barWidth = barWidth,
        visibleBarCount = visibleBarCount,
        dataTextSize = dataTextSize,
        yLineStrokeWidth = yLineStrokeWidth,
        yLinesCount = yLinesCount,
        target = target,
        horizontalInset = hInset,
        verticalInset = verticalInset,
        isAnimated = isAnimated,
        scrollInit = scrollInit,
        xPos = yLabelXPos,
        barOffset = barOffset,
        yAxisXOffset = yAxisXOffset,
        xAxisYOffset = xAxisYOffset,
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ChartContent(
    chartData: List<ChartData>,
    modifier: Modifier,
    chartColor: Color,
    chartWidth: Float,
    chartHeight: Float,
    chartStrokeWidth: Float,
    barColor: Color,
    barColorLow: Color,
    barWidth: Float,
    visibleBarCount: Int,
    dataTextSize: Float,
    yLineStrokeWidth: Float,
    yLinesCount: Int,
    target: Number,
    horizontalInset: Float,
    verticalInset: Float,
    isAnimated: Boolean,
    scrollInit: Float,
    xPos: Float,
    barOffset: Float,
    yAxisXOffset: Float,
    xAxisYOffset: Float
) {
    var scrollOffset by remember { mutableStateOf(scrollInit) }

    val scrollableState = ScrollableState {
        scrollOffset = if (it > 0) {
            (scrollOffset - it * visibleBarCount.toFloat() / chartWidth).coerceAtLeast(0f)
        } else {
            (scrollOffset - it * visibleBarCount.toFloat() / chartWidth).coerceAtMost(
                chartData.lastIndex.toFloat() - (visibleBarCount.toFloat() - 1)
            )
        }
        it
    }

    val visibleBars by remember {
        derivedStateOf {
            if (chartData.isNotEmpty()) {
                chartData.subList(
                    scrollOffset.roundToInt().coerceAtLeast(0),
                    (scrollOffset.roundToInt() + visibleBarCount).coerceAtMost(chartData.size)
                )
            } else {
                emptyList()
            }
        }
    }

    fun xOffset(chartData: ChartData) =
        chartWidth * visibleBars.indexOf(chartData) / visibleBarCount

    var showBarLabels by remember {
        mutableStateOf(false)
    }

    val bounds = Rect()
    val textPaint = Paint().apply {
        isAntiAlias = true
        textSize = dataTextSize
        color = chartColor.toArgb()
        textAlign = Paint.Align.CENTER
    }

    val maxY = chartData.maxWith(Comparator.comparing { it.yValue.toFloat() })
    val scaleFactor =
        if (target.toFloat() > maxY.yValue.toFloat()) (chartHeight - verticalInset) / target.toFloat() else (chartHeight - verticalInset) / maxY.yValue.toFloat()
    val yLineItem = target.toFloat() / yLinesCount
    val yLines = mutableListOf<Float>().apply {
        repeat(yLinesCount) {
            if (it >= 0) add(target.toFloat() - yLineItem * it)
        }
    }

    val animatableBar = remember { Animatable(0f) }
    val animateFactor = if (isAnimated) animatableBar.value else 1f

    LaunchedEffect(animatableBar) {
        animatableBar.animateTo(
            1f,
            animationSpec = tween(400, easing = LinearEasing)
        )
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .scrollable(scrollableState, Orientation.Horizontal)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_UP -> {
                        showBarLabels = !showBarLabels
                        false
                    }

                    else -> {
                        true
                    }
                }
            }
    ) {
        inset(horizontal = horizontalInset, vertical = verticalInset) {
            drawLine(
                color = chartColor,
                strokeWidth = chartStrokeWidth,
                start = Offset(0f, chartHeight),
                end = Offset(xAxisYOffset, chartHeight),
            )
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

            visibleBars.forEach { bar ->
                val xOffset = xOffset(bar)
                if (bar.yValue.toDouble() >= target.toDouble()) {
                    drawRoundRect(
                        color = barColor,
                        topLeft = Offset(
                            xOffset + barOffset,
                            chartHeight - bar.yValue.toFloat() * scaleFactor * animateFactor
                        ),
                        size = Size(barWidth, bar.yValue.toFloat() * scaleFactor * animateFactor),
                        cornerRadius = CornerRadius(10f, 10f)
                    )
                } else {
                    drawRoundRect(
                        color = barColorLow,
                        topLeft = Offset(
                            xOffset + barOffset,
                            chartHeight - bar.yValue.toFloat() * scaleFactor * animateFactor

                        ),
                        size = Size(barWidth, bar.yValue.toFloat() * scaleFactor * animateFactor),
                        cornerRadius = CornerRadius(10f, 10f)
                    )
                }
                if (bar.yValue != 0 && showBarLabels) {
                    drawIntoCanvas {
                        val text = bar.yValue.toString()
                        textPaint.getTextBounds(text, 0, text.length, bounds)
                        it.nativeCanvas.drawText(
                            text,
                            xOffset + barOffset + barWidth / 2,
                            chartHeight - PADDING_SMALL - bar.yValue.toFloat() * scaleFactor,
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
                        chartHeight + PADDING_MEDIUM + textHeight / 2,
                        textPaint
                    )
                }
            }
        }
    }
}
