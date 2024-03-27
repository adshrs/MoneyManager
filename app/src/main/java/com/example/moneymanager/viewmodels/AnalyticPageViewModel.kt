package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.components.expensesList.mockExpenses
import com.example.moneymanager.models.Expense
import com.example.moneymanager.models.Recurrence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Year
import java.time.YearMonth

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

	private lateinit var start: LocalDate
	private lateinit var end: LocalDate
	private var daysInRange = 1

	init {
		viewModelScope.launch(Dispatchers.IO) {
			val today = LocalDate.now()


			when (recurrence) {
				Recurrence.Weekly -> {
					start =
						LocalDate
							.now()
							.minusDays(today.dayOfWeek.value.toLong())
							.minusDays((page * 7).toLong())
					end = start.plusDays(6)
					daysInRange = 7
				}

				Recurrence.Monthly -> {
					start =
						LocalDate
							.of(today.year, today.month, 1)
							.minusMonths(page.toLong())
					val numberOfDays = YearMonth.of(start.year, start.month).lengthOfMonth()
					end = start.plusDays(numberOfDays.toLong() - 1)
					daysInRange = numberOfDays
				}

				Recurrence.Yearly -> {
					start = LocalDate.of(today.year, 1, 1)
					end = LocalDate.of(today.year, 12, 31)
					val numberOfDays = Year.of(start.year).length()
					daysInRange = numberOfDays
				}

				else -> Unit
			}

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