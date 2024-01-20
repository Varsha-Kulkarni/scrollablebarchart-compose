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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import dev.varshakulkarni.scrollablebarchart.BarChart
import dev.varshakulkarni.scrollablebarchart.BarChartData
import dev.varshakulkarni.scrollablebarchart.BarChartDataCollection
import dev.varshakulkarni.scrollablebarchart.BarChartDefaults
import dev.varshakulkarni.scrollablebarchart.ChartColors
import dev.varshakulkarni.scrollablebarchart.ChartSize
import dev.varshakulkarni.scrollablebarchart.SPACING_LARGE
import dev.varshakulkarni.scrollablebarchart.SPACING_MEDIUM

/**
 * RTLScrollableBarChart displays a Bar chart, which can be scrollable right-to-left direction
 * which is fully customizable .
 *
 * @param barChartDataCollection [BarChartDataCollection] collection containing list of [BarChartData] with x and y axis values, x is of [Number] and y is [Any]
 * @param modifier [Modifier] used to adjust the layout or drawing content.
 * @param chartSize [ChartSize] width and height of the chart.
 * @param chartStrokeWidth [Dp] stroke width of x and y axis .
 * @param barWidth [Dp] width of each bar.
 * @param barCornerRadius [CornerRadius] defines if each bar has rounded edges.
 * @param yLineStrokeWidth [Dp] stroke width of horizontal lines drawn on y axis
 * @param chartColors [ChartColors] chart colors
 * @param dataTextSize [TextUnit] font size of the text on the chart
 * @param target [Number] the minimum target to be achieved
 * @param yLinesCount [Int] the lines to be shown of the y axis
 * @param visibleBarCount [Int] number of bars that should be visible at a time
 * @param isAnimated [Boolean] Animate the bar drawing
 */

@Composable
fun RTLScrollableBarChart(
    barChartDataCollection: BarChartDataCollection,
    modifier: Modifier = Modifier,
    chartSize: ChartSize = BarChartDefaults.chartSize(),
    chartStrokeWidth: Dp = BarChartDefaults.ChartStrokeWidth,
    barWidth: Dp = BarChartDefaults.BarWidth,
    barCornerRadius: CornerRadius = BarChartDefaults.BarCornerRadius,
    yLineStrokeWidth: Dp = BarChartDefaults.YLineStrokeWidth,
    chartColors: ChartColors = BarChartDefaults.chartColors(),
    dataTextSize: TextUnit = BarChartDefaults.DataTextSize,
    target: Number = BarChartDefaults.TARGET,
    yLinesCount: Int = BarChartDefaults.Y_LINES_COUNT,
    visibleBarCount: Int = BarChartDefaults.VISIBLE_BAR_COUNT,
    isAnimated: Boolean = BarChartDefaults.ANIMATED
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
    // scrolling should start just after the visible bars
    val scrollInit = remember {
        barChartDataCollection.barChartData.size.toFloat() - visibleBarCount
    }
    // Y labels that appear behind the Y axis
    val yLabelXPos = remember { width + bounds.width() / 2 + SPACING_MEDIUM }

    BarChart(
        barChartDataCollection = barChartDataCollection,
        modifier = modifier,
        chartWidth = width,
        chartHeight = height,
        chartStrokeWidth = chartStrokeWidth.value,
        chartColors = chartColors,
        barWidth = barWidth.value,
        barCornerRadius = barCornerRadius,
        visibleBarCount = visibleBarCount,
        dataTextSize = dataTextSize.value,
        yLineStrokeWidth = yLineStrokeWidth.value,
        yLinesCount = yLinesCount,
        target = target,
        targetSet = target != 0f,
        isAnimated = isAnimated,
        scrollOffset = scrollInit,
        yLabelXPos = yLabelXPos,
        barOffset = 0f,
        xLabelSpacing = remember {
            bounds.width() + SPACING_MEDIUM
        },
        yLabelSpacing = remember {
            bounds.height() + SPACING_MEDIUM
        },
        yAxisXPos = width,
    )
}
