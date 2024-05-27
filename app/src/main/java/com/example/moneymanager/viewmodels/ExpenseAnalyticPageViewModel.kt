package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.models.category.CategoryResponse
import com.example.moneymanager.models.expense.ExpenseResponse
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

data class ExpenseAnalyticPageState(
	var expenses: List<ExpenseResponse> = listOf(),
	var categories: List<CategoryResponse> = listOf(),
	var filteredExpenses: List<ExpenseResponse> = listOf(),
	val dateStart: LocalDateTime = LocalDateTime.now(),
	val dateEnd: LocalDateTime = LocalDateTime.now(),
	val avgPerDay: Double = 0.0,
	val totalForDateRange: Double = 0.0
)

class ExpenseAnalyticPageViewModel(
	private val page: Int,
	val recurrence: Recurrence,
	val expenses: List<ExpenseResponse>,
	val categories: List<CategoryResponse>
) : ViewModel() {
	private val _uiState = MutableStateFlow(ExpenseAnalyticPageState())
	val uiState: StateFlow<ExpenseAnalyticPageState> = _uiState.asStateFlow()

	init {
		if (expenses.isNotEmpty() && categories.isNotEmpty()) {
			_uiState.update {
				it.copy(
					categories = categories,
					expenses = expenses
				)
			}

			viewModelScope.launch(Dispatchers.IO) {
				val (start, end, daysInRange) = calculateDateRange(recurrence, page)

				val filteredExpenses = uiState.value.expenses.filter {
					val date = LocalDate.parse(it.date, DateTimeFormatter.ISO_DATE)
					(date.isAfter(start) && date.isBefore(end)) || date.isEqual(start) || date.isEqual(end)
				}

				val totalExpensesAmount = filteredExpenses.sumOf { it.amount }
				val avgPerDay: Double = totalExpensesAmount / daysInRange

				viewModelScope.launch(Dispatchers.Main) {
					_uiState.update {
						it.copy(
							dateStart = LocalDateTime.of(start, LocalTime.MIN),
							dateEnd = LocalDateTime.of(end, LocalTime.MAX),
							filteredExpenses = filteredExpenses,
							avgPerDay = avgPerDay,
							totalForDateRange = totalExpensesAmount
						)
					}
				}
			}
		}
	}
}