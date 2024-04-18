package com.example.moneymanager.models.income

data class IncomeRequest(
	var amount: Double,
	var date: String,
	var description: String,
)
