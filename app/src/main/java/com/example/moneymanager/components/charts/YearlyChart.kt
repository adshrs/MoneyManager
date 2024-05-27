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
import com.example.moneymanager.models.expense.groupByMonth
import com.example.moneymanager.models.income.IncomeResponse
import com.example.moneymanager.models.income.groupByMonth
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.utils.simplifyNumber
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import java.time.Month

@Composable
fun YearlyChart(expenses: List<ExpenseResponse>?, incomes: List<IncomeResponse>?) {
	val groupedExpenses = expenses?.groupByMonth()
	val groupedIncomes = incomes?.groupByMonth()

	val containsGroupedExpenses = !groupedExpenses.isNullOrEmpty()

	BarChart(
		barChartData = BarChartData(
			bars = listOf(
				BarChartData.Bar(
					label = Month.JANUARY.name.substring(0,1),
					value =
					if (containsGroupedExpenses)
						groupedExpenses?.get(Month.JANUARY.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(Month.JANUARY.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				BarChartData.Bar(
					label = Month.FEBRUARY.name.substring(0,1),
					value =
					if (containsGroupedExpenses)
						groupedExpenses?.get(Month.FEBRUARY.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(Month.FEBRUARY.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				BarChartData.Bar(
					label = Month.MARCH.name.substring(0,1),
					value =
					if (containsGroupedExpenses)
						groupedExpenses?.get(Month.MARCH.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(Month.MARCH.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				BarChartData.Bar(
					label = Month.APRIL.name.substring(0,1),
					value =
					if (containsGroupedExpenses)
						groupedExpenses?.get(Month.APRIL.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(Month.APRIL.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				BarChartData.Bar(
					label = Month.MAY.name.substring(0,1),
					value =
					if (containsGroupedExpenses)
						groupedExpenses?.get(Month.MAY.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(Month.MAY.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				BarChartData.Bar(
					label = Month.JUNE.name.substring(0,1),
					value =
					if (containsGroupedExpenses)
						groupedExpenses?.get(Month.JUNE.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(Month.JUNE.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				BarChartData.Bar(
					label = Month.JULY.name.substring(0,1),
					value =
					if (containsGroupedExpenses)
						groupedExpenses?.get(Month.JULY.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(Month.JULY.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				BarChartData.Bar(
					label = Month.AUGUST.name.substring(0,1),
					value =
					if (containsGroupedExpenses)
						groupedExpenses?.get(Month.AUGUST.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(Month.AUGUST.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				BarChartData.Bar(
					label = Month.SEPTEMBER.name.substring(0,1),
					value =
					if (containsGroupedExpenses)
						groupedExpenses?.get(Month.SEPTEMBER.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(Month.SEPTEMBER.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				BarChartData.Bar(
					label = Month.OCTOBER.name.substring(0,1),
					value =
					if (containsGroupedExpenses)
						groupedExpenses?.get(Month.OCTOBER.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(Month.OCTOBER.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				BarChartData.Bar(
					label = Month.NOVEMBER.name.substring(0,1),
					value =
					if (containsGroupedExpenses)
						groupedExpenses?.get(Month.NOVEMBER.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(Month.NOVEMBER.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
				BarChartData.Bar(
					label = Month.DECEMBER.name.substring(0,1),
					value =
					if (containsGroupedExpenses)
						groupedExpenses?.get(Month.DECEMBER.name)?.total?.toFloat()?: 0f
					else
						groupedIncomes?.get(Month.DECEMBER.name)?.total?.toFloat()?: 0f,
					color = Color.White
				),
			)
		),
		labelDrawer = LabelDrawer(recurrence = Recurrence.Yearly),
		yAxisDrawer = SimpleYAxisDrawer(
			labelTextColor = TextSecondary,
			labelValueFormatter = ::simplifyNumber,
			labelTextSize = 12.sp
		),
		barDrawer = if (containsGroupedExpenses)
			BarDrawer(recurrence = Recurrence.Yearly, type = "Expense")
		else
			BarDrawer(recurrence = Recurrence.Yearly, type = "Income"),
		modifier = Modifier
			.fillMaxSize()
			.padding(top = 25.dp, bottom = 45.dp)
			.padding(horizontal = 10.dp)
	)
}