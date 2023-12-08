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
package dev.varshakulkarni.scrollablebarchart.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.varshakulkarni.scrollablebarchart.ChartData
import dev.varshakulkarni.scrollablebarchart.ChartDefaults
import dev.varshakulkarni.scrollablebarchart.SPACING_MEDIUM
import dev.varshakulkarni.scrollablebarchart.example.ui.theme.ScrollablebarchartcomposeTheme
import dev.varshakulkarni.scrollablebarchart.ui.chart.LTRScrollableBarChart
import dev.varshakulkarni.scrollablebarchart.ui.chart.RTLScrollableBarChart
import dev.varshakulkarni.scrollablebarchart.utils.rememberComposeImmutableList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScrollablebarchartcomposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val chartData = listOf(
                        ChartData(10, 1f),
                        ChartData(11, 2f),
                        ChartData(12, 8f),
                        ChartData(13, 3f),
                        ChartData(14, 5f),
                        ChartData(15, 6f),
                        ChartData(16, 9f),
                        ChartData(17, 4f),
                        ChartData(18, 3f),
                        ChartData(19, 6f),
                        ChartData(20, 6f)
                    )
                    val immutableChartData by rememberComposeImmutableList { chartData }
                    val data: List<ChartData> = remember(chartData) {
                        chartData.reversed()
                    }
                    val reversed by rememberComposeImmutableList {
                        data
                    }

                    Box(Modifier.padding(SPACING_MEDIUM.dp)) {
                        Column(
                            Modifier.verticalScroll(rememberScrollState()),
                            content = {
                                Text("Left to Right scroll")
                                LTRScrollableBarChart(
                                    chartData = immutableChartData,
                                    chartSize = ChartDefaults.chartSize(600.dp, 500.dp)
                                )

                                Text("Right to Left scroll")
                                RTLScrollableBarChart(
                                    chartData = reversed,
                                    chartSize = ChartDefaults.chartSize(600.dp, 500.dp)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
