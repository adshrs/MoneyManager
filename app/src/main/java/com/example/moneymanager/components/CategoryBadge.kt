package com.example.moneymanager.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moneymanager.models.Category
import com.example.moneymanager.ui.theme.Typography

@Composable
fun CategoryBadge(category: Category, modifier: Modifier = Modifier) {
	Surface(
		shape = RoundedCornerShape(10.dp),
		color = category.color.copy(alpha = 0.4f),
		modifier = modifier
	) {
		Text(
			modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
			text = category.name,
			color = category.color,
			style	= Typography.labelMedium
		)
	}
}