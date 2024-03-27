package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import com.example.moneymanager.models.Recurrence
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AnalyticsScreenState(
	val recurrence: Recurrence = Recurrence.Weekly,
	val dateRangeMenuOpened: Boolean = false
)

class AnalyticsViewModel: ViewModel() {
	private val _uiState = MutableStateFlow(AnalyticsScreenState())
	val uiState: StateFlow<AnalyticsScreenState> = _uiState.asStateFlow()

	fun setRecurrence(recurrence: Recurrence) {
		_uiState.update {
			it.copy(
				recurrence = recurrence
			)
		}
	}

	fun openDateRangeMenu() {
		_uiState.update {
			it.copy(
				dateRangeMenuOpened = true
			)
		}
	}

	fun closeDateRangeMenu() {
		_uiState.update {
			it.copy(
				dateRangeMenuOpened = false
			)
		}
	}
}