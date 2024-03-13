package com.example.moneymanager.components.expensesList

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moneymanager.components.ExpensesDayGroup
import com.example.moneymanager.models.Expense
import com.example.moneymanager.models.groupByDay
import com.example.moneymanager.ui.theme.MoneyManagerTheme

@Composable
fun ExpensesList(expenses: List<Expense>, modifier: Modifier = Modifier) {
	val groupedExpenses = expenses.groupByDay()

	Column(modifier = modifier) {
		groupedExpenses.keys.forEach { date ->
			if (groupedExpenses[date] != null) {
				ExpensesDayGroup(
					date = date,
					dayExpenses = groupedExpenses[date]!!,
					modifier = Modifier.padding(bottom = 28.dp)
				)
			}
		}
	}
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ExpensesListPreview() {
	MoneyManagerTheme {
		ExpensesList(expenses = mockExpenses)
	}
}