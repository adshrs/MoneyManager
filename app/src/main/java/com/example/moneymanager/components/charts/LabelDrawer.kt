package com.example.moneymanager.components.charts

import android.graphics.Paint
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.ui.theme.TextSecondary
import com.github.tehras.charts.bar.renderer.label.LabelDrawer
import com.github.tehras.charts.piechart.utils.toLegacyInt

class LabelDrawer(val recurrence: Recurrence, val lastDay: Int? = -1) : LabelDrawer {
	private val labelTextSize = when (recurrence) {
		Recurrence.Weekly -> 36f
		Recurrence.Monthly -> 36f
		Recurrence.Yearly -> 36f
		else -> 0f
	}

	private val leftOffset = when (recurrence) {
		Recurrence.Weekly -> 55f
		Recurrence.Monthly -> 10f
		Recurrence.Yearly -> 28f
		else -> 0f
	}

	private val bottomOffset = when (recurrence) {
		Recurrence.Weekly -> 65f
		Recurrence.Monthly -> 60f
		Recurrence.Yearly -> 65f
		else -> 0f
	}

	private val paint = android.graphics.Paint().apply {
		this.textAlign = Paint.Align.CENTER
		this.color = TextSecondary.toLegacyInt()
		this.textSize = labelTextSize
	}

	override fun drawLabel(
		drawScope: DrawScope,
		canvas: Canvas,
		label: String,
		barArea: Rect,
		xAxisArea: Rect
	) {
		val monthlyCondition =
			recurrence == Recurrence.Monthly && (
				Integer.parseInt(label) % 8 == 0 ||
			   Integer.parseInt(label) == 1 ||
				Integer.parseInt(label) == lastDay
			)

		if (monthlyCondition || recurrence != Recurrence.Monthly) {
			canvas.nativeCanvas.drawText(
				label,
				barArea.left + leftOffset,
				barArea.bottom + bottomOffset,
				paint
			)
		}
	}
}