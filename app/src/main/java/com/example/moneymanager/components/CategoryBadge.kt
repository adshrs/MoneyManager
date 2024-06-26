package com.example.moneymanager.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moneymanager.models.category.CategoryResponse
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.utils.parseColorString

@Composable
fun CategoryBadge(categoryResponse: CategoryResponse, modifier: Modifier = Modifier) {
	Surface(
		shape = RoundedCornerShape(10.dp),
		color = parseColorString(categoryResponse.color).copy(alpha = 0.4f),
		modifier = modifier
	) {
		Text(
			modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
			text = categoryResponse.name,
			color = parseColorString(categoryResponse.color),
			style = Typography.labelMedium
		)
	}
}