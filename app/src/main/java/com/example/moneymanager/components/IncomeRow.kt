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
import androidx.compose.ui.unit.dp
import com.example.moneymanager.models.category.CategoryResponse
import com.example.moneymanager.models.income.IncomeResponse
import com.example.moneymanager.ui.theme.CardColor
import com.example.moneymanager.ui.theme.Typography
import java.text.DecimalFormat

@Composable
fun IncomeRow(incomeResponse: IncomeResponse, categoryResponse: CategoryResponse, modifier: Modifier = Modifier) {
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
					categoryResponse = categoryResponse,
					modifier = Modifier.padding(vertical = 4.dp)
				)
				Text(
					text = incomeResponse.description,
					style = Typography.bodySmall,
					modifier = Modifier.padding(top = 4.dp)
				)
			}
			Column(verticalArrangement = Arrangement.Center) {
				Text(
					text = "Rs. ${DecimalFormat("0.#").format(incomeResponse.amount)}",
					style = Typography.labelMedium,
					color = Color.Green
				)
			}
		}
	}
}