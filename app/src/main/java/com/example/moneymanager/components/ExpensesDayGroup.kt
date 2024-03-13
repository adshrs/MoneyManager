package com.example.moneymanager.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moneymanager.models.DayExpenses
import com.example.moneymanager.ui.theme.Destructive
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.utils.formatDay
import java.text.DecimalFormat
import java.time.LocalDate

@Composable
fun ExpensesDayGroup(date: LocalDate, dayExpenses: DayExpenses, modifier: Modifier = Modifier) {
	Column(modifier = modifier) {
		Text(
			text = date.formatDay(),
			style = Typography.headlineLarge,
			color = TextSecondary
		)
		HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 4.dp))
		dayExpenses.expenses.forEach { expense ->
			ExpenseRow(
				expense = expense,
				modifier = Modifier.padding(top = 8.dp)
			)
		}
		HorizontalDivider(modifier = Modifier.padding(top = 12.dp, bottom = 10.dp))
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Text(text = "Total:", style = Typography.bodyMedium, color = TextSecondary)
			Text(
				text = "Rs. ${DecimalFormat("0.#").format(dayExpenses.total)}",
				style = Typography.headlineMedium,
				color = Destructive
			)
		}
	}
}