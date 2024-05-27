package com.example.moneymanager.repository

import com.example.moneymanager.api.IncomeApi
import com.example.moneymanager.models.income.IncomeRequest
import com.example.moneymanager.models.income.IncomeResponse
import retrofit2.Response
import javax.inject.Inject

class IncomeRepository @Inject constructor(private val incomeApi: IncomeApi) {

	suspend fun getIncomes(): Response<List<IncomeResponse>> {
		return incomeApi.getIncomes()
	}

	suspend fun addIncome(incomeRequest: IncomeRequest): Response<IncomeResponse> {
		return incomeApi.addIncome(incomeRequest)
	}

	suspend fun deleteIncome(incomeId: String): Response<Unit> {
		return incomeApi.deleteIncome(incomeId)
	}
}