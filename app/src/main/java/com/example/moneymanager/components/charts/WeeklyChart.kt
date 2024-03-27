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
import com.example.moneymanager.models.groupByDayOfWeek
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.utils.simplifyNumber
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.BarChartData.Bar
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import java.time.DayOfWeek

@Composable
fun WeeklyChart(expenses: List<Expense>) {
	val groupedExpenses = expenses.groupByDayOfWeek()

	BarChart(
		barChartData = BarChartData(
			bars = listOf(
				Bar(
					label = DayOfWeek.SUNDAY.name.substring(0,3),
					value = groupedExpenses[DayOfWeek.SUNDAY.name]?.total?.toFloat() ?: 0f,
					color = Color.White
				),
				Bar(
					label = DayOfWeek.MONDAY.name.substring(0,3),
					value = groupedExpenses[DayOfWeek.MONDAY.name]?.total?.toFloat() ?: 0f,
					color = Color.White
				),
				Bar(
					label = DayOfWeek.TUESDAY.name.substring(0,3),
					value = groupedExpenses[DayOfWeek.TUESDAY.name]?.total?.toFloat() ?: 0f,
					color = Color.White
				),
				Bar(
					label = DayOfWeek.WEDNESDAY.name.substring(0,3),
					value = groupedExpenses[DayOfWeek.WEDNESDAY.name]?.total?.toFloat() ?: 0f,
					color = Color.White
				),
				Bar(
					label = DayOfWeek.THURSDAY.name.substring(0,3),
					value = groupedExpenses[DayOfWeek.THURSDAY.name]?.total?.toFloat() ?: 0f,
					color = Color.White
				),
				Bar(
					label = DayOfWeek.FRIDAY.name.substring(0,3),
					value = groupedExpenses[DayOfWeek.FRIDAY.name]?.total?.toFloat() ?: 0f,
					color = Color.White
				),
				Bar(
					label = DayOfWeek.SATURDAY.name.substring(0,3),
					value = groupedExpenses[DayOfWeek.SATURDAY.name]?.total?.toFloat() ?: 0f,
					color = Color.White
				),
			)
		),
		labelDrawer = LabelDrawer(recurrence = Recurrence.Weekly),
		yAxisDrawer = SimpleYAxisDrawer(
			labelTextColor = TextSecondary,
			labelValueFormatter = ::simplifyNumber,
			labelTextSize = 12.sp
		),
		barDrawer = BarDrawer(recurrence = Recurrence.Weekly),
		modifier = Modifier
			.fillMaxSize()
			.padding(top = 25.dp, bottom = 45.dp)
			.padding(horizontal = 10.dp)
	)
}