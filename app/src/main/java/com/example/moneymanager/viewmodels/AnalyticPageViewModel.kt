package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.components.expensesList.mockExpenses
import com.example.moneymanager.models.Expense
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.utils.calculateDateRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime

data class AnalyticPageState(
	val expenses: List<Expense> = mockExpenses,
	val dateStart: LocalDateTime = LocalDateTime.now(),
	val dateEnd: LocalDateTime = LocalDateTime.now(),
	val avgPerDay: Double = 0.0,
	val totalForDateRange: Double = 0.0
)

class AnalyticPageViewModel(private val page: Int, val recurrence: Recurrence) : ViewModel() {
	private val _uiState = MutableStateFlow(AnalyticPageState())
	val uiState: StateFlow<AnalyticPageState> = _uiState.asStateFlow()

	init {
		viewModelScope.launch(Dispatchers.IO) {
			val (start, end, daysInRange) = calculateDateRange(recurrence, page)

			val filteredExpenses = mockExpenses.filter {
				(it.date.isAfter(start) && it.date.isBefore(end)) || it.date.isEqual(start) || it.date.isEqual(end)
			}

			val totalExpensesAmount = filteredExpenses.sumOf { it.amount }
			val avgPerDay: Double = totalExpensesAmount / daysInRange

			viewModelScope.launch(Dispatchers.Main) {
				_uiState.update {
					it.copy(
						dateStart = LocalDateTime.of(start, LocalTime.MIN),
						dateEnd = LocalDateTime.of(end, LocalTime.MAX),
						expenses = filteredExpenses,
						avgPerDay = avgPerDay,
						totalForDateRange = totalExpensesAmount
					)
				}
			}
		}
	}
}