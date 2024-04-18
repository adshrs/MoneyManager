package com.example.moneymanager.repository

import com.example.moneymanager.api.ExpenseApi
import com.example.moneymanager.models.expense.ExpenseRequest
import com.example.moneymanager.models.expense.ExpenseResponse
import retrofit2.Response
import javax.inject.Inject

class ExpenseRepository @Inject constructor(private val expenseApi: ExpenseApi) {

	suspend fun getExpenses(): Response<List<ExpenseResponse>> {
		return expenseApi.getExpenses()
	}

	suspend fun addExpense(expenseRequest: ExpenseRequest): Response<ExpenseResponse> {
		return expenseApi.addExpense(expenseRequest)
	}

	suspend fun deleteExpense(expenseId: String): Response<Unit> {
		return expenseApi.deleteExpense(expenseId)
	}
}