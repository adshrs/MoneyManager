package com.example.moneymanager.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.models.category.CategoryRequest
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

data class AddCategoryScreenState(
	val newCategoryColor: Color = Primary,
	val newCategoryName: String = "",
	var userId: String = "",
)

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
	private val categoryRepository: CategoryRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(AddCategoryScreenState())
	val uiState: StateFlow<AddCategoryScreenState> = _uiState.asStateFlow()

	private val statusResultChannel = Channel<NetworkResult<String>>()
	val statusNetworkResults = statusResultChannel.receiveAsFlow()

	@Inject
	lateinit var tokenManager: TokenManager

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

	fun createCategory(categoryRequest: CategoryRequest) {
		viewModelScope.launch {
			val response = categoryRepository.addCategory(categoryRequest)

			if (response.isSuccessful && response.body() != null) {
				statusResultChannel.send(NetworkResult.Success("Category created"))
			}
			else if (response.errorBody() != null) {
				val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
				statusResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
			}
			else {
				statusResultChannel.send(NetworkResult.Error("Something went wrong"))
			}
		}
	}

	fun removeToken() {
		tokenManager.deleteToken()
	}
}
