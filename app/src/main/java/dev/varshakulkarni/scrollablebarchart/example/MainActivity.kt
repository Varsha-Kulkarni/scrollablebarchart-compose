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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.varshakulkarni.scrollablebarchart.BarChartData
import dev.varshakulkarni.scrollablebarchart.BarChartDataCollection
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
                    val barChartData = listOf(
                        BarChartData(10, 1),
                        BarChartData(11, 2),
                        BarChartData(12, 8),
                        BarChartData(13, 3),
                        BarChartData(14, 5),
                        BarChartData(15, 6f),
                        BarChartData(16, 9f),
                        BarChartData(17, 4f),
                        BarChartData(18, 3f),
                        BarChartData(19, 60f),
                        BarChartData(20, 6f)
                    )
                    val reversedData: List<BarChartData> = rememberSaveable(barChartData) {
                        barChartData.reversed()
                    }

                    Box(Modifier.padding(SPACING_MEDIUM.dp)) {
                        Column(
                            Modifier.verticalScroll(rememberScrollState()),
                            content = {
                                Text("Left to Right scroll")
                                Spacer(modifier = Modifier.height(24.dp))
                                LTRScrollableBarChart(
                                    barChartDataCollection = BarChartDataCollection(barChartData),
                                    modifier = Modifier.padding(24.dp)
                                )

                                Text("Right to Left scroll")
                                Spacer(modifier = Modifier.height(24.dp))
                                RTLScrollableBarChart(
                                    barChartDataCollection = BarChartDataCollection(reversedData),
                                    target = 30f,
                                    modifier = Modifier.padding(24.dp)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
