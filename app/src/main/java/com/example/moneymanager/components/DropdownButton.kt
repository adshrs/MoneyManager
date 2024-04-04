package com.example.moneymanager.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moneymanager.R
import com.example.moneymanager.ui.theme.FillTertiary
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.ui.theme.Typography

@Composable
fun DropdownButton(
	modifier: Modifier = Modifier,
	label: String = "",
	onClick: () -> Unit,
	content: (@Composable RowScope.() -> Unit)? = null
) {
	Surface(
		shape = RoundedCornerShape(6.dp),
		color = FillTertiary,
		modifier = modifier,
		onClick = onClick
	) {
		Row(
			modifier = Modifier.padding(start = 10.dp, end = 7.dp, top = 5.dp, bottom = 5.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(text = label, style = Typography.headlineMedium)
			Icon(
				painterResource(id = R.drawable.icon_unfold_more),
				contentDescription = "Open picker",
				modifier = Modifier.padding(start = 6.dp),
				tint = TextSecondary
			)
		}
	}
	Row(
		modifier = Modifier.padding(top = 30.dp)
	) {
		if (content != null) {
			content()
		}
	}
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DropdownButtonPreview() {
	MoneyManagerTheme {
		DropdownButton(label = "This week", onClick = {})
	}
}