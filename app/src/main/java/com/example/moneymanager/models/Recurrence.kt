package com.example.moneymanager.models

sealed class Recurrence(val name: String, val target: String) {
	data object None: Recurrence("None", "None")
	data object Daily: Recurrence("Daily", "Day")
	data object Weekly: Recurrence("Weekly", "Week")
	data object Monthly: Recurrence("Monthly", "Month")
	data object Yearly: Recurrence("Yearly", "Year")
}