package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.repository.AuthRepository
import com.example.moneymanager.repository.UserRepository
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

data class SettingsScreenState(
	val logoutWarningVisible: Boolean = false,
	val eraseDataWarningVisible: Boolean = false,
	var isLoading: Boolean = false,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
	private val authRepository: AuthRepository,
	private val userRepository: UserRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(SettingsScreenState())
	val uiState: StateFlow<SettingsScreenState> = _uiState.asStateFlow()

	private val statusResultChannel = Channel<NetworkResult<String>>()
	val statusNetworkResults = statusResultChannel.receiveAsFlow()

	@Inject
	lateinit var tokenManager: TokenManager

	fun showLogoutWarning() {
		_uiState.update {
			it.copy(
				logoutWarningVisible = true
			)
		}
	}

	fun hideLogoutWarning() {
		_uiState.update {
			it.copy(
				logoutWarningVisible = false
			)
		}
	}

	fun showEraseDataWarning() {
		_uiState.update {
			it.copy(
				eraseDataWarningVisible = true
			)
		}
	}

	fun hideEraseDataWarning() {
		_uiState.update {
			it.copy(
				eraseDataWarningVisible = false
			)
		}
	}

	fun eraseAllData() {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = userRepository.eraseAllData()

			if (response.isSuccessful && response.body() != null) {
				statusResultChannel.send(NetworkResult.Success("All of your data has been erased."))
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
	}

	fun logout() {
		tokenManager.deleteToken()
	}
}