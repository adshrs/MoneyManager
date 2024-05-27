package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneymanager.models.user.ChangePasswordRequest
import com.example.moneymanager.models.user.UserRequest
import com.example.moneymanager.models.user.UserResponse
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

data class AccountScreenState(
	val user: UserResponse? = null,
	val fullName: String = "",
	val username: String = "",
	val email: String = "",
	val oldPassword: String = "",
	val newPassword: String = "",
	val oldPasswordVisible: Boolean = false,
	val newPasswordVisible: Boolean = false,
	val isEditButtonActive: Boolean = false,
	val changePasswordDialogOpened: Boolean = false,
	val isLoading: Boolean = false
)

@HiltViewModel
class AccountViewModel @Inject constructor(
	private val userRepository: UserRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(AccountScreenState())
	val uiState: StateFlow<AccountScreenState> = _uiState.asStateFlow()

	private val userResultChannel = Channel<NetworkResult<UserResponse>>()
	val userNetworkResults = userResultChannel.receiveAsFlow()

	private val statusResultChannel = Channel<NetworkResult<String>>()
	val statusNetworkResults = statusResultChannel.receiveAsFlow()

	@Inject
	lateinit var tokenManager: TokenManager

	init {
		getUserDetails()
	}

	fun updateUser(user: UserResponse) {
		_uiState.update { it.copy(user = user) }
		setNewFullName(user.name)
		setNewUsername(user.username)
		setNewEmail(user.email)
	}

	fun changeEditButtonActiveState() {
		_uiState.update {
			it.copy(
				isEditButtonActive = !_uiState.value.isEditButtonActive
			)
		}
	}

	fun changePasswordDialogOpenedState() {
		_uiState.update {
			it.copy(
				changePasswordDialogOpened = !_uiState.value.changePasswordDialogOpened
			)
		}
	}

	fun changeOldPasswordVisible() {
		_uiState.update {
			it.copy(
				oldPasswordVisible = !_uiState.value.oldPasswordVisible
			)
		}
	}

	fun changeNewPasswordVisible() {
		_uiState.update {
			it.copy(
				newPasswordVisible = !_uiState.value.newPasswordVisible
			)
		}
	}

	fun setNewFullName(newFullName: String) {
		_uiState.update {
			it.copy(
				fullName = newFullName
			)
		}
	}

	fun setNewUsername(newUsername: String) {
		_uiState.update {
			it.copy(
				username = newUsername
			)
		}
	}

	fun setNewEmail(newEmail: String) {
		_uiState.update {
			it.copy(
				email = newEmail
			)
		}
	}

	fun setNewOldPassword(newOldPassword: String) {
		_uiState.update {
			it.copy(
				oldPassword = newOldPassword
			)
		}
	}

	fun setNewNewPassword(newNewPassword: String) {
		_uiState.update {
			it.copy(
				newPassword = newNewPassword
			)
		}
	}

	private fun getUserDetails() {
		viewModelScope.launch {
			_uiState.update { it.copy(isLoading = true) }
			val response = userRepository.getUserDetails()

			if (response.isSuccessful && response.body() != null) {
				userResultChannel.send(NetworkResult.Success(response.body()!!))
			}
			else if (response.errorBody() != null) {
				val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
				userResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
			}
			else {
				userResultChannel.send(NetworkResult.Error("Something went wrong"))
			}
			_uiState.update { it.copy(isLoading = false) }
		}
	}

	fun updateUserDetails(userRequest: UserRequest) {
		viewModelScope.launch {
			val response = userRepository.updateUserDetails(userRequest)

			if (response.isSuccessful && response.body() != null) {
				statusResultChannel.send(NetworkResult.Success("Account details updated"))
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

	fun changePassword(changePasswordRequest: ChangePasswordRequest) {
		viewModelScope.launch {
			val response = userRepository.changePassword(changePasswordRequest)

			if (response.isSuccessful && response.body() != null) {
				statusResultChannel.send(NetworkResult.Success("Your password has been changed"))
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