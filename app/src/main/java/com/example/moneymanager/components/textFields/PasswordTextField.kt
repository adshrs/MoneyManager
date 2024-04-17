package com.example.moneymanager.components.textFields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PasswordTextField(
	modifier: Modifier,
	label: @Composable (() -> Unit)?,
	value: String,
	trailingIcon: @Composable (() -> Unit),
	onValueChange: (String)-> Unit,
	visualTransformation : VisualTransformation
) {
	OutlinedTextField(
		label = label,
		value = value,
		onValueChange = onValueChange,
		modifier = modifier
			.padding(horizontal = 16.dp, vertical = 8.dp)
			.fillMaxWidth(),
		keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
		shape = RoundedCornerShape(10.dp),
		trailingIcon = trailingIcon,
		visualTransformation = visualTransformation,
		singleLine = true
	)
}