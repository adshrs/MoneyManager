package com.example.moneymanager.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneymanager.components.DropdownButton
import com.example.moneymanager.components.expensesList.ExpensesList
import com.example.moneymanager.components.expensesList.mockExpenses
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.viewmodels.ExpensesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Expenses(
	navController: NavController,
	expensesViewModel: ExpensesViewModel = viewModel()
) {
	val state by expensesViewModel.uiState.collectAsState()

	val recurrences = listOf(
		Recurrence.Daily,
		Recurrence.Weekly,
		Recurrence.Monthly,
		Recurrence.Yearly
	)

	var recurrenceMenuOpened by remember {
		mutableStateOf(false)
	}

	Scaffold(
		topBar = {
			MediumTopAppBar(
				title = { Text(text = "Expenses", style = Typography.titleMedium) },
				colors = TopAppBarDefaults.mediumTopAppBarColors(
					containerColor = TopAppBarBackground
				)
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
				Row(verticalAlignment = Alignment.CenterVertically) {
					Text(
						text = "Total for:",
						style = Typography.bodyMedium
					)
					DropdownButton(
						label = state.recurrence.target,
						onClick = { recurrenceMenuOpened = !recurrenceMenuOpened },
						modifier = Modifier.padding(start = 16.dp)
					)
					DropdownMenu(
						expanded = recurrenceMenuOpened,
						onDismissRequest = { recurrenceMenuOpened = false },
						modifier = Modifier

					) {
						recurrences.forEach { recurrence ->
							DropdownMenuItem(
								text = { Text(text = recurrence.target) },
								onClick = {
									expensesViewModel.setRecurrence(recurrence)
									recurrenceMenuOpened = false
								}
							)
						}
					}
				}
				Row(modifier = Modifier.padding(vertical = 32.dp)) {
					Text(
						text = "Rs.",
						style = Typography.bodyMedium,
						color = TextSecondary,
						modifier = Modifier.padding(end = 4.dp)
					)
					Text(text = "${state.sumTotal}", style = Typography.titleLarge)
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
fun ExpensesPreview() {
	MoneyManagerTheme {
		Expenses(navController = rememberNavController())
	}
}