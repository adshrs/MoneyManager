package com.example.moneymanager.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneymanager.R
import com.example.moneymanager.components.charts.MonthlyChart
import com.example.moneymanager.components.charts.WeeklyChart
import com.example.moneymanager.components.charts.YearlyChart
import com.example.moneymanager.components.expensesList.ExpensesList
import com.example.moneymanager.components.expensesList.mockExpenses
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.viewmodels.AnalyticsViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Analytics(
	navController: NavController,
	analyticsViewModel: AnalyticsViewModel = viewModel()
) {
	val uiState by analyticsViewModel.uiState.collectAsState()

	val recurrences = listOf(
		Recurrence.Weekly,
		Recurrence.Monthly,
		Recurrence.Yearly
	)

	Scaffold(
		topBar = {
			MediumTopAppBar(
				title = { Text(text = "Analytics") },
				colors = TopAppBarDefaults.mediumTopAppBarColors(
					containerColor = TopAppBarBackground
				),
				actions = {
					IconButton(
						onClick = analyticsViewModel::openDateRangeMenu
					) {
						Icon(
							painterResource(id = R.drawable.icon_date_range),
							contentDescription = "filter date range"
						)
					}
					DropdownMenu(
						expanded = uiState.dateRangeMenuOpened,
						onDismissRequest = analyticsViewModel::closeDateRangeMenu,
						modifier = Modifier
					) {
						recurrences.forEach { recurrence ->
							DropdownMenuItem(
								text = { Text(text = recurrence.name) },
								onClick = {
									analyticsViewModel.setRecurrence(recurrence)
									analyticsViewModel.closeDateRangeMenu()
								}
							)
						}
					}
				}
			)
		},
		content = { innerPadding ->
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
						Text(text = "12 Sep - 18 Sep", style = Typography.titleSmall)
						Row(modifier = Modifier.padding(top = 8.dp)) {
							Text(
								text = "USD",
								style = Typography.bodyMedium,
								color = TextSecondary,
								modifier = Modifier.padding(end = 4.dp)
							)
							Text(text = "85", style = Typography.headlineMedium)
						}
					}
					Column(horizontalAlignment = Alignment.End) {
						Text(text = "Avg/day", style = Typography.titleSmall)
						Row(modifier = Modifier.padding(top = 8.dp)) {
							Text(
								text = "USD",
								style = Typography.bodyMedium,
								color = TextSecondary,
								modifier = Modifier.padding(end = 4.dp)
							)
							Text(text = "85", style = Typography.headlineMedium)
						}
					}
				}

				Box(
					modifier = Modifier
						.height(223.dp)
				) {
					when (uiState.recurrence) {
						Recurrence.Weekly -> WeeklyChart(expenses = mockExpenses)
						Recurrence.Monthly -> MonthlyChart(expenses = mockExpenses, date = LocalDate.now())
						Recurrence.Yearly -> YearlyChart(expenses = mockExpenses)
						else -> Unit
					}
				}

				ExpensesList(
					expenses = mockExpenses,
					modifier = Modifier
						.padding(0.dp)
						.verticalScroll(rememberScrollState())
				)
			}
		}
	)
}

@Preview
@Composable
fun AnalyticsPreview() {
	MoneyManagerTheme {
		Analytics(navController = rememberNavController())
	}
}