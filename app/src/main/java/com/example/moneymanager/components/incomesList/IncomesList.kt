package com.example.moneymanager.components.incomesList

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
import com.example.moneymanager.components.IncomesDayGroup
import com.example.moneymanager.models.category.CategoryResponse
import com.example.moneymanager.models.income.IncomeResponse
import com.example.moneymanager.models.income.groupByDay
import com.example.moneymanager.ui.theme.MoneyManagerTheme

@Composable
fun IncomesList(
	incomes: List<IncomeResponse>,
	categories: List<CategoryResponse>,
	modifier: Modifier = Modifier
) {
	val groupedIncomes = incomes.groupByDay()

	Column(modifier = modifier) {
		if (groupedIncomes.isEmpty()) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(333.dp),
				contentAlignment = Alignment.TopCenter
			) {
				Text(text = "No data for this date range.", modifier.padding(top = 130.dp))
			}
		} else {
			groupedIncomes.keys.forEach { date ->
				if (groupedIncomes[date] != null) {
					IncomesDayGroup(
						date = date,
						dayIncomes = groupedIncomes[date]!!,
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
fun IncomesListPreview() {
	MoneyManagerTheme {
		IncomesList(incomes = mockIncomes, categories = listOf())
	}
}