package com.example.moneymanager.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CategoriesScreenState(
	val newCategoryColor: Color = Color.White,
	val newCategoryName: String = "",
	val colorPickerOpened: Boolean = false
)

class CategoriesViewModel: ViewModel() {
	private val _uiState = MutableStateFlow(CategoriesScreenState())
	val uiState: StateFlow<CategoriesScreenState> = _uiState.asStateFlow()

	fun setNewCategoryColor(color: Color) {
		_uiState.update {
			it.copy(
				newCategoryColor = color
			)
		}
	}

	fun setNewCategoryName(name: String) {
		_uiState.update {
			it.copy(
				newCategoryName = name
			)
		}
	}

	fun showColorPicker() {
		_uiState.update {
			it.copy(
				colorPickerOpened = true
			)
		}
	}

	fun hideColorPicker() {
		_uiState.update {
			it.copy(
				colorPickerOpened = false
			)
		}
	}

	fun createNewCategory() {
		//TODO: save new category to db
	}
}