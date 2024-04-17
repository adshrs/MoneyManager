package com.example.moneymanager.utils

import androidx.compose.ui.graphics.Color

fun parseColorString(colorString: String): Color {
	val regex = Regex("Color\\((\\d+\\.\\d+), (\\d+\\.\\d+), (\\d+\\.\\d+), (\\d+\\.\\d+), .*\\)")
	val matchResult = regex.find(colorString)

	if (matchResult != null && matchResult.groupValues.size == 5) {
		val red = (matchResult.groupValues[1].toFloat() * 255).toInt()
		val green = (matchResult.groupValues[2].toFloat() * 255).toInt()
		val blue = (matchResult.groupValues[3].toFloat() * 255).toInt()
		val alpha = (matchResult.groupValues[4].toFloat() * 255).toInt()

		return Color(red, green, blue, alpha)
	} else {
		throw IllegalArgumentException("Invalid color string format: $colorString")
	}
}