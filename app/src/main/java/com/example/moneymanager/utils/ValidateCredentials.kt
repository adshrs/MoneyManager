package com.example.moneymanager.utils

import android.util.Patterns

fun validateCredentials(email: String, password: String): Pair<Boolean, String> {
	var result = Pair(true, "")

	if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
		result = Pair(false, "Please provide a valid email")
	}
	else if (password.length < 8){
		result = Pair(false, "Password requires at least 8 characters")
	}
	return result
}