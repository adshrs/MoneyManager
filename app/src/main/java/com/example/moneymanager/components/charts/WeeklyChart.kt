package com.example.moneymanager.components.charts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.models.expense.ExpenseResponse
import com.example.moneymanager.models.expense.groupByDayOfWeek
import com.example.moneymanager.models.income.IncomeResponse
import com.example.moneymanager.models.income.groupByDayOfWeek
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.utils.simplifyNumber
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.BarChartData.Bar
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import java.time.DayOfWeek

@Composable
fun WeeklyChart(expenses: List<ExpenseResponse>?, incomes: List<IncomeResponse>?) {
	val groupedExpenses = expenses?.groupByDayOfWeek()
	val groupedIncomes = incomes?.groupByDayOfWeek()

	val containsGroupedExpenses = !groupedExpenses.isNullOrEmpty()

	BarChart(
		barChartData = BarChartData(
			bars = listOf(
				Bar(
					label = DayOfWeek.SUNDAY.name.substring(0, 3),
					value =
					if (containsGroupedExpenses)
						groupedExpenses?.get(DayOfWeek.SUNDAY.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(DayOfWeek.SUNDAY.name)?.total?.toFloat() ?: 0f,
					color = Color.White
				),
				Bar(
					label = DayOfWeek.MONDAY.name.substring(0, 3),
					value = if (containsGroupedExpenses)
						groupedExpenses?.get(DayOfWeek.MONDAY.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(DayOfWeek.MONDAY.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				Bar(
					label = DayOfWeek.TUESDAY.name.substring(0, 3),
					value = if (containsGroupedExpenses)
						groupedExpenses?.get(DayOfWeek.TUESDAY.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(DayOfWeek.TUESDAY.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				Bar(
					label = DayOfWeek.WEDNESDAY.name.substring(0, 3),
					value = if (containsGroupedExpenses)
						groupedExpenses?.get(DayOfWeek.WEDNESDAY.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(DayOfWeek.WEDNESDAY.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				Bar(
					label = DayOfWeek.THURSDAY.name.substring(0, 3),
					value = if (containsGroupedExpenses)
						groupedExpenses?.get(DayOfWeek.THURSDAY.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(DayOfWeek.THURSDAY.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				Bar(
					label = DayOfWeek.FRIDAY.name.substring(0, 3),
					value = if (containsGroupedExpenses)
						groupedExpenses?.get(DayOfWeek.FRIDAY.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(DayOfWeek.FRIDAY.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				Bar(
					label = DayOfWeek.SATURDAY.name.substring(0, 3),
					value = if (containsGroupedExpenses)
						groupedExpenses?.get(DayOfWeek.SATURDAY.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(DayOfWeek.SATURDAY.name)?.total?.toFloat()?: 0f,
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
		barDrawer =
		if (containsGroupedExpenses)
			BarDrawer(recurrence = Recurrence.Weekly, type = "Expense")
		else
			BarDrawer(recurrence = Recurrence.Weekly, type = "Income"),
		modifier = Modifier
			.fillMaxSize()
			.padding(top = 25.dp, bottom = 45.dp)
			.padding(horizontal = 10.dp)
	)
}