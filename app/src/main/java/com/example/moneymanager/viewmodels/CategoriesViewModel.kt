package com.example.moneymanager.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.moneymanager.models.Category
import com.example.moneymanager.ui.theme.Primary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CategoriesScreenState(
	val newCategoryColor: Color = Primary,
	val newCategoryName: String = "",
	val colorPickerOpened: Boolean = false,
	val categories: List<Category> = listOf(
		Category("Groceries", Color.Red),
		Category("Food", Color.Blue),
		Category("Stationary", Color.White),
		Category("Cosmetics", Color.Green),
		Category("Furniture", Color.Yellow),
		Category("Electronics", Color.Cyan)
	)
)

class CategoriesViewModel : ViewModel() {
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
		val newCategoriesList = mutableListOf<Category>()
		newCategoriesList.addAll(_uiState.value.categories)

		_uiState.update {
			it.copy(
				categories = newCategoriesList.plus(
					Category(
						uiState.value.newCategoryName,
						uiState.value.newCategoryColor
					)
				),
				newCategoryName = "",
				newCategoryColor = Primary
			)
		}
	}

	fun deleteCategory(category: Category) {
		val index = _uiState.value.categories.indexOf(category)
		val newList = mutableListOf<Category>()
		newList.addAll(_uiState.value.categories)
		newList.removeAt(index)

		_uiState.update {
			it.copy(
				categories = newList
			)
		}
	}
}