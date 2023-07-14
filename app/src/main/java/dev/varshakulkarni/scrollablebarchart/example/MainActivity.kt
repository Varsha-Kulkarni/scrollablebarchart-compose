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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dev.varshakulkarni.scrollablebarchart.ChartData
import dev.varshakulkarni.scrollablebarchart.ScrollableBarChart
import dev.varshakulkarni.scrollablebarchart.example.ui.theme.ScrollablebarchartcomposeTheme

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
                        ChartData(20, 9000),
                        ChartData(10, 400),
                        ChartData(30, 300),
                        ChartData(40, 8000),
                        ChartData(50, 8300),
                        ChartData(70, 12000),
                        ChartData(80, 200),
                        ChartData(90, 300),
                        ChartData(100, 830),
                        ChartData(110, 600),

                    )

                    BoxWithConstraints() {
                        ScrollableBarChart(
                            chartData,
                            chartWidth = constraints.maxWidth - 150f,
                            chartHeight = constraints.maxHeight - 150f
                        )
                    }
                }
            }
        }
    }
}
