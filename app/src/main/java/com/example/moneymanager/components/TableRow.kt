package com.example.moneymanager.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.moneymanager.R
import com.example.moneymanager.ui.theme.Destructive
import com.example.moneymanager.ui.theme.TextPrimary
import com.example.moneymanager.ui.theme.Typography

@Composable
fun TableRow(
	label: String,
	modifier: Modifier = Modifier,
	hasArrow: Boolean = false,
	isDestructive: Boolean = false,
	content: (@Composable RowScope.() -> Unit)? = null
) {
	val textColor = if (isDestructive) Destructive else TextPrimary

	Row(
		modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, )  ,
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(
			text = label,
			modifier = Modifier.padding(vertical = 10.dp),
			style = Typography.bodyMedium,
			color = textColor
		)
		if (hasArrow) {
			Icon(
				painterResource(id = R.drawable.icon_chevron_right),
				contentDescription = "Right arrow",
				modifier = Modifier.padding(vertical = 10.dp)
			)
		}
		if (content != null) {
			content()
		}
	}
}