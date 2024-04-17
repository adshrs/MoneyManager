package com.example.moneymanager.pages

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextAlign
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
import com.example.moneymanager.ui.theme.Surface
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.ui.theme.Watermark
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.viewmodels.SignUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(navController: NavController, signUpViewModel: SignUpViewModel = hiltViewModel()) {

	val uiState by signUpViewModel.uiState.collectAsState()
	val context = LocalContext.current
	val keyboardController = LocalSoftwareKeyboardController.current

	LaunchedEffect(signUpViewModel, context) {
		signUpViewModel.networkResults.collect { result ->
			uiState.isLoading = false

			when (result) {
				is NetworkResult.Success -> {
					Toast.makeText(context, "Your account has been created", Toast.LENGTH_SHORT)
						.show()
					navController.navigate("signin")
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
		topBar = {
			TopAppBar(
				title = { },
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = Surface
				),
				navigationIcon = {
					Surface(
						color = Color.Transparent,
						modifier = Modifier
							.padding(8.dp)
							.clickable(
								indication = null,
								interactionSource = remember { MutableInteractionSource() }
							) {
								navController.popBackStack()
							}
					) {
						Row(
							modifier = Modifier.padding(vertical = 10.dp),

							) {
							Icon(
								Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
								contentDescription = "Menu"
							)
							Text(text = "Back", textAlign = TextAlign.Center)
						}
					}
				}
			)
		},
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
						Text(
							text = "Create an Account",
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
							label = { Text(text = "Full Name") },
							value = uiState.fullName,
							onValueChange = signUpViewModel::setFullName,
							keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
						)
						AuthTextField(
							modifier = Modifier,
							label = { Text(text = "Username") },
							value = uiState.username,
							onValueChange = signUpViewModel::setUsername,
							keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
						)
						AuthTextField(
							modifier = Modifier,
							label = { Text(text = "Email") },
							value = uiState.email,
							onValueChange = signUpViewModel::setEmail,
							keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
						)
						PasswordTextField(
							modifier = Modifier,
							label = { Text(text = "Password") },
							value = uiState.password,
							onValueChange = signUpViewModel::setPassword,
							trailingIcon = {
								IconButton(onClick = signUpViewModel::changePasswordVisible) {
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
								signUpViewModel.registerUser()
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
							enabled = uiState.fullName.isNotBlank() && uiState.username.isNotBlank()
								&& uiState.email.isNotBlank() && uiState.password.isNotBlank()
						) {
							Text(text = "Register")
							if (uiState.isLoading) {
								Box(modifier = Modifier.padding(start = 8.dp)) {
									LoadingIndicator(size = 24.dp, color = Color.Black)
								}
							}
						}
						Row {
							Text(text = "Already have an account? ")
							Text(
								text = "Sign In",
								style = LocalTextStyle.current.copy(
									fontWeight = FontWeight.SemiBold,
									textDecoration = TextDecoration.Underline
								),
								modifier = Modifier.clickable { navController.popBackStack() }
							)
						}
						Text(
							text = "Money Manager",
							style = Typography.headlineSmall.copy(
								fontWeight = FontWeight.Bold,
								color = Watermark
							),
							modifier = Modifier.padding(top = 35.dp)
						)
					}
				}
			}
		}
	)
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUp() {
	MoneyManagerTheme {
		SignUp(navController = rememberNavController())
	}
}