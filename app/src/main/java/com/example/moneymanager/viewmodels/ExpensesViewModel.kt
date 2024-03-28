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

data class ExpensesScreenState(
	val recurrence: Recurrence = Recurrence.Daily,
	val sumTotal: Double = 0.0,
	val expenses: List<Expense> = mockExpenses
)

class ExpensesViewModel: ViewModel() {
	private val _uiState = MutableStateFlow(ExpensesScreenState())
	val uiState: StateFlow<ExpensesScreenState> = _uiState.asStateFlow()

	init {
		viewModelScope.launch(Dispatchers.IO) {
			setRecurrence(Recurrence.Daily)
		}
	}

	fun setRecurrence(recurrence: Recurrence) {
		val (start, end) = calculateDateRange(recurrence, 0)

		val filteredExpenses = mockExpenses.filter {
			(it.date.isAfter(start) && it.date.isBefore(end)) || it.date.isEqual(start) || it.date.isEqual(end)
		}

		val sumTotal = filteredExpenses.sumOf { it.amount }

		_uiState.update {
			it.copy(
				recurrence = recurrence,
				expenses = filteredExpenses,
				sumTotal = sumTotal
			)
		}
	}
}