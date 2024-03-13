package com.example.moneymanager.components.expensesList

import androidx.compose.ui.graphics.Color
import com.example.moneymanager.models.Category
import com.example.moneymanager.models.Expense
import com.example.moneymanager.models.Recurrence
import io.github.serpro69.kfaker.Faker
import java.time.LocalDate
import java.time.temporal.ChronoUnit

val faker = Faker()

val mockExpenses: List<Expense> = List(30) {index ->
	Expense(
		id = index,
		amount = faker.random.nextInt(min = 1, max = 999).toDouble() + faker.random.nextDouble(),
		date = LocalDate.now().minus(
			faker.random.nextInt(min = 0, max = 3).toLong(),
			ChronoUnit.DAYS
		),
		recurrence = faker.random.randomValue(
			listOf(
				Recurrence.None,
				Recurrence.Daily,
				Recurrence.Weekly,
				Recurrence.Monthly,
				Recurrence.Yearly
			)
		),
		note = faker.australia.animals(),
		category = faker.random.randomValue(
			listOf(
				Category("Groceries", Color.Red),
				Category("Food", Color.Blue),
				Category("Stationary", Color.White),
				Category("Cosmetics", Color.Green),
				Category("Furniture", Color.Yellow),
				Category("Electronics", Color.Cyan)
			)
		)
	)
}