package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.models.user.UserRequest
import com.example.moneymanager.models.user.UserResponse
import com.example.moneymanager.repository.AuthRepository
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.utils.validateCredentials
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SignUpScreenState(
	val fullName: String = "",
	val username: String = "",
	val email: String = "",
	val password: String = "",
	val passwordVisible: Boolean = false,
	var errorText: String = "",
	var isLoading: Boolean = false
)

@HiltViewModel
class SignUpViewModel @Inject constructor(
	private val authRepository: AuthRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(SignUpScreenState())
	val uiState: StateFlow<SignUpScreenState> = _uiState.asStateFlow()

	val networkResults: Flow<NetworkResult<UserResponse>>
		get() =  authRepository.registerNetworkResults

	fun setFullName(fullName: String) {
		_uiState.update {
			it.copy(
				fullName = fullName
			)
		}
	}

	fun setUsername(username: String) {
		_uiState.update {
			it.copy(
				username = username
			)
		}
	}

	fun setEmail(email: String) {
		_uiState.update {
			it.copy(
				email = email
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

	fun registerUser() {
		viewModelScope.launch {
			val validationResult = validateUserInput()

			if (validationResult.first) {
				authRepository.registerUser(
					UserRequest(
						username = _uiState.value.username,
						name = _uiState.value.fullName,
						email = _uiState.value.email,
						password = _uiState.value.password
					)
				)
			}
			else {
				_uiState.value.errorText = validationResult.second
			}
		}
	}

	private fun validateUserInput(): Pair<Boolean, String> {
		return validateCredentials(_uiState.value.email, _uiState.value.password)
	}
}