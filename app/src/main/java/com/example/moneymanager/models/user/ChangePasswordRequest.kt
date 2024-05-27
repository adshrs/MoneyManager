package com.example.moneymanager.models.user

data class ChangePasswordRequest(
	val oldPassword: String,
	val newPassword: String
)
