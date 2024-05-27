package com.example.moneymanager.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moneymanager.R
import com.example.moneymanager.components.textFields.UnstyledTextField
import com.example.moneymanager.models.user.ChangePasswordRequest
import com.example.moneymanager.models.user.UserRequest
import com.example.moneymanager.ui.theme.BackgroundElevated
import com.example.moneymanager.ui.theme.Button
import com.example.moneymanager.ui.theme.DisabledButton
import com.example.moneymanager.ui.theme.Primary
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.viewmodels.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Account(navController: NavController, accountViewModel: AccountViewModel = hiltViewModel()) {

	val uiState by accountViewModel.uiState.collectAsState()
	val context = LocalContext.current

	val keyboardController = LocalSoftwareKeyboardController.current

	LaunchedEffect(accountViewModel, context) {
		accountViewModel.userNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					accountViewModel.updateUser(result.data!!)
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						accountViewModel.removeToken()
						navController.navigate("signin")
						Toast.makeText(context, "Session Expired, sign in again", Toast.LENGTH_SHORT)
							.show()
					} else if (result.message.startsWith("Invalid compact JWT string")) {
						navController.navigate("signin")
					} else {
						Toast.makeText(context, result.message.toString(), Toast.LENGTH_SHORT)
							.show()
					}
				}

				is NetworkResult.Loading -> {}
			}
		}
	}

	LaunchedEffect(accountViewModel, context) {
		accountViewModel.statusNetworkResults.collect { result ->

			when (result) {
				is NetworkResult.Success -> {
					Toast.makeText(context, result.data, Toast.LENGTH_SHORT)
						.show()
						if (result.data == "Account details updated") {
							if (uiState.username == uiState.user?.username) {
								navController.navigate("menu/account") {
									popUpTo("menu/account") {
										inclusive = true
									}
								}
							} else {
								accountViewModel.removeToken()
								navController.navigate("signin")
								Toast.makeText(
									context,
									"Sign in again with your new username",
									Toast.LENGTH_SHORT
								)
									.show()
							}
						}

					if (result.data == "Your password has been changed") {
						accountViewModel.removeToken()
						navController.navigate("signin")
						Toast.makeText(context, result.data, Toast.LENGTH_SHORT)
							.show()
						Toast.makeText(
							context,
							"Sign in again with your new password",
							Toast.LENGTH_SHORT
						)
							.show()
					}
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						accountViewModel.removeToken()
						navController.navigate("signin")
						Toast.makeText(context, "Session Expired, sign in again", Toast.LENGTH_SHORT)
							.show()
					} else if (result.message.startsWith("Invalid compact JWT string")) {
						navController.navigate("signin")
					} else {
						Toast.makeText(context, result.message.toString(), Toast.LENGTH_SHORT)
							.show()
					}
				}

				is NetworkResult.Loading -> {}
			}
		}
	}

	Scaffold(
		topBar = {
			MediumTopAppBar(
				title = {
					Row(verticalAlignment = Alignment.CenterVertically) {
						Icon(
							Icons.Filled.AccountCircle,
							contentDescription = "account"
						)
						Spacer(modifier = Modifier.width(8.dp))
						Text(text = "Your Account", style = Typography.titleMedium)
					}
				},
				colors = TopAppBarDefaults.mediumTopAppBarColors(
					containerColor = TopAppBarBackground
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
							Text(text = "Menu", textAlign = TextAlign.Center)
						}
					}
				},
				actions = {
					IconButton(
						onClick = {
							accountViewModel.changeEditButtonActiveState()
							accountViewModel.setNewFullName(uiState.user?.name.toString())
							accountViewModel.setNewUsername(uiState.user?.username.toString())
							accountViewModel.setNewEmail(uiState.user?.email.toString())
						},
						colors = IconButtonColors(
							containerColor = if (uiState.isEditButtonActive) Color.White else Color.Transparent,
							contentColor = if (uiState.isEditButtonActive) Color.Black else Color.White,
							disabledContentColor = Primary,
							disabledContainerColor = Color.White
						)
					) {
						Icon(
							Icons.Outlined.Edit,
							contentDescription = "Edit account details"
						)
					}
				},
				modifier = Modifier.clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
			)
		},
		content = { innerPadding ->
			Column(modifier = Modifier.padding(innerPadding)) {
				Column(
					modifier = Modifier
						.padding(horizontal = 8.dp, vertical = 16.dp)
						.clip(RoundedCornerShape(8.dp))
						.background(BackgroundElevated)
						.fillMaxWidth()
						.fillMaxHeight(),
					verticalArrangement = Arrangement.SpaceBetween
				) {
					Column {
						val textFieldBorderColor =
							when (uiState.isEditButtonActive) {
								true -> Button
								false -> Color.Gray
							}

						if (uiState.isEditButtonActive) {
							Spacer(modifier = Modifier.size(20.dp))

							Row(
								modifier = Modifier
									.padding(horizontal = 18.dp)
									.padding(bottom = 10.dp)
									.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically
							) {
								Text(text = "Edit Mode", style = Typography.headlineMedium)
							}
						}

						Spacer(modifier = Modifier.size(20.dp))

						Row(
							modifier = Modifier
								.padding(horizontal = 18.dp)
								.padding(bottom = 10.dp)
								.fillMaxWidth(),
							verticalAlignment = Alignment.CenterVertically
						) {
							Text(text = "Full Name", style = Typography.labelMedium, color = Button)
						}
						Row(
							modifier = Modifier
								.padding(horizontal = 16.dp)
								.fillMaxWidth(),
							verticalAlignment = Alignment.CenterVertically
						) {
							Surface(
								color = Color.Transparent,
								modifier = Modifier
									.height(44.dp)
									.weight(1f)

									.border(1.dp, textFieldBorderColor, RoundedCornerShape(20.dp)),
								shape = RoundedCornerShape(20.dp)
							) {
								Column(
									verticalArrangement = Arrangement.Center,
									modifier = Modifier
										.fillMaxSize()
										.padding(start = 16.dp)
								) {
									UnstyledTextField(
										value = uiState.fullName,
										onValueChange = accountViewModel::setNewFullName,
										modifier = Modifier
											.fillMaxWidth(),
										singleLine = true,
										enabled = uiState.isEditButtonActive
									)
								}
							}
						}

						Spacer(modifier = Modifier.size(24.dp))

						Row(
							modifier = Modifier
								.padding(horizontal = 18.dp)
								.padding(bottom = 10.dp)
								.fillMaxWidth(),
							verticalAlignment = Alignment.CenterVertically
						) {
							Text(text = "Username", style = Typography.labelMedium, color = Button)
						}
						Row(
							modifier = Modifier
								.padding(horizontal = 16.dp)
								.fillMaxWidth(),
							verticalAlignment = Alignment.CenterVertically
						) {
							Surface(
								color = Color.Transparent,
								modifier = Modifier
									.height(44.dp)
									.weight(1f)

									.border(1.dp, textFieldBorderColor, RoundedCornerShape(20.dp)),
								shape = RoundedCornerShape(20.dp)
							) {
								Column(
									verticalArrangement = Arrangement.Center,
									modifier = Modifier
										.fillMaxSize()
										.padding(start = 16.dp)
								) {
									UnstyledTextField(
										value = uiState.username,
										onValueChange = accountViewModel::setNewUsername,
										modifier = Modifier
											.fillMaxWidth(),
										singleLine = true,
										enabled = uiState.isEditButtonActive
									)
								}
							}
						}

						Spacer(modifier = Modifier.size(24.dp))

						Row(
							modifier = Modifier
								.padding(horizontal = 18.dp)
								.padding(bottom = 10.dp)
								.fillMaxWidth(),
							verticalAlignment = Alignment.CenterVertically
						) {
							Text(text = "Email", style = Typography.labelMedium, color = Button)
						}
						Row(
							modifier = Modifier
								.padding(horizontal = 16.dp)
								.fillMaxWidth(),
							verticalAlignment = Alignment.CenterVertically
						) {
							Surface(
								color = Color.Transparent,
								modifier = Modifier
									.height(44.dp)
									.weight(1f)

									.border(1.dp, textFieldBorderColor, RoundedCornerShape(20.dp)),
								shape = RoundedCornerShape(20.dp)
							) {
								Column(
									verticalArrangement = Arrangement.Center,
									modifier = Modifier
										.fillMaxSize()
										.padding(start = 16.dp)
								) {
									UnstyledTextField(
										value = uiState.email,
										onValueChange = accountViewModel::setNewEmail,
										modifier = Modifier
											.fillMaxWidth(),
										singleLine = true,
										enabled = uiState.isEditButtonActive
									)
								}
							}
						}
					}

					Column {
						if (!uiState.isEditButtonActive) {
							Row(
								modifier = Modifier
									.padding(horizontal = 16.dp)
									.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.Center
							) {
								Button(
									onClick = accountViewModel::changePasswordDialogOpenedState,
									modifier = Modifier
										.fillMaxWidth()
										.height(50.dp)
										.border(5.dp, Color.Gray, RoundedCornerShape(20.dp)),
									shape = RoundedCornerShape(20.dp),
									colors = ButtonColors(
										containerColor = Button,
										contentColor = Color.Black,
										disabledContainerColor = DisabledButton,
										disabledContentColor = Color.Black
									)
								) {
									Text(text = "Change Password")
								}
							}
						}

						if (uiState.isEditButtonActive) {
							val isSaveButtonEnabled =
								(uiState.fullName.isNotBlank() && uiState.fullName != uiState.user?.name)
										  || (uiState.username.isNotBlank() && uiState.username != uiState.user?.username)
										  || (uiState.email.isNotBlank() && uiState.email != uiState.user?.email)

							Spacer(modifier = Modifier.size(16.dp))

							Row(
								modifier = Modifier
									.padding(horizontal = 16.dp)
									.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.Center
							) {
								Button(
									onClick = {
										accountViewModel.updateUserDetails(
											UserRequest(
												username = uiState.username,
												name = uiState.fullName,
												email = uiState.email,
												password = uiState.user?.password.toString()
											)
										)
										keyboardController?.hide()
									},
									modifier = Modifier
										.fillMaxWidth()
										.height(50.dp)
										.border(5.dp, Color.Gray, RoundedCornerShape(20.dp)),
									shape = RoundedCornerShape(20.dp),
									colors = ButtonColors(
										containerColor = Button,
										contentColor = Color.Black,
										disabledContainerColor = DisabledButton,
										disabledContentColor = Color.Black
									),
									enabled = isSaveButtonEnabled
								) {
									Text(text = "Save Changes")
								}
							}
						}

						Spacer(modifier = Modifier.size(20.dp))
					}
				}
			}

			if (uiState.changePasswordDialogOpened) {
				Dialog(
					onDismissRequest = {
						accountViewModel.changePasswordDialogOpenedState()
						accountViewModel.setNewOldPassword("")
						accountViewModel.setNewNewPassword("")
					},
					properties = DialogProperties(
						dismissOnClickOutside = false
					)
				) {
					Surface(
						shape = RoundedCornerShape(20.dp),
						color = BackgroundElevated,
						modifier = Modifier.height(400.dp)
					) {
						Column(
							verticalArrangement = Arrangement.SpaceBetween,
							horizontalAlignment = Alignment.CenterHorizontally,
							modifier = Modifier
								.fillMaxSize()
						) {
							Row(
								modifier = Modifier.padding(top = 32.dp)
							) {
								Text(text = "Change Password", style = Typography.headlineLarge)
							}

							Column(
								modifier = Modifier
									.padding(horizontal = 8.dp)

							) {
								Row(
									modifier = Modifier
										.padding(horizontal = 18.dp)
										.padding(bottom = 10.dp)
										.fillMaxWidth(),
									verticalAlignment = Alignment.CenterVertically
								) {
									Text(
										text = "Old Password",
										style = Typography.labelMedium,
										color = Button
									)
								}
								Row(
									modifier = Modifier
										.padding(horizontal = 16.dp)
										.fillMaxWidth(),
									verticalAlignment = Alignment.CenterVertically
								) {
									Surface(
										color = Color.Transparent,
										modifier = Modifier
											.height(44.dp)
											.weight(1f)

											.border(1.dp, Button, RoundedCornerShape(20.dp)),
										shape = RoundedCornerShape(20.dp)
									) {
										Column(
											verticalArrangement = Arrangement.Center,
											modifier = Modifier
												.fillMaxSize()
												.padding(start = 16.dp)
										) {
											UnstyledTextField(
												value = uiState.oldPassword,
												onValueChange = accountViewModel::setNewOldPassword,
												modifier = Modifier
													.fillMaxWidth(),
												singleLine = true,
												visualTransformation =
												if (uiState.oldPasswordVisible)
													VisualTransformation.None
												else
													PasswordVisualTransformation()
											)
										}
									}
									IconButton(onClick = { accountViewModel.changeOldPasswordVisible() }) {
										if (uiState.oldPasswordVisible) {
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
								}

								Spacer(modifier = Modifier.size(24.dp))

								Row(
									modifier = Modifier
										.padding(horizontal = 18.dp)
										.padding(bottom = 10.dp)
										.fillMaxWidth(),
									verticalAlignment = Alignment.CenterVertically
								) {
									Text(
										text = "New Password",
										style = Typography.labelMedium,
										color = Button
									)
								}
								Row(
									modifier = Modifier
										.padding(horizontal = 16.dp)
										.fillMaxWidth(),
									verticalAlignment = Alignment.CenterVertically
								) {
									Surface(
										color = Color.Transparent,
										modifier = Modifier
											.height(44.dp)
											.weight(1f)
											.border(1.dp, Button, RoundedCornerShape(20.dp)),
										shape = RoundedCornerShape(20.dp)
									) {
										Column(
											verticalArrangement = Arrangement.Center,
											modifier = Modifier
												.fillMaxSize()
												.padding(start = 16.dp)
										) {
											UnstyledTextField(
												value = uiState.newPassword,
												onValueChange = accountViewModel::setNewNewPassword,
												modifier = Modifier
													.fillMaxWidth(),
												singleLine = true,
												visualTransformation =
												if (uiState.newPasswordVisible)
													VisualTransformation.None
												else
													PasswordVisualTransformation()
											)
										}
									}

									IconButton(onClick = { accountViewModel.changeNewPasswordVisible() }) {
										if (uiState.newPasswordVisible) {
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
								}
							}

							Row(
								modifier = Modifier
									.padding(horizontal = 24.dp)
									.padding(bottom = 32.dp)
									.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.End
							) {
								OutlinedButton(
									onClick = {
										accountViewModel.changePasswordDialogOpenedState()
										accountViewModel.setNewOldPassword("")
										accountViewModel.setNewNewPassword("")
									}
								) {
									Text(text = "Back", color = Color.White)
								}

								Spacer(modifier = Modifier.size(12.dp))

								OutlinedButton(
									onClick = {
										accountViewModel.changePassword(
											ChangePasswordRequest(
												oldPassword = uiState.oldPassword,
												newPassword = uiState.newPassword
											)
										)
									}
								) {
									Text(text = "Submit", color = Color.White)
								}
							}
						}
					}
				}
			}
		}
	)
}