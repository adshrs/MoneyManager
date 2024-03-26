package com.example.moneymanager.components.charts

import android.graphics.Paint
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import com.example.moneymanager.ui.theme.TextSecondary
import com.github.tehras.charts.bar.renderer.label.LabelDrawer
import com.github.tehras.charts.piechart.utils.toLegacyInt

class LabelDrawer: LabelDrawer {
	private val paint = android.graphics.Paint().apply {
		this.textAlign = Paint.Align.CENTER
		this.color = TextSecondary.toLegacyInt()
		this.textSize = 38f
	}

	override fun drawLabel(
		drawScope: DrawScope,
		canvas: Canvas,
		label: String,
		barArea: Rect,
		xAxisArea: Rect
	) {
		canvas.nativeCanvas.drawText(
			label,
			barArea.left + 55f,
			barArea.bottom + 65f,
			paint
		)
	}
}