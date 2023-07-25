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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.varshakulkarni.scrollablebarchart.ChartData
import dev.varshakulkarni.scrollablebarchart.SPACING_MEDIUM
import dev.varshakulkarni.scrollablebarchart.example.ui.theme.ScrollablebarchartcomposeTheme
import dev.varshakulkarni.scrollablebarchart.ui.chart.LTRScrollableBarChart
import dev.varshakulkarni.scrollablebarchart.ui.chart.RTLScrollableBarChart

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
                        ChartData(100, 1f),
                        ChartData(110, 2f),
                        ChartData(120, 8f),
                        ChartData(130, 3f),
                        ChartData(140, 5f),
                        ChartData(150, 6f),
                        ChartData(160, 9f),
                        ChartData(170, 4f),
                        ChartData(180, 3f),
                        ChartData(190, 6f),
                        ChartData(200, 6f)
                    )

                    BoxWithConstraints() {
                        Column(Modifier.padding(SPACING_MEDIUM.dp)) {
                            Text("Left to Right scroll")
                            LTRScrollableBarChart(
                                chartData, chartWidth = 600f, chartHeight = 600f
                            )
                            Text("Right to Left scroll")
                            RTLScrollableBarChart(
                                chartData, chartWidth = 600f, chartHeight = 600f
                            )
                        }
                    }
                }
            }
        }
    }
}
