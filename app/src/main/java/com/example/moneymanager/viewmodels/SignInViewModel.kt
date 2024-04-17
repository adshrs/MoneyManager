package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.models.authentication.AuthenticationRequest
import com.example.moneymanager.models.authentication.AuthenticationResponse
import com.example.moneymanager.repository.AuthRepository
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignInScreenState(
	val username: String = "",
	val password: String = "",
	val passwordVisible: Boolean = false,
	var errorText: String = "",
	var isLoading: Boolean = false
)

@HiltViewModel
class SignInViewModel @Inject constructor(
	private val authRepository: AuthRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(SignInScreenState())
	val uiState: StateFlow<SignInScreenState> = _uiState.asStateFlow()

	val networkResults: Flow<NetworkResult<AuthenticationResponse>>
		get() =  authRepository.loginNetworkResults

	@Inject
	lateinit var tokenManager: TokenManager

	fun setUsername(username: String) {
		_uiState.update {
			it.copy(
				username = username
			)
		}
	}

	fun setPassword(password: String) {
		_uiState.update {
			it.copy(
				password = password
			)
		}
	}

	fun changePasswordVisible() {
		_uiState.update {
			it.copy(
				passwordVisible = !_uiState.value.passwordVisible
			)
		}
	}

	fun loginUser() {
		viewModelScope.launch {
			authRepository.loginUser(
				AuthenticationRequest(
					username = _uiState.value.username,
					password = _uiState.value.password
				)
			)
		}
	}

	fun tokenExists(): Boolean =
		tokenManager.getToken() != null
}