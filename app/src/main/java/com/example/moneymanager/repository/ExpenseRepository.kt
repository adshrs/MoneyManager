package com.example.moneymanager.repository

import com.example.moneymanager.api.ExpenseApi
import com.example.moneymanager.models.expense.ExpenseRequest
import com.example.moneymanager.models.expense.ExpenseResponse
import com.example.moneymanager.utils.NetworkResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class ExpenseRepository @Inject constructor(private val expenseApi: ExpenseApi) {

	private val statusResultChannel = Channel<NetworkResult<String>>()
	val statusNetworkResults = statusResultChannel.receiveAsFlow()

	suspend fun getExpenses(): Response<List<ExpenseResponse>> {
		return expenseApi.getExpenses()
	}

	suspend fun addExpense(expenseRequest: ExpenseRequest): Response<ExpenseResponse> {
		return expenseApi.addExpense(expenseRequest)
	}

	suspend fun deleteExpense(expenseId: String) {
		statusResultChannel.send(NetworkResult.Loading())

		val response = expenseApi.deleteExpense(expenseId)
		if (response.isSuccessful) {
			statusResultChannel.send(NetworkResult.Success("Expense Deleted"))
		}
		else if (response.errorBody() != null) {
			val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
			statusResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
		}
		else {
			statusResultChannel.send(NetworkResult.Error("Something went wrong"))
		}
	}
}