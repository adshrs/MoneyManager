package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.models.expense.ExpenseResponse
import com.example.moneymanager.models.income.IncomeResponse
import com.example.moneymanager.models.user.UserResponse
import com.example.moneymanager.repository.ExpenseRepository
import com.example.moneymanager.repository.IncomeRepository
import com.example.moneymanager.repository.UserRepository
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

data class MenuScreenState(
	val user: UserResponse? = null,
	var expenses: List<ExpenseResponse> = listOf(),
	var incomes: List<IncomeResponse> = listOf(),
	val totalExpense: Double = 0.0,
	val totalIncome: Double = 0.0,
	var isLoading: Boolean = false
)

@HiltViewModel
class MenuViewModel @Inject constructor(
	private val userRepository: UserRepository,
	private val expenseRepository: ExpenseRepository,
	private val incomeRepository: IncomeRepository
): ViewModel() {

	private val _uiState = MutableStateFlow(MenuScreenState())
	val uiState: StateFlow<MenuScreenState> = _uiState.asStateFlow()

	private val userResultChannel = Channel<NetworkResult<UserResponse>>()
	val userNetworkResults = userResultChannel.receiveAsFlow()

	private val expenseResultChannel = Channel<NetworkResult<List<ExpenseResponse>>>()
	val expenseNetworkResults = expenseResultChannel.receiveAsFlow()

	private val incomeResultChannel = Channel<NetworkResult<List<IncomeResponse>>>()
	val incomeNetworkResults = incomeResultChannel.receiveAsFlow()

	private val statusResultChannel = Channel<NetworkResult<String>>()
	val statusNetworkResults = statusResultChannel.receiveAsFlow()

	@Inject
	lateinit var tokenManager: TokenManager

	init {
		getUserDetails()
		getExpenses()
		getIncomes()
	}

	fun updateUser(user: UserResponse) {
		_uiState.update {
			it.copy(
				user = user
			)
		}
	}

	fun updateExpenses(expenses: List<ExpenseResponse>) {
		_uiState.update {
			it.copy(
				expenses = expenses
			)
		}
	}

	fun updateIncomes(incomes: List<IncomeResponse>) {
		_uiState.update {
			it.copy(
				incomes = incomes
			)
		}
	}

	fun updateTotalExpense(expenses: List<ExpenseResponse>) {
		_uiState.update {
			it.copy(
				totalExpense = expenses.sumOf { it.amount }
			)
		}
	}

	fun updateTotalIncome(incomes: List<IncomeResponse>) {
		_uiState.update {
			it.copy(
				totalIncome = incomes.sumOf { it.amount }
			)
		}
	}

	private fun getUserDetails() {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = userRepository.getUserDetails()

			if (response.isSuccessful && response.body() != null) {
				userResultChannel.send(NetworkResult.Success(response.body()!!))
			}
			else if (response.errorBody() != null) {
				val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
				userResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
			}
			else {
				userResultChannel.send(NetworkResult.Error("Something went wrong"))
			}
			_uiState.update { it.copy(isLoading = false) }
		}
	}

	private fun getExpenses() {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = expenseRepository.getExpenses()

			if (response.isSuccessful && response.body() != null) {
				expenseResultChannel.send(NetworkResult.Success(response.body()!!))
			}
			else if (response.errorBody() != null) {
				val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
				expenseResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
			}
			else {
				expenseResultChannel.send(NetworkResult.Error("Something went wrong"))
			}
			_uiState.update { it.copy(isLoading = false) }
		}
	}

	private fun getIncomes() {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = incomeRepository.getIncomes()

			if (response.isSuccessful && response.body() != null) {
				incomeResultChannel.send(NetworkResult.Success(response.body()!!))
			}
			else if (response.errorBody() != null) {
				val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
				incomeResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
			}
			else {
				incomeResultChannel.send(NetworkResult.Error("Something went wrong"))
			}
			_uiState.update { it.copy(isLoading = false) }
		}
	}

	fun removeToken() {
		tokenManager.deleteToken()
	}
}