package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import com.example.moneymanager.repository.AuthRepository
import com.example.moneymanager.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SettingsScreenState(
	val logoutWarningVisible: Boolean = false,
	val eraseDataWarningVisible: Boolean = false,
	var isLoading: Boolean = false,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
	private val authRepository: AuthRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(SettingsScreenState())
	val uiState: StateFlow<SettingsScreenState> = _uiState.asStateFlow()

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

	fun logout() {
		tokenManager.deleteToken()
	}
}