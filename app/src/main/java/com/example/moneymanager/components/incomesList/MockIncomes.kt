package com.example.moneymanager.components.incomesList

import com.example.moneymanager.models.income.IncomeResponse
import io.github.serpro69.kfaker.Faker
import java.time.LocalDate
import java.time.temporal.ChronoUnit

val faker = Faker()

val mockIncomes: List<IncomeResponse> = List(30) { index ->
	IncomeResponse(
		id = index.toString(),
		amount = faker.random.nextInt(min = 1, max = 999).toDouble() + faker.random.nextDouble(),
		date = LocalDate.now().minus(
			faker.random.nextInt(min = 0, max = 3).toLong(),
			ChronoUnit.DAYS
		).toString(),
		description = faker.australia.animals(),
		userId = ""
	)
}