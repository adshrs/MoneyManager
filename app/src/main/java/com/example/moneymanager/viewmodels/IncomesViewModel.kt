package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.models.category.CategoryResponse
import com.example.moneymanager.models.income.IncomeResponse
import com.example.moneymanager.repository.CategoryRepository
import com.example.moneymanager.repository.IncomeRepository
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

data class IncomesScreenState(
	val recurrence: Recurrence = Recurrence.Daily,
	val sumTotal: Double = 0.0,
	var recurrenceMenuOpened: Boolean = false,
	var incomes: List<IncomeResponse> = listOf(),
	var filteredIncomes: List<IncomeResponse> = listOf(),
	var categories: List<CategoryResponse> = listOf(),
	var isLoading: Boolean = false
)

data class IncomesDayGroupState(
	var deleteWarningVisible: Boolean = false,
	val incomeIdToDelete: String = ""
)

@HiltViewModel
class IncomesViewModel @Inject constructor(
	private val incomeRepository: IncomeRepository,
	private val categoryRepository: CategoryRepository
): ViewModel() {

	private val _uiState = MutableStateFlow(IncomesScreenState())
	val uiState: StateFlow<IncomesScreenState> = _uiState.asStateFlow()

	private val _incomesDayGroupUiState = MutableStateFlow(IncomesDayGroupState())
	val incomesDayGroupUiState: StateFlow<IncomesDayGroupState> = _incomesDayGroupUiState.asStateFlow()

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
		getIncomes()
	}

	fun showDeleteWarning(incomeId: String) {
		_incomesDayGroupUiState.update {
			it.copy(
				deleteWarningVisible = true,
				incomeIdToDelete = incomeId
			)
		}
	}

	fun hideDeleteWarning() {
		_incomesDayGroupUiState.update {
			it.copy(
				deleteWarningVisible = false,
				incomeIdToDelete = ""
			)
		}
	}

	fun setRecurrence(recurrence: Recurrence) {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val (start, end) = calculateDateRange(recurrence, 0)

			val filteredIncomes = _uiState.value.incomes.filter {
				val date = LocalDate.parse(it.date, DateTimeFormatter.ISO_DATE)
				(date.isAfter(start) && date.isBefore(end)) || date.isEqual(start) || date.isEqual(end)
			}

			val sumTotal = filteredIncomes.sumOf { it.amount }

			_uiState.update {
				it.copy(
					recurrence = recurrence,
					filteredIncomes = filteredIncomes,
					sumTotal = sumTotal
				)
			}
			delay(0.3.seconds)
			_uiState.update { it.copy(isLoading = false) }
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
				categories = categories
			)
		}
	}

	private fun getCategories() {
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

		getCategories()
	}

	fun removeToken() {
		tokenManager.deleteToken()
	}
}