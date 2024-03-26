package com.example.moneymanager.models

import java.time.LocalDate

data class Expense(
	val id: Int,
	val amount: Double,
	val date: LocalDate,
	val recurrence: Recurrence,
	val note: String,
	val category: Category
)

data class DayExpenses(
	val expenses: MutableList<Expense>,
	var total: Double
)

fun List<Expense>.groupByDay(): Map<LocalDate, DayExpenses> {
	val dataMap: MutableMap<LocalDate, DayExpenses> = mutableMapOf()

	this.forEach { expense ->
		val date = expense.date

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

fun List<Expense>.groupByDayOfWeek(): Map<String, DayExpenses> {
	val dataMap: MutableMap<String, DayExpenses> = mutableMapOf()

	this.forEach { expense ->
		val dayOfWeek = expense.date.dayOfWeek

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

