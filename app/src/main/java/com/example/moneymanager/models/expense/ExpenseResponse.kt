package com.example.moneymanager.models.expense

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ExpenseResponse(
	val id: String,
	var amount: Double,
	var date: String,
	var description: String,
	var categoryId: String,
	val userId: String
)

data class DayExpenses(
	val expenses: MutableList<ExpenseResponse>,
	var total: Double
)

fun List<ExpenseResponse>.groupByDay(): Map<LocalDate, DayExpenses> {
	val dataMap: MutableMap<LocalDate, DayExpenses> = mutableMapOf()

	this.forEach { expense ->
		val date = LocalDate.parse(expense.date, DateTimeFormatter.ISO_DATE)

		if (dataMap[date] == null) {
			dataMap[date] = DayExpenses(
				expenses = mutableListOf(),
				total	= 0.0
			)
		}

		dataMap[date]!!.expenses.add(expense)
		dataMap[date]!!.total = dataMap[date]!!.total.plus(expense.amount)
	}

	return  dataMap.toSortedMap(compareByDescending { it })
}

fun List<ExpenseResponse>.groupByDayOfWeek(): Map<String, DayExpenses> {
	val dataMap: MutableMap<String, DayExpenses> = mutableMapOf()

	this.forEach { expense ->
		val date = LocalDate.parse(expense.date, DateTimeFormatter.ISO_DATE)
		val dayOfWeek = date.dayOfWeek

		if (dataMap[dayOfWeek.name] == null) {
			dataMap[dayOfWeek.name] = DayExpenses(
				expenses = mutableListOf(),
				total	= 0.0
			)
		}

		dataMap[dayOfWeek.name]!!.expenses.add(expense)
		dataMap[dayOfWeek.name]!!.total = dataMap[dayOfWeek.name]!!.total.plus(expense.amount)
	}

	return  dataMap.toSortedMap(compareByDescending { it })
}

fun List<ExpenseResponse>.groupByDayOfMonth(): Map<Int, DayExpenses> {
	val dataMap: MutableMap<Int, DayExpenses> = mutableMapOf()

	this.forEach { expense ->
		val date = LocalDate.parse(expense.date, DateTimeFormatter.ISO_DATE)
		val dayOfMonth = date.dayOfMonth

		if (dataMap[dayOfMonth] == null) {
			dataMap[dayOfMonth] = DayExpenses(
				expenses = mutableListOf(),
				total	= 0.0
			)
		}

		dataMap[dayOfMonth]!!.expenses.add(expense)
		dataMap[dayOfMonth]!!.total = dataMap[dayOfMonth]!!.total.plus(expense.amount)
	}

	return  dataMap.toSortedMap(compareByDescending { it })
}

fun List<ExpenseResponse>.groupByMonth(): Map<String, DayExpenses> {
	val dataMap: MutableMap<String, DayExpenses> = mutableMapOf()

	this.forEach { expense ->
		val date = LocalDate.parse(expense.date, DateTimeFormatter.ISO_DATE)
		val month = date.month

		if (dataMap[month.name] == null) {
			dataMap[month.name] = DayExpenses(
				expenses = mutableListOf(),
				total	= 0.0
			)
		}

		dataMap[month.name]!!.expenses.add(expense)
		dataMap[month.name]!!.total = dataMap[month.name]!!.total.plus(expense.amount)
	}

	return  dataMap.toSortedMap(compareByDescending { it })
}


