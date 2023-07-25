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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import dev.varshakulkarni.scrollablebarchart.ChartContent
import dev.varshakulkarni.scrollablebarchart.ChartData
import dev.varshakulkarni.scrollablebarchart.SPACING_MEDIUM

@Composable
fun RTLScrollableBarChart(
    chartData: List<ChartData>,
    modifier: Modifier = Modifier,
    chartWidth: Float = with(LocalContext.current) { resources.displayMetrics.widthPixels.toFloat() },
    chartHeight: Float = with(LocalContext.current) { resources.displayMetrics.heightPixels.toFloat() },
    chartStrokeWidth: Float = 2f,
    chartColor: Color = MaterialTheme.colorScheme.onBackground,
    barColor: Color = Color.Green,
    barColorLow: Color = Color.Gray,
    barWidth: Float = 30f,
    visibleBarCount: Int = 6,
    dataTextSize: Float = 35.sp.value,
    yLineStrokeWidth: Float = 1f,
    yLinesCount: Int = 4,
    target: Number = 8,
    horizontalInset: Float = 24f,
    verticalInset: Float = 24f,
    isAnimated: Boolean = true,
) {
    val data: List<ChartData> = chartData.reversed()

    val bounds = Rect()
    val textPaint = Paint().apply {
        isAntiAlias = true
        textSize = dataTextSize
        color = chartColor.toArgb()
        textAlign = Paint.Align.CENTER
    }
    val text = target.toString()
    textPaint.getTextBounds(text, 0, text.length, bounds)

    val width = chartWidth * 8 / 9 - bounds.width()
    val height = chartHeight * 9 / 10
    val scrollInit = chartData.size.toFloat() - visibleBarCount
    val yLabelXPos = width + bounds.width() / 2 + SPACING_MEDIUM

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
        horizontalInset = horizontalInset,
        verticalInset = verticalInset,
        isAnimated = isAnimated,
        scrollInit = scrollInit,
        xPos = yLabelXPos,
        barOffset = 0f,
        yAxisXOffset = width,
        xAxisYOffset = width,
    )
}
