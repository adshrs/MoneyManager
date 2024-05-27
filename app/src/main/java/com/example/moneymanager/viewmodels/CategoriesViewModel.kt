package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.models.category.CategoryResponse
import com.example.moneymanager.repository.CategoryRepository
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

data class CategoriesScreenState(
	val dropdownSelection: String = "Expense",
	var expenseCategories: List<CategoryResponse> = listOf(),
	var incomeCategories: List<CategoryResponse> = listOf(),
	var deleteWarningVisible: Boolean = false,
	val categoryIdToDelete: String = "",
	var isLoading: Boolean = false
)

@HiltViewModel
class CategoriesViewModel @Inject constructor(
	private val categoryRepository: CategoryRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(CategoriesScreenState())
	val uiState: StateFlow<CategoriesScreenState> = _uiState.asStateFlow()

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

	fun setDropdownSelection(selected: String) {
		_uiState.update {
			it.copy(
				dropdownSelection = selected
			)
		}
	}

	fun showDeleteWarning(categoryId: String) {
		_uiState.update {
			it.copy(
				deleteWarningVisible = true,
				categoryIdToDelete = categoryId
			)
		}
	}

	fun hideDeleteWarning() {
		_uiState.update {
			it.copy(
				deleteWarningVisible = false,
				categoryIdToDelete = ""
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

	fun deleteCategory(categoryId: String) {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = categoryRepository.deleteCategory(categoryId)
			if (response.isSuccessful) {
				statusResultChannel.send(NetworkResult.Success("Category Deleted"))
			} else if (response.errorBody() != null) {
				val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
				statusResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
			} else {
				statusResultChannel.send(NetworkResult.Error("Something went wrong"))
			}
			_uiState.update { it.copy(isLoading = false) }
		}
	}

	fun removeToken() {
		tokenManager.deleteToken()
	}
}