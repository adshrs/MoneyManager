package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.models.category.CategoryResponse
import com.example.moneymanager.models.income.IncomeResponse
import com.example.moneymanager.utils.calculateDateRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class IncomeAnalyticPageState(
	var incomes: List<IncomeResponse> = listOf(),
	var categories: List<CategoryResponse> = listOf(),
	var filteredIncomes: List<IncomeResponse> = listOf(),
	val dateStart: LocalDateTime = LocalDateTime.now(),
	val dateEnd: LocalDateTime = LocalDateTime.now(),
	val avgPerDay: Double = 0.0,
	val totalForDateRange: Double = 0.0
)

class IncomeAnalyticPageViewModel(
	private val page: Int,
	val recurrence: Recurrence,
	val incomes: List<IncomeResponse>,
	val categories: List<CategoryResponse>
) : ViewModel() {
	private val _uiState = MutableStateFlow(IncomeAnalyticPageState())
	val uiState: StateFlow<IncomeAnalyticPageState> = _uiState.asStateFlow()

	init {
		if (incomes.isNotEmpty() && categories.isNotEmpty()) {
			_uiState.update {
				it.copy(
					categories = categories,
					incomes = incomes
				)
			}

			viewModelScope.launch(Dispatchers.IO) {
				val (start, end, daysInRange) = calculateDateRange(recurrence, page)

				val filteredIncomes = uiState.value.incomes.filter {
					val date = LocalDate.parse(it.date, DateTimeFormatter.ISO_DATE)
					(date.isAfter(start) && date.isBefore(end)) || date.isEqual(start) || date.isEqual(end)
				}

				val totalIncomesAmount = filteredIncomes.sumOf { it.amount }
				val avgPerDay: Double = totalIncomesAmount / daysInRange

				viewModelScope.launch(Dispatchers.Main) {
					_uiState.update {
						it.copy(
							dateStart = LocalDateTime.of(start, LocalTime.MIN),
							dateEnd = LocalDateTime.of(end, LocalTime.MAX),
							filteredIncomes = filteredIncomes,
							avgPerDay = avgPerDay,
							totalForDateRange = totalIncomesAmount
						)
					}
				}
			}
		}
	}
}