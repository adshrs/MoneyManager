package com.example.moneymanager.pages

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneymanager.R
import com.example.moneymanager.components.LoadingIndicator
import com.example.moneymanager.components.textFields.AuthTextField
import com.example.moneymanager.components.textFields.PasswordTextField
import com.example.moneymanager.ui.theme.Destructive
import com.example.moneymanager.ui.theme.DisabledButton
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.Secondary
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.ui.theme.Watermark
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.viewmodels.SignInViewModel

@Composable
fun SignIn(
	navController: NavController,
	signInViewModel: SignInViewModel = hiltViewModel()
) {

	val uiState by signInViewModel.uiState.collectAsState()
	val context = LocalContext.current

	val keyboardController = LocalSoftwareKeyboardController.current

	if (signInViewModel.tokenExists()) {
		Box(
			modifier = Modifier.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			LoadingIndicator(size = 32.dp, color = Color.Black)
		}
		navController.navigate("home/expenses") {
			popUpTo("signin") {
				inclusive = true
			}
		}
		return
	}

	LaunchedEffect(signInViewModel, context) {
		signInViewModel.networkResults.collect { result ->
			uiState.isLoading = false

			when (result) {
				is NetworkResult.Success -> {
					navController.navigate("home/expenses") {
						popUpTo("signin") {
							inclusive = true
						}
					}
				}

				is NetworkResult.Error -> {
					uiState.errorText = result.message.toString()
				}

				is NetworkResult.Loading -> {
					uiState.isLoading = true
				}
			}
		}
	}

	Scaffold(
		modifier = Modifier.imePadding(),
		content = { innerPadding ->
			LazyColumn(
				modifier = Modifier
					.padding(innerPadding)
					.fillMaxSize(),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				item {
					Column(
						modifier = Modifier
							.fillMaxSize(),
						verticalArrangement = Arrangement.Center,
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						Image(
							painterResource(
								id = R.drawable.app_logo
							),
							contentDescription = "App logo",
							modifier = Modifier
								.size(130.dp)
								.padding(bottom = 8.dp)
						)
						Text(
							text = "Sign In",
							style = Typography.titleLarge
						)
						Box(
							modifier = Modifier
								.fillMaxWidth()
								.height(48.dp)
								.padding(horizontal = 16.dp),
							contentAlignment = Alignment.Center
						) {
							Text(
								modifier = Modifier.padding(horizontal = 4.dp),
								text = uiState.errorText,
								style = Typography.bodySmall,
								color = Destructive
							)
						}
						AuthTextField(
							modifier = Modifier,
							label = { Text(text = "Username") },
							value = uiState.username,
							onValueChange = signInViewModel::setUsername,
							keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
						)
						PasswordTextField(
							modifier = Modifier,
							label = { Text(text = "Password") },
							value = uiState.password,
							onValueChange = signInViewModel::setPassword,
							trailingIcon = {
								IconButton(onClick = { signInViewModel.changePasswordVisible() }) {
									if (uiState.passwordVisible) {
										Icon(
											painterResource(
												id = R.drawable.icon_visibility
											),
											contentDescription = "Password visibility on"
										)
									} else {
										Icon(
											painterResource(
												id = R.drawable.icon_visibility_off
											),
											contentDescription = "Password visibility off"
										)
									}
								}
							},
							visualTransformation =
							if (uiState.passwordVisible)
								VisualTransformation.None
							else
								PasswordVisualTransformation()
						)
						Button(
							onClick = {
								signInViewModel.loginUser()
								keyboardController?.hide()
							},
							modifier = Modifier
								.padding(horizontal = 16.dp)
								.padding(top = 48.dp, bottom = 32.dp)
								.fillMaxWidth()
								.height(50.dp),
							shape = RoundedCornerShape(10.dp),
							colors = ButtonColors(
								containerColor = Secondary,
								contentColor = Color.Black,
								disabledContainerColor = DisabledButton,
								disabledContentColor = Color.Black
							),
							enabled = uiState.username.isNotBlank() && uiState.password.isNotBlank()
									  && uiState.password.length >= 8
						) {
							Text(text = "Sign In")
							if (uiState.isLoading) {
								Box(modifier = Modifier.padding(start = 8.dp)) {
									LoadingIndicator(size = 24.dp, color = Color.Black)
								}
							}
						}
						Row {
							Text(text = "Don't have an account? ")
							Text(
								text = "Register",
								style = LocalTextStyle.current.copy(
									fontWeight = FontWeight.SemiBold,
									textDecoration = TextDecoration.Underline
								),
								modifier = Modifier.clickable { navController.navigate("signup") }
							)
						}
						Text(
							text = "Money Manager",
							style = Typography.headlineSmall.copy(
								fontWeight = FontWeight.Bold,
								color = Watermark
							),
							modifier = Modifier.padding(top = 60.dp)
						)
					}
				}
			}
		}
	)

	BackHandler {
		(context as? Activity)?.finish()
	}
}

@Preview(showBackground = true)
@Composable
fun PreviewSignIn() {
	MoneyManagerTheme {
		SignIn(navController = rememberNavController())
	}
}