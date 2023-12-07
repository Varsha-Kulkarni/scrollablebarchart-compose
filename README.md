# scrollablebarchart-compose

Simple customizable scrollable bar chart library which was built using Jetpack Compose as a learning exercise, follows [guidelines](https://github.com/androidx/androidx/blob/androidx-main/compose/docs/compose-component-api-guidelines.md) as much as possible. 

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
LTRScrollableBarChart(chartData = chartData,
                      chartSize = ChartDefaults.chartSize(600.dp, 500.dp))
```

</td>
<td> 
  <img src="/assets/ltr.gif" width="260">
</td>

</tr>
<tr>
  <td>
    
```
RTLScrollableBarChart(chartData = chartData,
                      chartSize = ChartDefaults.chartSize(600.dp, 500.dp))
```
</td>
<td> 
  <img src="/assets/rtl.gif" width="260">
</td>

</tr>

