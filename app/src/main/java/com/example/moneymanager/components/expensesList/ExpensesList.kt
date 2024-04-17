package com.example.moneymanager.components.expensesList

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moneymanager.components.ExpensesDayGroup
import com.example.moneymanager.models.category.CategoryResponse
import com.example.moneymanager.models.expense.ExpenseResponse
import com.example.moneymanager.models.expense.groupByDay
import com.example.moneymanager.ui.theme.MoneyManagerTheme

@Composable
fun ExpensesList(expenses: List<ExpenseResponse>, categories: List<CategoryResponse>, modifier: Modifier = Modifier) {
	val groupedExpenses = expenses.groupByDay()

	Column(modifier = modifier) {
		if (groupedExpenses.isEmpty()) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(333.dp),
				contentAlignment = Alignment.TopCenter
				) {
				Text(text = "No data for this date range.", modifier.padding(top = 130.dp))
			}
		} else {
			groupedExpenses.keys.forEach { date ->
				if (groupedExpenses[date] != null) {
					ExpensesDayGroup(
						date = date,
						dayExpenses = groupedExpenses[date]!!,
						categories = categories,
						modifier = Modifier.padding(bottom = 28.dp)
					)
				}
			}
		}
	}
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ExpensesListPreview() {
	MoneyManagerTheme {
		ExpensesList(expenses = mockExpenses, categories = listOf())
	}
}