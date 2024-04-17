package com.example.moneymanager.api

import com.example.moneymanager.models.authentication.AuthenticationRequest
import com.example.moneymanager.models.authentication.AuthenticationResponse
import com.example.moneymanager.models.user.UserRequest
import com.example.moneymanager.models.user.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi {

	@POST("/api/user")
	suspend fun signUp(@Body request: UserRequest): Response<UserResponse>

	@POST("/api/auth")
	suspend fun signIn(@Body request: AuthenticationRequest): Response<AuthenticationResponse>

	@POST("/api/auth/logout/{username}")
	suspend fun logout(@Path("username") username: String): Response<Void>
}