package com.example.moneymanager.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.models.category.CategoryResponse
import com.example.moneymanager.models.expense.ExpenseRequest
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
import java.time.LocalDate
import javax.inject.Inject

data class AddScreenState(
	val amount: String = "",
	val date: LocalDate = LocalDate.now(),
	val description: String = "",
	val categoryName: String? = null,
	val categoryId: String? = null,
	val selectedCategoryColor: String? = null,
	var categories: List<CategoryResponse> = listOf(),
	val isLoading: Boolean = false
)

@HiltViewModel
class AddViewModel @Inject constructor(
	private val expenseRepository: ExpenseRepository,
	private val categoryRepository: CategoryRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(AddScreenState())
	val uiState: StateFlow<AddScreenState> = _uiState.asStateFlow()

	private val categoryResultChannel = Channel<NetworkResult<List<CategoryResponse>>>()
	val categoryNetworkResults = categoryResultChannel.receiveAsFlow()

	private val statusResultChannel = Channel<NetworkResult<String>>()
	val statusNetworkResults = statusResultChannel.receiveAsFlow()

	@Inject
	lateinit var tokenManager: TokenManager

	init {
		getCategories()
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

	fun updateCategories(categories: List<CategoryResponse>) {
		_uiState.update { it.copy(categories = categories) }
	}

	private fun getCategories() {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = categoryRepository.getCategories()

			if (response.isSuccessful && response.body() != null) {
				categoryResultChannel.send(NetworkResult.Success(response.body()!!))
				Log.d("MONEYMANAGERTAG", "${response.body()}")
			}
			else if (response.errorBody() != null) {
				val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
				categoryResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
				Log.e("MONEYMANAGERTAG", errorObj.getString("message"))
			}
			else {
				categoryResultChannel.send(NetworkResult.Error("Something went wrong"))
				Log.e("MONEYMANAGERTAG", "Something went wrong")
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

	fun removeToken() {
		tokenManager.deleteToken()
	}
}