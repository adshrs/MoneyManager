package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.models.category.CategoryResponse
import com.example.moneymanager.models.expense.ExpenseResponse
import com.example.moneymanager.repository.CategoryRepository
import com.example.moneymanager.repository.ExpenseRepository
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
	val recurrence: Recurrence = Recurrence.Weekly,
	val dateRangeMenuOpened: Boolean = false,
	var expenses: List<ExpenseResponse> = listOf(),
	var categories: List<CategoryResponse> = listOf(),
	var isLoading: Boolean = false
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
	private val expenseRepository: ExpenseRepository,
	private val categoryRepository: CategoryRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(AnalyticsScreenState())
	val uiState: StateFlow<AnalyticsScreenState> = _uiState.asStateFlow()

	private val _expensesDayGroupUiState = MutableStateFlow(ExpensesDayGroupState())
	val expensesDayGroupUiState: StateFlow<ExpensesDayGroupState> = _expensesDayGroupUiState.asStateFlow()

	private val expenseResultChannel = Channel<NetworkResult<List<ExpenseResponse>>>()
	val expenseNetworkResults = expenseResultChannel.receiveAsFlow()

	private val categoryResultChannel = Channel<NetworkResult<List<CategoryResponse>>>()
	val categoryNetworkResults = categoryResultChannel.receiveAsFlow()

	private val statusResultChannel = Channel<NetworkResult<String>>()
	val statusNetworkResults = statusResultChannel.receiveAsFlow()

	@Inject
	lateinit var tokenManager: TokenManager

	init {
		getCategories()
		getExpenses()
	}

	fun showDeleteWarning(categoryId: String) {
		_expensesDayGroupUiState.update {
			it.copy(
				deleteWarningVisible = true,
				expenseIdToDelete = categoryId
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

	fun updateCategories(categories: List<CategoryResponse>) {
		_uiState.update {
			it.copy(
				categories = categories
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
			val response = categoryRepository.getCategories()

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

		getCategories()
	}

	fun removeToken() {
		tokenManager.deleteToken()
	}
}