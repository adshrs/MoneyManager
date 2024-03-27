package com.example.moneymanager.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.moneymanager.ui.theme.Primary

@Composable
fun LoadingIndicator(
	size: Dp = 32.dp,
	sweepAngle: Float = 90f,
	color: androidx.compose.ui.graphics.Color = Primary,
	strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth
) {
	val transition = rememberInfiniteTransition(label = "")

	// define the changing value from 0 to 360.
	// This is the angle of the beginning of indicator arc
	// this value will change over time from 0 to 360 and repeat indefinitely.
	// it changes starting position of the indicator arc and the animation is obtained
	val currentArcStartAngle by transition.animateValue(
		0,
		360,
		Int.VectorConverter,
		infiniteRepeatable(
			animation = tween(
				durationMillis = 1100,
				easing = LinearEasing
			)
		), label = ""
	)

	////// draw /////

	// define stroke with given width and arc ends type considering device DPI
	val stroke = with(LocalDensity.current) {
		Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Square)
	}

	// draw on canvas
	Canvas(
		Modifier
			.progressSemantics() // (optional) for Accessibility services
			.size(size) // canvas size
			.padding(strokeWidth / 2) //padding. otherwise, not the whole circle will fit in the canvas
	) {
		// draw "background" (gray) circle with defined stroke.
		// without explicit center and radius it fit canvas bounds
		drawCircle(androidx.compose.ui.graphics.Color.LightGray, style = stroke)

		// draw arc with the same stroke
		drawArc(
			color,
			// arc start angle
			// -90 shifts the start position towards the y-axis
			startAngle = currentArcStartAngle.toFloat() - 90,
			sweepAngle = sweepAngle,
			useCenter = false,
			style = stroke
		)
	}
}