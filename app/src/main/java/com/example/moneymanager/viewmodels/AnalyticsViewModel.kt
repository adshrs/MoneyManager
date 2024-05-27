package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.models.category.CategoryResponse
import com.example.moneymanager.models.expense.ExpenseResponse
import com.example.moneymanager.models.income.IncomeResponse
import com.example.moneymanager.repository.CategoryRepository
import com.example.moneymanager.repository.ExpenseRepository
import com.example.moneymanager.repository.IncomeRepository
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

data class AnalyticsScreenState(
	val analyticsType: String = "Expense",
	val recurrence: Recurrence = Recurrence.Weekly,
	val dateRangeMenuOpened: Boolean = false,
	var expenses: List<ExpenseResponse> = listOf(),
	var incomes: List<IncomeResponse> = listOf(),
	var categories: List<CategoryResponse> = listOf(),
	var isLoading: Boolean = false
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
	private val expenseRepository: ExpenseRepository,
	private val incomeRepository: IncomeRepository,
	private val categoryRepository: CategoryRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(AnalyticsScreenState())
	val uiState: StateFlow<AnalyticsScreenState> = _uiState.asStateFlow()

	private val _expensesDayGroupUiState = MutableStateFlow(ExpensesDayGroupState())
	val expensesDayGroupUiState: StateFlow<ExpensesDayGroupState> = _expensesDayGroupUiState.asStateFlow()

	private val _incomesDayGroupUiState = MutableStateFlow(IncomesDayGroupState())
	val incomesDayGroupUiState: StateFlow<IncomesDayGroupState> = _incomesDayGroupUiState.asStateFlow()

	private val expenseResultChannel = Channel<NetworkResult<List<ExpenseResponse>>>()
	val expenseNetworkResults = expenseResultChannel.receiveAsFlow()

	private val incomeResultChannel = Channel<NetworkResult<List<IncomeResponse>>>()
	val incomeNetworkResults = incomeResultChannel.receiveAsFlow()

	private val categoryResultChannel = Channel<NetworkResult<List<CategoryResponse>>>()
	val categoryNetworkResults = categoryResultChannel.receiveAsFlow()

	private val statusResultChannel = Channel<NetworkResult<String>>()
	val statusNetworkResults = statusResultChannel.receiveAsFlow()

	@Inject
	lateinit var tokenManager: TokenManager

	init {
		getCategories()
		getExpenses()
		getIncomes()
	}

	fun updateAnalyticsTypeSelection(analyticsType: String) {
		_uiState.update {
			it.copy(
				analyticsType = analyticsType
			)
		}
	}

	fun showExpenseDeleteWarning(expenseId: String) {
		_expensesDayGroupUiState.update {
			it.copy(
				deleteWarningVisible = true,
				expenseIdToDelete = expenseId
			)
		}
	}

	fun showIncomeDeleteWarning(incomeId: String) {
		_incomesDayGroupUiState.update {
			it.copy(
				deleteWarningVisible = true,
				incomeIdToDelete = incomeId
			)
		}
	}

	fun hideDeleteWarning() {
		_expensesDayGroupUiState.update {
			it.copy(
				deleteWarningVisible = false,
				expenseIdToDelete = ""
			)
		}
		_incomesDayGroupUiState.update {
			it.copy(
				deleteWarningVisible = false,
				incomeIdToDelete = ""
			)
		}
	}

	fun setRecurrence(recurrence: Recurrence) {
		_uiState.update {
			it.copy(
				recurrence = recurrence
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

	fun updateCategories(categories: List<CategoryResponse>) {
		_uiState.update {
			it.copy(
				categories = _uiState.value.categories.plus(categories)
			)
		}
	}

	fun openDateRangeMenu() {
		_uiState.update {
			it.copy(
				dateRangeMenuOpened = true
			)
		}
	}

	fun closeDateRangeMenu() {
		_uiState.update {
			it.copy(
				dateRangeMenuOpened = false
			)
		}
	}

	private fun getCategories() {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = categoryRepository.getCategories("Expense")

			if (response.isSuccessful && response.body() != null) {
				categoryResultChannel.send(NetworkResult.Success(response.body()!!))
			}
			else if (response.errorBody() != null) {
				val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
				categoryResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
			}
			else {
				categoryResultChannel.send(NetworkResult.Error("Something went wrong"))
			}
			_uiState.update { it.copy(isLoading = false) }
		}

		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = categoryRepository.getCategories("Income")

			if (response.isSuccessful && response.body() != null) {
				categoryResultChannel.send(NetworkResult.Success(response.body()!!))
			}
			else if (response.errorBody() != null) {
				val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
				categoryResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
			}
			else {
				categoryResultChannel.send(NetworkResult.Error("Something went wrong"))
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

	fun deleteExpense(expenseId: String) {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = expenseRepository.deleteExpense(expenseId)
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
			_uiState.update { it.copy(isLoading = false) }
		}
	}

	fun deleteIncome(incomeId: String) {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = incomeRepository.deleteIncome(incomeId)
			if (response.isSuccessful) {
				statusResultChannel.send(NetworkResult.Success("Income Deleted"))
			}
			else if (response.errorBody() != null) {
				val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
				statusResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
			}
			else {
				statusResultChannel.send(NetworkResult.Error("Something went wrong"))
			}
			_uiState.update { it.copy(isLoading = false) }
		}
	}

	fun removeToken() {
		tokenManager.deleteToken()
	}
}