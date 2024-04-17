package com.example.moneymanager.viewmodels

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.models.category.CategoryResponse
import com.example.moneymanager.repository.CategoryRepository
import com.example.moneymanager.ui.theme.Primary
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

data class CategoriesScreenState(
	val newCategoryColor: Color = Primary,
	val newCategoryName: String = "",
	val colorPickerOpened: Boolean = false,
	var categories: List<CategoryResponse> = listOf(),
	var userId: String = "",
	var deleteWarningVisible: Boolean = false,
	val categoryIdToDelete: String = "",
	var isLoading: Boolean = false
)

@HiltViewModel
class CategoriesViewModel @Inject constructor(
	private val categoryRepository: CategoryRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(CategoriesScreenState())
	val uiState: StateFlow<CategoriesScreenState> = _uiState.asStateFlow()

	private val categoryResultChannel = Channel<NetworkResult<List<CategoryResponse>>>()
	val categoryNetworkResults = categoryResultChannel.receiveAsFlow()

	private val statusResultChannel = Channel<NetworkResult<String>>()
	val statusNetworkResults = statusResultChannel.receiveAsFlow()

	@Inject
	lateinit var tokenManager: TokenManager

	init {
		getCategories()
	}

	fun showDeleteWarning(categoryId: String) {
		_uiState.update {
			it.copy(
				deleteWarningVisible = true,
				categoryIdToDelete = categoryId
			)
		}
	}

	fun hideDeleteWarning() {
		_uiState.update {
			it.copy(
				deleteWarningVisible = false,
				categoryIdToDelete = ""
			)
		}
	}

	fun updateCategories(categories: List<CategoryResponse>) {
		_uiState.update { it.copy(categories = categories) }
	}

	 private fun getCategories() {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = categoryRepository.getCategories()

			if (response.isSuccessful && response.body() != null) {
				categoryResultChannel.send(NetworkResult.Success(response.body()!!))
				Log.d("MONEYMANAGERTAG", "${response.body()}")
			}
			else if (response.errorBody() != null) {
				val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
				categoryResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
				Log.e("MONEYMANAGERTAG", errorObj.getString("message"))
			}
			else {
				categoryResultChannel.send(NetworkResult.Error("Something went wrong"))
				Log.e("MONEYMANAGERTAG", "Something went wrong")
			}
			_uiState.update { it.copy(isLoading = false) }
		}
	}

	fun deleteCategory(categoryId: String) {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = categoryRepository.deleteCategory(categoryId)
			if (response.isSuccessful) {
				statusResultChannel.send(NetworkResult.Success("Category Deleted"))
			}
			else if (response.errorBody() != null) {
				val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
				statusResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
			}
			else {
				statusResultChannel.send(NetworkResult.Error("Something went wrong"))
			}
			_uiState.update { it.copy(isLoading = false) }
		}

		getCategories()
	}

	fun removeToken() {
		tokenManager.deleteToken()
	}
}