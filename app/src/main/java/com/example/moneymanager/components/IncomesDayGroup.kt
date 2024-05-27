package com.example.moneymanager.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moneymanager.models.category.CategoryResponse
import com.example.moneymanager.models.income.DayIncomes
import com.example.moneymanager.ui.theme.Primary
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.utils.formatDay
import com.example.moneymanager.viewmodels.AnalyticsViewModel
import com.example.moneymanager.viewmodels.IncomesViewModel
import java.text.DecimalFormat
import java.time.LocalDate

@Composable
fun IncomesDayGroup(
	date: LocalDate,
	dayIncomes: DayIncomes,
	categories: List<CategoryResponse>,
	modifier: Modifier = Modifier,
	incomesViewModel: IncomesViewModel = hiltViewModel(),
	analyticsViewModel: AnalyticsViewModel = hiltViewModel()
) {

	Column(modifier = modifier) {
		Text(
			text = date.formatDay(),
			style = Typography.headlineLarge,
			color = TextSecondary
		)
		HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 6.dp))
		dayIncomes.incomes.forEach { income ->
			val categoryResponse = categories.find { it.id == income.categoryId }
			if (categoryResponse != null) {
				IncomeRow(
					incomeResponse = income,
					categoryResponse = categoryResponse,
					modifier = Modifier
						.padding(top = 6.dp)
						.clickable {
							incomesViewModel.showDeleteWarning(income.id)
							analyticsViewModel.showIncomeDeleteWarning(income.id)
						}
				)
			}
			else {
				IncomeRow(
					incomeResponse = income,
					categoryResponse = CategoryResponse(
						id = "",
						name = "Income",
						color = Primary.toString(),
						userId = "",
						type = "Income"
					),
					modifier = Modifier
						.padding(top = 6.dp)
						.clickable {
							incomesViewModel.showDeleteWarning(income.id)
							analyticsViewModel.showIncomeDeleteWarning(income.id)
						}
				)
			}

		}
		HorizontalDivider(modifier = Modifier.padding(top = 12.dp, bottom = 10.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(text = "Total:", style = Typography.bodyMedium, color = TextSecondary)
			Text(
				text = "Rs. ${DecimalFormat("0.#").format(dayIncomes.total)}",
				style = Typography.labelMedium,
				color = Color.Green
			)
		}
	}
}