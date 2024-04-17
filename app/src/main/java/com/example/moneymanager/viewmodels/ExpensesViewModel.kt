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
import com.example.moneymanager.utils.calculateDateRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

data class ExpensesScreenState(
	val recurrence: Recurrence = Recurrence.Daily,
	val sumTotal: Double = 0.0,
	var recurrenceMenuOpened: Boolean = false,
	var expenses: List<ExpenseResponse> = listOf(),
	var filteredExpenses: List<ExpenseResponse> = listOf(),
	var categories: List<CategoryResponse> = listOf(),
	var isLoading: Boolean = false
)

@HiltViewModel
class ExpensesViewModel @Inject constructor(
	private val expenseRepository: ExpenseRepository,
	private val categoryRepository: CategoryRepository
): ViewModel() {

	private val _uiState = MutableStateFlow(ExpensesScreenState())
	val uiState: StateFlow<ExpensesScreenState> = _uiState.asStateFlow()

	private val expenseResultChannel = Channel<NetworkResult<List<ExpenseResponse>>>()
	val expenseNetworkResults = expenseResultChannel.receiveAsFlow()

	private val categoryResultChannel = Channel<NetworkResult<List<CategoryResponse>>>()
	val categoryNetworkResults = categoryResultChannel.receiveAsFlow()

	@Inject
	lateinit var tokenManager: TokenManager

	init {
		getCategories()
		getExpenses()
	}

	fun setRecurrence(recurrence: Recurrence) {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val (start, end) = calculateDateRange(recurrence, 0)

			val filteredExpenses = uiState.value.expenses.filter {
				val date = LocalDate.parse(it.date, DateTimeFormatter.ISO_DATE)
				(date.isAfter(start) && date.isBefore(end)) || date.isEqual(start) || date.isEqual(end)
			}

			val sumTotal = filteredExpenses.sumOf { it.amount }

			_uiState.update {
				it.copy(
					recurrence = recurrence,
					filteredExpenses = filteredExpenses,
					sumTotal = sumTotal
				)
			}
			delay(0.3.seconds)
			_uiState.update { it.copy(isLoading = false) }
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

	fun removeToken() {
		tokenManager.deleteToken()
	}
}