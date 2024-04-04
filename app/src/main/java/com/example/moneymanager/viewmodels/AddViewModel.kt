package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import com.example.moneymanager.models.Recurrence
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

data class AddScreenState(
	val amount: String = "",
	val recurrence: Recurrence? = null,
	val date: LocalDate = LocalDate.now(),
	val Description: String = "",
	val category: String? = null // TODO: Replace when you build the Category model
)

class AddViewModel : ViewModel() {
	private val _uiState = MutableStateFlow(AddScreenState())
	val uiState: StateFlow<AddScreenState> = _uiState.asStateFlow()

	fun setAmount(amount: String) {
		_uiState.update {
			it.copy(
				amount = amount
			)
		}
	}

	fun setRecurrence(recurrence: Recurrence) {
		_uiState.update {
			it.copy(
				recurrence = recurrence
			)
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
				Description = note
			)
		}
	}

	fun setCategory(category: String) {
		_uiState.update {
			it.copy(
				category = category
			)
		}
	}

	fun submit() {
		//TODO: Save to db
	}
}