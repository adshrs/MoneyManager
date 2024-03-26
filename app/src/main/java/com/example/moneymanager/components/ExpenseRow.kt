package com.example.moneymanager.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moneymanager.components.expensesList.faker
import com.example.moneymanager.models.Category
import com.example.moneymanager.models.Expense
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.ui.theme.CardColor
import com.example.moneymanager.ui.theme.Destructive
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.Typography
import java.text.DecimalFormat
import java.time.LocalDate

@Composable
fun ExpenseRow(expense: Expense, modifier: Modifier = Modifier) {
	Surface(
		modifier = modifier,
		shape = RoundedCornerShape(10.dp),
		color = CardColor
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(vertical = 8.dp, horizontal = 12.dp),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically,
		) {
			Column(horizontalAlignment = Alignment.Start) {
				CategoryBadge(
					category = expense.category,
					modifier = Modifier.padding(vertical = 4.dp)
				)
				Text(
					text = expense.note,
					style = Typography.bodySmall,
					modifier = Modifier.padding(top = 4.dp)
				)
			}
			Column(verticalArrangement = Arrangement.Center) {
				Text(
					text = "Rs. ${DecimalFormat("0.#").format(expense.amount)}",
					style = Typography.labelMedium,
					color = Destructive
				)
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun ExpenseRowPreview() {
	MoneyManagerTheme {
		Surface(color = com.example.moneymanager.ui.theme.Surface) {
			ExpenseRow(
				expense = Expense(
					id = 1,
					amount = 1950.0,
					date = LocalDate.now(),
					recurrence = Recurrence.None,
					note = "Clothes",
					category = faker.random.randomValue(
						listOf(
							Category("Shopping", Color.Red)
						)
					)
				)
			)
		}
	}
}
