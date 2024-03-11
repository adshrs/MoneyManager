package com.example.moneymanager.models

sealed class Recurrence(val name: String, val target: String) {
	data object None: Recurrence("None", "None")
	data object Daily: Recurrence("Daily", "Today")
	data object Weekly: Recurrence("Weekly", "This week")
	data object Monthly: Recurrence("Monthly", "This month")
	data object Yearly: Recurrence("Yearly", "This year")
}