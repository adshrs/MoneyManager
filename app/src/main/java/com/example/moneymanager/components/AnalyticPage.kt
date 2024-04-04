package com.example.moneymanager.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moneymanager.components.charts.MonthlyChart
import com.example.moneymanager.components.charts.WeeklyChart
import com.example.moneymanager.components.charts.YearlyChart
import com.example.moneymanager.components.expensesList.ExpensesList
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.utils.calculateDateRange
import com.example.moneymanager.utils.formatDayForRange
import com.example.moneymanager.viewmodels.AnalyticPageViewModel
import com.example.moneymanager.viewmodels.viewModelFactory
import java.text.DecimalFormat

@Composable
fun AnalyticPage(
	innerPadding: PaddingValues,
	page: Int,
	recurrence: Recurrence,
	analyticPageViewModel: AnalyticPageViewModel = viewModel(
		key = "$page-${recurrence.name} ",
		factory = viewModelFactory {
			AnalyticPageViewModel(page, recurrence)
		}
	)
) {
	val uiState by analyticPageViewModel.uiState.collectAsState()

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(innerPadding)
			.padding(horizontal = 8.dp)
			.padding(top = 16.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Row(
			horizontalArrangement = Arrangement.SpaceBetween,
			modifier = Modifier.fillMaxWidth()
		) {
			Column() {
				Text(
					text = "${uiState.dateStart.toLocalDate().formatDayForRange()} " +
							  "- ${uiState.dateEnd.toLocalDate().formatDayForRange()}",
					style = Typography.bodyMedium
				)
				Row(modifier = Modifier.padding(top = 8.dp)) {
					Text(
						text = "Rs.",
						style = Typography.bodySmall,
						color = TextSecondary,
						modifier = Modifier.padding(end = 4.dp)
					)
					Text(
						text = DecimalFormat("0.#").format(uiState.totalForDateRange),
						style = Typography.headlineSmall
					)
				}
			}
			Column(horizontalAlignment = Alignment.End) {
				Text(text = "Avg/day", style = Typography.bodyMedium)
				Row(modifier = Modifier.padding(top = 8.dp)) {
					Text(
						text = "Rs.",
						style = Typography.bodySmall,
						color = TextSecondary,
						modifier = Modifier.padding(end = 4.dp)
					)
					Text(
						text = DecimalFormat("0.#").format(uiState.avgPerDay),
						style = Typography.headlineSmall
					)
				}
			}
		}

		Box(
			modifier = Modifier
				.height(223.dp)
		) {
			when (recurrence) {
				Recurrence.Weekly -> WeeklyChart(expenses = uiState.expenses)
				Recurrence.Monthly ->
					MonthlyChart(
						expenses = uiState.expenses,
						numberOfDays = calculateDateRange(Recurrence.Monthly, page).daysInRange
					)
				Recurrence.Yearly -> YearlyChart(expenses = uiState.expenses)
				else -> Unit
			}
		}

		ExpensesList(
			expenses = uiState.expenses,
			modifier = Modifier
				.padding(0.dp)
				.verticalScroll(rememberScrollState())
		)
	}
}