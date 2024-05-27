package com.example.moneymanager.repository

import com.example.moneymanager.api.UserApi
import com.example.moneymanager.models.user.ChangePasswordRequest
import com.example.moneymanager.models.user.UserRequest
import com.example.moneymanager.models.user.UserResponse
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {

	suspend fun getUserDetails(): Response<UserResponse> {
		return userApi.getUserDetails()
	}

	suspend fun updateUserDetails(userRequest: UserRequest): Response<UserResponse> {
		return userApi.updateUserDetails(userRequest)
	}

	suspend fun eraseAllData(): Response<Unit> {
		return userApi.eraseAllData()
	}

	suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Response<Unit> {
		return userApi.changePassword(changePasswordRequest)
	}
}