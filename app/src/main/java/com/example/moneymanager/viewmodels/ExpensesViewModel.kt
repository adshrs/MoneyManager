package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import com.example.moneymanager.models.Recurrence
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ExpensesScreenState(
	val recurrence: Recurrence = Recurrence.Daily
)

class ExpensesViewModel: ViewModel() {
	private val _uiState = MutableStateFlow(ExpensesScreenState())
	val uiState: StateFlow<ExpensesScreenState> = _uiState.asStateFlow()

	fun setRecurrence(recurrence: Recurrence) {
		_uiState.update {
			it.copy(
				recurrence = recurrence
			)
		}
	}
}