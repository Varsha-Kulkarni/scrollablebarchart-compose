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

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlin.math.roundToInt

@Composable
fun rememberSaveableChartState(
    noOfVisibleBarCount: Int,
    yLinesCount: Int,
    barData: List<BarChartData>,
    target: Float = 0f,
    targetSet: Boolean,
    scrollOffset: Float,
): BarChartState {
    return rememberSaveable(saver = BarChartState.Saver) {
        BarChartState.getState(
            barData = barData,
            target = target,
            targetSet = targetSet,
            yLinesCount = yLinesCount,
            noOfVisibleBarCount = noOfVisibleBarCount,
            scrollOffset = scrollOffset
        )
    }
}

class BarChartState {
    private var targetSet by mutableStateOf(false)
    private var viewWidth = 0f
    private var viewHeight = 0f
    var noVisibleBarCount by mutableStateOf(0)
    var target by mutableStateOf(0f)
    var yLinesCount by mutableStateOf(0)
    var barData = listOf<BarChartData>()
    var visibleBarCount by mutableStateOf(0)
    var scrollOffset by mutableStateOf(0f)

    var showBarLabels by mutableStateOf(false)

    val transformableState = TransformableState { zoomChange, _, _ -> scaleView(zoomChange) }

    val scrollableState = ScrollableState {
        scrollOffset = if (it > 0) {
            (scrollOffset - it.scrolledBars).coerceAtLeast(0f)
        } else {
            (scrollOffset - it.scrolledBars).coerceAtMost(barData.lastIndex.toFloat() - (noVisibleBarCount.toFloat() - 1f))
        }

        it
    }

    private val Float.scrolledBars: Float
        get() = this * visibleBarCount.toFloat() / viewWidth

    val visibleBars by derivedStateOf {
        if (barData.isNotEmpty()) {
            barData.subList(
                scrollOffset.roundToInt().coerceAtLeast(0),
                (scrollOffset.roundToInt() + visibleBarCount).coerceAtMost(barData.size)
            )
        } else {
            emptyList()
        }
    }

    private fun scaleView(zoomChange: Float) {
        if ((zoomChange < 1f) ||
            (zoomChange > 1f)
        ) {
            visibleBarCount = (visibleBarCount / zoomChange).roundToInt()
        }
    }

    fun getScaleFactor(): Float {
        if (visibleBars.isNotEmpty()) {
            val maxY = visibleBars.maxWith(Comparator.comparing { it.yValue.toFloat() })
            if (!targetSet) {
                target = maxY.yValue.toFloat()
            }
            return if (target > maxY.yValue.toFloat()) viewHeight / target
            else viewHeight / maxY.yValue.toFloat()
        }
        return 0f
    }

    fun getYLines(): List<Float> {
        val yLineItem = target / yLinesCount

        return mutableListOf<Float>().apply {
            repeat(yLinesCount) {
                if (it >= 0) add(target - yLineItem * it.toFloat())
            }
        }
    }

    fun setViewSize(width: Float, height: Float) {
        viewWidth = width
        viewHeight = height
    }

    fun xOffset(barData: BarChartData) =
        viewWidth * visibleBars.indexOf(barData) / visibleBarCount

    fun yOffset(value: Int) = value

    companion object {
        fun getState(
            target: Float,
            noOfVisibleBarCount: Int,
            yLinesCount: Int,
            targetSet: Boolean,
            barData: List<BarChartData>,
            visibleBarCount: Int? = null,
            scrollOffset: Float? = null,
        ) =
            BarChartState().apply {
                this.target = target
                this.targetSet = targetSet
                this.noVisibleBarCount = noOfVisibleBarCount
                this.yLinesCount = yLinesCount
                this.barData = barData
                this.visibleBarCount = visibleBarCount ?: noVisibleBarCount
                this.scrollOffset = scrollOffset ?: (barData.size.toFloat() - this.visibleBarCount)
            }

        @Suppress("UNCHECKED_CAST")
        val Saver: Saver<BarChartState, Any> = listSaver(
            save = {
                listOf(
                    it.target,
                    it.targetSet,
                    it.noVisibleBarCount,
                    it.yLinesCount,
                    it.barData,
                    it.visibleBarCount,
                    it.scrollOffset
                )
            },
            restore = {
                getState(
                    target = it[0] as Float,
                    targetSet = it[1] as Boolean,
                    noOfVisibleBarCount = it[2] as Int,
                    yLinesCount = it[3] as Int,
                    barData = it[4] as List<BarChartData>,
                    visibleBarCount = it[5] as Int,
                    scrollOffset = it[6] as Float,
                )
            }
        )
    }
}
