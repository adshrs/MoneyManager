package com.example.moneymanager.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moneymanager.ui.theme.Primary
import com.example.moneymanager.ui.theme.Typography

@Composable
fun WarningInfo(info: String) {
	Surface(
		modifier = Modifier
			.padding(top = 16.dp)
			.padding(horizontal = 8.dp),
		border = BorderStroke(1.5.dp, Primary.copy(alpha = 0.7f)),
		shape = RoundedCornerShape(20.dp),

		) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 10.dp, vertical = 6.dp),
			verticalAlignment = Alignment.CenterVertically,
		) {
			Icon(
				imageVector = Icons.Filled.Info,
				contentDescription = "info",
				modifier = Modifier.size(20.dp),
				tint = Primary.copy(alpha = 0.8f)
			)
			Text(
				text = info,
				style = Typography.headlineSmall,
				modifier = Modifier.padding(start = 8.dp),
				color = Primary.copy(alpha = 0.8f)
			)
		}
	}
}
