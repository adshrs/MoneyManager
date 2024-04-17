package com.example.moneymanager.viewmodels

import androidx.lifecycle.ViewModel
import com.example.moneymanager.repository.AuthRepository
import com.example.moneymanager.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
	private val authRepository: AuthRepository
): ViewModel() {

	@Inject
	lateinit var tokenManager: TokenManager

	fun logout() {
		tokenManager.deleteToken()
	}
}