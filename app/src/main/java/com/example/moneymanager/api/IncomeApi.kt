package com.example.moneymanager.api

import com.example.moneymanager.models.income.IncomeRequest
import com.example.moneymanager.models.income.IncomeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IncomeApi {
	@GET("/api/income")
	suspend fun getIncomes(): Response<List<IncomeResponse>>

	@POST("/api/income")
	suspend fun addIncome(@Body incomeRequest: IncomeRequest): Response<IncomeResponse>

	@PUT("/api/income/{incomeId}")
	suspend fun updateIncome(@Path("incomeId") incomeId: String): Response<IncomeResponse>

	@DELETE("/api/income/{incomeId}")
	suspend fun deleteIncome(@Path("incomeId") incomeId: String): Response<Unit>
}