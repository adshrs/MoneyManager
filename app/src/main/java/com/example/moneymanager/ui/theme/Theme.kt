package com.example.moneymanager.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
	primary = Primary,
	background = Surface,
	surface = Surface,
	error = Destructive,
	onPrimary = TextPrimary,
	onSecondary = TextPrimary,
	onBackground = TextPrimary,
	onSurface = TextPrimary
)

	/* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */


@Composable
fun MoneyManagerTheme(
	darkTheme: Boolean = true,
	// Dynamic color is available on Android 12+
	dynamicColor: Boolean = true,
	content: @Composable () -> Unit
) {
	val colorScheme = when {

		darkTheme -> DarkColorScheme
		else -> DarkColorScheme
	}
	val view = LocalView.current
	if (!view.isInEditMode) {
		SideEffect {
			val window = (view.context as Activity).window
			window.statusBarColor = colorScheme.background.toArgb()
			window.navigationBarColor = colorScheme.background.toArgb()
			WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
		}
	}

	MaterialTheme(
		colorScheme = colorScheme,
		typography = Typography,
		content = content
	)
}