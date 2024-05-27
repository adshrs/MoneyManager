package com.example.moneymanager.components.charts

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.ui.theme.Destructive
import com.example.moneymanager.ui.theme.SystemGray04
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.BarDrawer

class BarDrawer(val recurrence: Recurrence, val type: String): BarDrawer {
	private val barPaint = Paint().apply {
		this.isAntiAlias = true
	}

	private val rightOffset = when(recurrence) {
		Recurrence.Weekly -> 35f
		Recurrence.Monthly -> 4f
		Recurrence.Yearly -> 15f
		else -> 0f
	}

	override fun drawBar(
		drawScope: DrawScope,
		canvas: Canvas,
		barArea: Rect,
		bar: BarChartData.Bar
	) {
		canvas.drawRoundRect(
			barArea.left,
			0f,
			barArea.right + rightOffset,
			barArea.bottom,
			6f,
			6f,
			barPaint.apply {
				color = SystemGray04
			}
		)
		canvas.drawRoundRect(
			barArea.left,
			barArea.top,
			barArea.right + rightOffset,
			barArea.bottom,
			6f,
			6f,
			barPaint.apply {
				color = if (type == "Expense") Destructive else	Color.Green
			}
		)
	}
}