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
package dev.varshakulkarni.scrollablebarchart.ui.chart

import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import dev.varshakulkarni.scrollablebarchart.ChartColors
import dev.varshakulkarni.scrollablebarchart.ChartContent
import dev.varshakulkarni.scrollablebarchart.ChartData
import dev.varshakulkarni.scrollablebarchart.ChartDefaults
import dev.varshakulkarni.scrollablebarchart.ChartSize
import dev.varshakulkarni.scrollablebarchart.SPACING_LARGE
import dev.varshakulkarni.scrollablebarchart.SPACING_MEDIUM
import dev.varshakulkarni.scrollablebarchart.utils.ComposeImmutableList

@Composable
fun LTRScrollableBarChart(
    chartData: ComposeImmutableList<ChartData>,
    modifier: Modifier = Modifier,
    chartSize: ChartSize = ChartDefaults.chartSize(),
    chartStrokeWidth: Dp = ChartDefaults.ChartStrokeWidth,
    barWidth: Dp = ChartDefaults.BarWidth,
    barCornerRadius: CornerRadius = ChartDefaults.BarCornerRadius,
    yLineStrokeWidth: Dp = ChartDefaults.YLineStrokeWidth,
    verticalInset: Dp = ChartDefaults.VerticalInset,
    chartColors: ChartColors = ChartDefaults.chartColors(),
    dataTextSize: TextUnit = ChartDefaults.DataTextSize,
    target: Number = ChartDefaults.TARGET,
    yLinesCount: Int = ChartDefaults.Y_LINES_COUNT,
    visibleBarCount: Int = ChartDefaults.VISIBLE_BAR_COUNT,
    isAnimated: Boolean = ChartDefaults.ANIMATED
) {
    val bounds = Rect()
    val textPaint = Paint().apply {
        isAntiAlias = true
        textSize = dataTextSize.value
        color = chartColors.chartColor().toArgb()
        textAlign = Paint.Align.CENTER
    }
    val text = target.toString()
    textPaint.getTextBounds(text, 0, text.length, bounds)

    val width = chartSize.width().value - bounds.width()
    val height = chartSize.height().value - SPACING_LARGE
    val barOffset = width / visibleBarCount - barWidth.value
    val xAxisYOffset = width + barWidth.value
    val yLabelXPos = 0f - bounds.width() / 2 - SPACING_MEDIUM

    ChartContent(
        chartData = chartData,
        modifier = modifier,
        chartWidth = width,
        chartHeight = height,
        chartStrokeWidth = chartStrokeWidth.value,
        barWidth = barWidth.value,
        barCornerRadius = barCornerRadius,
        visibleBarCount = visibleBarCount,
        dataTextSize = dataTextSize.value,
        yLineStrokeWidth = yLineStrokeWidth.value,
        verticalInset = verticalInset.value,
        yLinesCount = yLinesCount,
        chartColors = chartColors,
        target = target,
        isAnimated = isAnimated,
        scrollInit = 0f,
        xPos = yLabelXPos,
        barOffset = barOffset,
        yAxisXOffset = 0f,
        xAxisYOffset = xAxisYOffset,
    )
}
