# scrollablebarchart-compose

Simple customizable scrollable bar chart library which was built using Jetpack Compose as a learning exercise, follows [guidelines](https://github.com/androidx/androidx/blob/androidx-main/compose/docs/compose-component-api-guidelines.md) as much as possible. 

## Usage
Sample Data
```
val chartData = listOf( ChartData(10, 400),
                        ChartData(20, 900),
                        ChartData(30, 300),
                        ChartData(40, 8000),
                        ChartData(50, 8300),
                        ChartData(70, 12000),
                        ChartData(80, 200),
                        ChartData(90, 300),
                        ChartData(100, 830),
                        ChartData(120, 600))
```
<table>
<tr>
<td> Example </td> <td> Preview </td>
</tr>
<tr>
  <td> 
    
```
ScrollableBarChart(chartData)
```

</td>
<td> 
  <img src="/assets/chart_left-to-right.gif" width="260">
</td>

</tr>
<tr>
  <td>
    
```
ScrollableBarChart( chartData,
                    chartDirection = ChartDirection.RIGHT_TO_LEFT)
```
</td>
  <img src="/assets/chart_right_to_left.gif" width="260">
<td> 
</td>

</tr>

