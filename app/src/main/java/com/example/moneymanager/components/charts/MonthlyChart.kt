package com.example.moneymanager.components.charts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneymanager.models.Expense
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.models.groupByDayOfMonth
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.utils.simplifyNumber
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer

@Composable
fun MonthlyChart(expenses: List<Expense>, numberOfDays: Int) {
	val groupedExpenses = expenses.groupByDayOfMonth()

	BarChart(
		barChartData = BarChartData(
			bars = buildList {
				for (i in 1..numberOfDays) {
					add(
						BarChartData.Bar(
							label = "$i",
							value = groupedExpenses[i]?.total?.toFloat() ?: 0f,
							color = Color.White
						)
					)
				}
			}
		),
		labelDrawer = LabelDrawer(recurrence = Recurrence.Monthly, lastDay = numberOfDays),
		yAxisDrawer = SimpleYAxisDrawer(
			labelTextColor = TextSecondary,
			labelValueFormatter = ::simplifyNumber,
			labelTextSize = 12.sp
		),
		barDrawer = BarDrawer(recurrence = Recurrence.Monthly),
		modifier = Modifier
			.fillMaxSize()
			.padding(top = 25.dp, bottom = 45.dp)
			.padding(horizontal = 10.dp)
	)
}