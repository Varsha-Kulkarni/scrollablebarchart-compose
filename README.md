# scrollablebarchart-compose

Simple customizable scrollable bar chart library which is built using Jetpack Compose as a pure learning exercise, follows [guidelines](https://github.com/androidx/androidx/blob/androidx-main/compose/docs/compose-component-api-guidelines.md) as much as possible. This has been used in [Pedometer](https://github.com/Varsha-Kulkarni/Pedometer) app which tracks user's daily step count.

## Usage
Sample Data
```
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
```
<table>
<tr>
<td> Example </td> <td> Preview </td>
</tr>
<tr>
  <td> 
    
```
LTRScrollableBarChart(
    chartData = chartData,
    chartSize = ChartDefaults.chartSize(600.dp, 500.dp)
)
```

</td>
<td> 
  <img src="/assets/ltr.gif" width="260">
</td>

</tr>
<tr>
  <td>
    
```
RTLScrollableBarChart(
    chartData = chartData,
    chartSize = ChartDefaults.chartSize(600.dp, 500.dp))
```
</td>
<td> 
  <img src="/assets/rtl.gif" width="260">
</td>

</tr>
</table>

# Contributions

Most welcome to file issues, PRs, specially where performance could be improved. Please see [Contributing Guidelines](https://github.com/Varsha-Kulkarni/scrollablebarchart-compose/blob/main/CONTRIBUTING.md)

# License

```
Copyright 2023 Varsha Kulkarni
 
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
    https://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

