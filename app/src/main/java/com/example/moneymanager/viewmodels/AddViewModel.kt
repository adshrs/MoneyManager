package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.models.category.CategoryResponse
import com.example.moneymanager.models.expense.ExpenseRequest
import com.example.moneymanager.models.income.IncomeRequest
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
import java.time.LocalDate
import javax.inject.Inject

data class AddScreenState(
	val typeDropdownSelection: String = "Expense",
	val amount: String = "",
	val date: LocalDate = LocalDate.now(),
	val description: String = "",
	val categoryName: String? = null,
	val categoryId: String? = null,
	val selectedCategoryColor: String? = null,
	var expenseCategories: List<CategoryResponse> = listOf(),
	var incomeCategories: List<CategoryResponse> = listOf(),
	val isLoading: Boolean = false
)

@HiltViewModel
class AddViewModel @Inject constructor(
	private val expenseRepository: ExpenseRepository,
	private val incomeRepository: IncomeRepository,
	private val categoryRepository: CategoryRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(AddScreenState())
	val uiState: StateFlow<AddScreenState> = _uiState.asStateFlow()

	private val expenseCategoryResultChannel = Channel<NetworkResult<List<CategoryResponse>>>()
	val expenseCategoryNetworkResults = expenseCategoryResultChannel.receiveAsFlow()

	private val incomeCategoryResultChannel = Channel<NetworkResult<List<CategoryResponse>>>()
	val incomeCategoryNetworkResults = incomeCategoryResultChannel.receiveAsFlow()

	private val statusResultChannel = Channel<NetworkResult<String>>()
	val statusNetworkResults = statusResultChannel.receiveAsFlow()

	@Inject
	lateinit var tokenManager: TokenManager

	init {
		getExpenseCategories()
		getIncomeCategories()
	}

	fun updateExpenseCategories(categories: List<CategoryResponse>) {
		_uiState.update { it.copy(expenseCategories = categories) }
	}

	fun updateIncomeCategories(categories: List<CategoryResponse>) {
		_uiState.update { it.copy(incomeCategories = categories) }
	}

	fun updateDropdownSelection(selected: String) {
		_uiState.update {
			it.copy(
				typeDropdownSelection = selected
			)
		}
	}

	fun setAmount(amount: String) {
		var parsed = amount.toDoubleOrNull()

		if (amount.isEmpty()) {
			parsed = 0.0
		}

		if (parsed != null) {
			_uiState.update {
				it.copy(
					amount = amount.trim()
				)
			}
		}
	}

	fun setDate(date: LocalDate) {
		_uiState.update {
			it.copy(
				date = date
			)
		}
	}

	fun setNote(note: String) {
		_uiState.update {
			it.copy(
				description = note
			)
		}
	}

	fun setCategoryName(category: String) {
		_uiState.update {
			it.copy(
				categoryName = category
			)
		}
	}

	fun setCategoryId(categoryId: String) {
		_uiState.update {
			it.copy(
				categoryId = categoryId
			)
		}
	}

	fun setCategoryColor(categoryColor: String) {
		_uiState.update {
			it.copy(
				selectedCategoryColor = categoryColor
			)
		}
	}

	fun revertFields() {
		_uiState.update {
			it.copy(
				amount = "",
				date = LocalDate.now(),
				description = "",
				categoryName = null
			)
		}
	}

	private fun getExpenseCategories() {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = categoryRepository.getCategories("Expense")

			if (response.isSuccessful && response.body() != null) {
				expenseCategoryResultChannel.send(NetworkResult.Success(response.body()!!))
			} else if (response.errorBody() != null) {
				val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
				expenseCategoryResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
			} else {
				expenseCategoryResultChannel.send(NetworkResult.Error("Something went wrong"))
			}
			_uiState.update { it.copy(isLoading = false) }
		}
	}

	private fun getIncomeCategories() {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = categoryRepository.getCategories("Income")

			if (response.isSuccessful && response.body() != null) {
				incomeCategoryResultChannel.send(NetworkResult.Success(response.body()!!))
			} else if (response.errorBody() != null) {
				val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
				incomeCategoryResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
			} else {
				incomeCategoryResultChannel.send(NetworkResult.Error("Something went wrong"))
			}
			_uiState.update { it.copy(isLoading = false) }
		}
	}

	fun addExpense(expenseRequest: ExpenseRequest) {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = expenseRepository.addExpense(expenseRequest)

			if (response.isSuccessful && response.body() != null) {
				statusResultChannel.send(NetworkResult.Success("Expense Added"))
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

	fun addIncome(incomeRequest: IncomeRequest) {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = incomeRepository.addIncome(incomeRequest)

			if (response.isSuccessful && response.body() != null) {
				statusResultChannel.send(NetworkResult.Success("Income Added"))
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