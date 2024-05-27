package com.example.moneymanager.utils

import com.example.moneymanager.models.expense.ExpenseResponse
import com.example.moneymanager.models.income.IncomeResponse
import com.itextpdf.kernel.colors.Color
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine
import com.itextpdf.layout.Document
import com.itextpdf.layout.Style
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.LineSeparator
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.Leading
import com.itextpdf.layout.property.Property
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.VerticalAlignment
import java.io.OutputStream

data class ReportContent(
	val name: String,
	val totalExpense: Double,
	val expenseData: List<ExpenseResponse>,
	val totalIncome: Double,
	val incomeData: List<IncomeResponse>
)

fun generatePdf(reportContent: ReportContent, outputStream: OutputStream) {
	val pdfWriter = PdfWriter(outputStream)
	val pdfDocument = PdfDocument(pdfWriter)
	val document = Document(pdfDocument, PageSize.A4).apply {
		setMargins(50f, 30f, 50f, 30f)
		setProperty(
			Property.LEADING,
			Leading(Leading.MULTIPLIED, 1f)
		)
	}.setWordSpacing(0f)

	val page = pdfDocument.addNewPage()

	//Title
	val reportTitle = Paragraph("Expense & Income Report")
		.setBold()
		.setFontSize(24f)

	//User's Name
	val name = createLightText(reportContent.name)

	//Top Section Separator
	val line = LineSeparator(
		SolidLine().apply {
			color = DeviceRgb(204, 204, 204)
		}
	).setMarginTop(20f)

	//Summary Info Title
	val summary = Paragraph("Summary")
		.setBold()
		.setFontSize(18f)
		.apply {
			setMarginTop(30f)
			setMarginBottom(20f)
		}

	//Summary Info
	val totalExpense = Paragraph("Total Expense: ${reportContent.totalExpense}")
		.setBold()
		.setFontSize(14f)
		.setFontColor(DeviceRgb(204, 18, 18))
		.setTextAlignment(TextAlignment.LEFT)

	val totalIncome = Paragraph("Total Income: ${reportContent.totalIncome}")
		.setBold()
		.setFontSize(14f)
		.setFontColor(DeviceRgb(18, 204, 30))
		.setTextAlignment(TextAlignment.CENTER)

	val difference = Paragraph("Difference: ${reportContent.totalIncome - reportContent.totalExpense}")
		.setBold()
		.setFontSize(14f)
		.setFontColor(DeviceRgb(92, 92, 92))
		.setTextAlignment(TextAlignment.RIGHT)

	val summaryTable = Table(3, true)

	summaryTable.addCell(createNoBorderCell(totalExpense))
	summaryTable.addCell(createNoBorderCell(totalIncome))
	summaryTable.addCell(createNoBorderCell(difference))

	//Expenses Table Title
	val expenses = Paragraph("Expenses")
		.setBold()
		.setFontSize(18f)
		.apply {
			setMarginTop(60f)
			setMarginBottom(10f)
		}

	//Expenses Table
	val expenseDescription =
		createBoldText("Description").setTextAlignment(TextAlignment.LEFT)
	val expenseDate = createBoldText("Date").setTextAlignment(TextAlignment.LEFT)
	val expenseAmount = createBoldText("Amount").setTextAlignment(TextAlignment.RIGHT)

	val expensesTable = Table(3, true)

	expensesTable.addCell(createProductTableCell(expenseDescription))
	expensesTable.addCell(createProductTableCell(expenseDate))
	expensesTable.addCell(createProductTableCell(expenseAmount))

	val expensesLighterBlack = DeviceRgb(64, 64, 64)
	reportContent.expenseData.forEach {
		val pDescription = createBoldText(
			it.description,
			expensesLighterBlack
		).setTextAlignment(TextAlignment.LEFT)

		val pDate = createBoldText(
			it.date,
			expensesLighterBlack
		).setTextAlignment(TextAlignment.LEFT)

		val pAmount = createBoldText(
			"Rs. ${it.amount}",
			expensesLighterBlack
		).setTextAlignment(TextAlignment.RIGHT)

		expensesTable.addCell(createProductTableCell(pDescription))
		expensesTable.addCell(createProductTableCell(pDate))
		expensesTable.addCell(createProductTableCell(pAmount))
	}

	//Incomes Table Title
	val incomes = Paragraph("Incomes")
		.setBold()
		.setFontSize(18f)
		.apply {
			setMarginTop(60f)
			setMarginBottom(10f)
		}

	//Incomes Table
	val incomeDescription =
		createBoldText("Description").setTextAlignment(TextAlignment.LEFT)
	val incomeDate = createBoldText("Date").setTextAlignment(TextAlignment.LEFT)
	val incomeAmount = createBoldText("Amount").setTextAlignment(TextAlignment.RIGHT)

	val incomesTable = Table(3, true)

	incomesTable.addCell(createProductTableCell(incomeDescription))
	incomesTable.addCell(createProductTableCell(incomeDate))
	incomesTable.addCell(createProductTableCell(incomeAmount))

	val incomesLighterBlack = DeviceRgb(64, 64, 64)
	reportContent.incomeData.forEach {
		val pDescription = createBoldText(
			it.description,
			incomesLighterBlack
		).setTextAlignment(TextAlignment.LEFT)

		val pDate = createBoldText(
			it.date,
			incomesLighterBlack
		).setTextAlignment(TextAlignment.LEFT)

		val pAmount = createBoldText(
			"Rs. ${it.amount}",
			incomesLighterBlack
		).setTextAlignment(TextAlignment.RIGHT)

		incomesTable.addCell(createProductTableCell(pDescription))
		incomesTable.addCell(createProductTableCell(pDate))
		incomesTable.addCell(createProductTableCell(pAmount))
	}

	document.add(reportTitle)
	document.add(name)
	document.add(line)
	document.add(summary)
	document.add(summaryTable)
	document.add(expenses)
	document.add(expensesTable)
	document.add(incomes)
	document.add(incomesTable)

	document.close()
}

fun createLightText(text: String): Paragraph {
	val lightTextStyle = Style().apply {
		setFontSize(14f)
		setFontColor(DeviceRgb(92, 92, 92))
	}
	return Paragraph(text).addStyle(lightTextStyle)
}

private fun createBoldText(text: String, color: Color = DeviceRgb.BLACK): Paragraph {
	val boldTextStyle = Style().apply {
		setFontSize(14f)
		setFontColor(color)
		setVerticalAlignment(VerticalAlignment.MIDDLE)
	}
	return Paragraph(text).addStyle(boldTextStyle)
}

private fun createNoBorderCell(paragraph: Paragraph): Cell {
	return Cell().add(paragraph).setBorder(null)
}

private fun createProductTableCell(paragraph: Paragraph): Cell {
	return Cell().add(paragraph).apply {
		setPaddingBottom(20f)
		setPaddingTop(15f)
		setBorder(null)
		setBorderBottom(SolidBorder(DeviceRgb(204, 204, 204), 1f))
	}
}
