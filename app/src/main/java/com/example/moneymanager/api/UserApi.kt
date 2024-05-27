package com.example.moneymanager.api

import com.example.moneymanager.models.user.ChangePasswordRequest
import com.example.moneymanager.models.user.UserRequest
import com.example.moneymanager.models.user.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApi {
	@GET("/api/user")
	suspend fun getUserDetails(): Response<UserResponse>

	@PUT("/api/user")
	suspend fun updateUserDetails(@Body updatedUserDetailsRequest: UserRequest): Response<UserResponse>

	@DELETE("/api/user/eraseUserData")
	suspend fun eraseAllData(): Response<Unit>

	@POST("/api/user/changePassword")
	suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<Unit>
}