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

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object ChartDefaults {
    val ChartStrokeWidth = 2.dp
    val HorizontalInset = 24.dp
    val VerticalInset = 24.dp
    val BarCornerRadius: CornerRadius = CornerRadius(0f, 0f)
    val BarWidth = 30.dp
    val DataTextSize = 35.sp
    val YLineStrokeWidth = 1.dp

    const val TARGET = 8
    const val VISIBLE_BAR_COUNT = 6
    const val Y_LINES_COUNT = 4
    const val ANIMATED = true

    @Composable
    fun chartColors(
        backgroundColor: Color = MaterialTheme.colorScheme.background,
        chartColor: Color = MaterialTheme.colorScheme.onBackground,
        barColor: Color = Color.Green,
        barColorLow: Color = Color.Gray
    ): ChartColors = DefaultChartColors(
        backgroundColor = backgroundColor,
        chartColor = chartColor,
        barColor = barColor,
        barColorLow = barColorLow
    )

    @Composable
    fun chartSize(
        width: Dp = LocalConfiguration.current.screenWidthDp.dp,
        height: Dp = LocalConfiguration.current.screenWidthDp.dp
    ): ChartSize = DefaultChartSize(width = width, height = height)
}

@Stable
interface ChartColors {
    /**
     * background color
     */
    @Composable
    fun backgroundColor(): Color

    /**
     * chart color (axis colors)
     */
    @Composable
    fun chartColor(): Color

    /**
     * bar color
     */
    @Composable
    fun barColor(): Color

    /**
     * bar color if the value is less than a target set.
     */
    @Composable
    fun barColorLow(): Color
}

@Immutable
private class DefaultChartColors(
    private val backgroundColor: Color,
    private val chartColor: Color,
    private val barColor: Color,
    private val barColorLow: Color
) : ChartColors {

    @Composable
    override fun backgroundColor(): Color = backgroundColor

    @Composable
    override fun chartColor(): Color = chartColor

    @Composable
    override fun barColor(): Color = barColor

    @Composable
    override fun barColorLow(): Color = barColorLow
}

@Stable
interface ChartSize {
    /**
     * Width of the chart
     */
    @Composable
    fun width(): Dp

    /**
     * Height of the chart
     */
    @Composable
    fun height(): Dp
}

@Immutable
private class DefaultChartSize(
    private val width: Dp,
    private val height: Dp,
) : ChartSize {

    @Composable
    override fun width(): Dp = width

    @Composable
    override fun height(): Dp = height
}
