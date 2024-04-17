package com.example.moneymanager.repository

import com.example.moneymanager.api.AuthApi
import com.example.moneymanager.models.authentication.AuthenticationRequest
import com.example.moneymanager.models.authentication.AuthenticationResponse
import com.example.moneymanager.models.user.UserRequest
import com.example.moneymanager.models.user.UserResponse
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.utils.TokenManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.json.JSONObject
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authApi: AuthApi) {

	private val registerResultChannel = Channel<NetworkResult<UserResponse>>()
	val registerNetworkResults = registerResultChannel.receiveAsFlow()

	private val loginResultChannel = Channel<NetworkResult<AuthenticationResponse>>()
	val loginNetworkResults = loginResultChannel.receiveAsFlow()

	private val logoutResultChannel = Channel<NetworkResult<String>>()
	val logoutNetworkResults = logoutResultChannel.receiveAsFlow()

	@Inject
	lateinit var tokenManager: TokenManager

	suspend fun registerUser(userRequest: UserRequest) {
		registerResultChannel.send(NetworkResult.Loading())

		val response = authApi.signUp(userRequest)

		if (response.isSuccessful && response.body() != null) {
			registerResultChannel.send(NetworkResult.Success(response.body()!!))
		}
		else if (response.errorBody() != null) {
			val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
			registerResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
		}
		else {
			registerResultChannel.send(NetworkResult.Error("Something went wrong"))
		}
	}

	suspend fun loginUser(authenticationRequest: AuthenticationRequest) {
		loginResultChannel.send(NetworkResult.Loading())

		val response = authApi.signIn(authenticationRequest)

		if (response.isSuccessful && response.body() != null) {
			loginResultChannel.send(NetworkResult.Success(response.body()!!))

			tokenManager.saveToken(response.body()!!.accessToken)
		}
		else if (response.errorBody() != null) {
			val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
			loginResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
		}
		else {
			loginResultChannel.send(NetworkResult.Error("Something went wrong"))
		}
	}

	suspend fun logoutUser(username: String) {
		logoutResultChannel.send(NetworkResult.Loading())

		val response = authApi.logout(username)

		if (response.isSuccessful) {
			logoutResultChannel.send(NetworkResult.Success("Logged out successfully"))
			tokenManager.deleteToken()
		}
		else if (response.errorBody() != null) {
			val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
			loginResultChannel.send(NetworkResult.Error(errorObj.getString("message")))
		}
		else {
			loginResultChannel.send(NetworkResult.Error("Something went wrong"))
		}
	}
}