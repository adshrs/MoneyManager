package com.example.moneymanager.models.expense

data class ExpenseRequest(
	var amount: Double,
	var date: String,
	var description: String,
	var categoryId: String,
)
