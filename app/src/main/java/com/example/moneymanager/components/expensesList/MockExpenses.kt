package com.example.moneymanager.components.expensesList

import com.example.moneymanager.models.expense.ExpenseResponse
import io.github.serpro69.kfaker.Faker
import java.time.LocalDate
import java.time.temporal.ChronoUnit

val faker = Faker()

val mockExpenses: List<ExpenseResponse> = List(30) { index ->
	ExpenseResponse(
		id = index.toString(),
		amount = faker.random.nextInt(min = 1, max = 999).toDouble() + faker.random.nextDouble(),
		date = LocalDate.now().minus(
			faker.random.nextInt(min = 0, max = 3).toLong(),
			ChronoUnit.DAYS
		).toString(),
		description = faker.australia.animals(),
		categoryId = "",
		userId = ""
	)
}