package com.example.moneymanager.models.income

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class IncomeResponse(
	val id: String,
	var amount: Double,
	var date: String,
	var description: String,
	val userId: String
)

data class DayIncomes(
	val incomes: MutableList<IncomeResponse>,
	var total: Double
)

fun List<IncomeResponse>.groupByDay(): Map<LocalDate, DayIncomes> {
	val dataMap: MutableMap<LocalDate, DayIncomes> = mutableMapOf()

	this.forEach { income ->
		val date = LocalDate.parse(income.date, DateTimeFormatter.ISO_DATE)

		if (dataMap[date] == null) {
			dataMap[date] = DayIncomes(
				incomes = mutableListOf(),
				total	= 0.0
			)
		}

		dataMap[date]!!.incomes.add(income)
		dataMap[date]!!.total = dataMap[date]!!.total.plus(income.amount)
	}

	return  dataMap.toSortedMap(compareByDescending { it })
}

fun List<IncomeResponse>.groupByDayOfWeek(): Map<String, DayIncomes> {
	val dataMap: MutableMap<String, DayIncomes> = mutableMapOf()

	this.forEach { income ->
		val date = LocalDate.parse(income.date, DateTimeFormatter.ISO_DATE)
		val dayOfWeek = date.dayOfWeek

		if (dataMap[dayOfWeek.name] == null) {
			dataMap[dayOfWeek.name] = DayIncomes(
				incomes = mutableListOf(),
				total	= 0.0
			)
		}

		dataMap[dayOfWeek.name]!!.incomes.add(income)
		dataMap[dayOfWeek.name]!!.total = dataMap[dayOfWeek.name]!!.total.plus(income.amount)
	}

	return  dataMap.toSortedMap(compareByDescending { it })
}

fun List<IncomeResponse>.groupByDayOfMonth(): Map<Int, DayIncomes> {
	val dataMap: MutableMap<Int, DayIncomes> = mutableMapOf()

	this.forEach { income ->
		val date = LocalDate.parse(income.date, DateTimeFormatter.ISO_DATE)
		val dayOfMonth = date.dayOfMonth

		if (dataMap[dayOfMonth] == null) {
			dataMap[dayOfMonth] = DayIncomes(
				incomes = mutableListOf(),
				total	= 0.0
			)
		}

		dataMap[dayOfMonth]!!.incomes.add(income)
		dataMap[dayOfMonth]!!.total = dataMap[dayOfMonth]!!.total.plus(income.amount)
	}

	return  dataMap.toSortedMap(compareByDescending { it })
}

fun List<IncomeResponse>.groupByMonth(): Map<String, DayIncomes> {
	val dataMap: MutableMap<String, DayIncomes> = mutableMapOf()

	this.forEach { income ->
		val date = LocalDate.parse(income.date, DateTimeFormatter.ISO_DATE)
		val month = date.month

		if (dataMap[month.name] == null) {
			dataMap[month.name] = DayIncomes(
				incomes = mutableListOf(),
				total	= 0.0
			)
		}

		dataMap[month.name]!!.incomes.add(income)
		dataMap[month.name]!!.total = dataMap[month.name]!!.total.plus(income.amount)
	}

	return  dataMap.toSortedMap(compareByDescending { it })
}