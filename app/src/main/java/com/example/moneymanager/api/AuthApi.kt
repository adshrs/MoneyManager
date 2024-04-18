package com.example.moneymanager.api

import com.example.moneymanager.models.authentication.AuthenticationRequest
import com.example.moneymanager.models.authentication.AuthenticationResponse
import com.example.moneymanager.models.user.UserRequest
import com.example.moneymanager.models.user.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

	@POST("/api/user")
	suspend fun signUp(@Body request: UserRequest): Response<UserResponse>

	@POST("/api/auth")
	suspend fun signIn(@Body request: AuthenticationRequest): Response<AuthenticationResponse>
}