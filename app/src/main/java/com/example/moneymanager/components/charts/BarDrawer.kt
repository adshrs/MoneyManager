package com.example.moneymanager.components.charts

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.moneymanager.ui.theme.Primary
import com.example.moneymanager.ui.theme.SystemGray04
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.BarDrawer

class BarDrawer: BarDrawer {
	private val barPaint = Paint().apply {
		this.isAntiAlias = true
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
			barArea.right + 35f,
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
			barArea.right + 35f,
			barArea.bottom,
			6f,
			6f,
			barPaint.apply {
				color = Primary
			}
		)
	}
}