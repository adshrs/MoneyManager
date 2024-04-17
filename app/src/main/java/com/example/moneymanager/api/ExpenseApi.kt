package com.example.moneymanager.api

import com.example.moneymanager.models.expense.ExpenseRequest
import com.example.moneymanager.models.expense.ExpenseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ExpenseApi {
	@GET("/api/expense")
	suspend fun getExpenses(): Response<List<ExpenseResponse>>

	@POST("/api/expense")
	suspend fun addExpense(@Body expenseRequest: ExpenseRequest): Response<ExpenseResponse>

	@PUT("/api/expense/{expenseId}")
	suspend fun updateExpense(@Path("expenseId") expenseId: String): Response<ExpenseResponse>

	@DELETE("/api/expense/{expenseId}")
	suspend fun deleteExpense(@Path("expenseId") expenseId: String): Response<Unit>
}