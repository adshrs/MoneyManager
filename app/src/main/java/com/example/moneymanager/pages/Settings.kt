package com.example.moneymanager.pages

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moneymanager.R
import com.example.moneymanager.components.LoadingIndicator
import com.example.moneymanager.components.TableRow
import com.example.moneymanager.ui.theme.BackgroundElevated
import com.example.moneymanager.ui.theme.Destructive
import com.example.moneymanager.ui.theme.DividerColor
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.viewmodels.SettingsViewModel

@SuppressLint("OpaqueUnitKey")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
	navController: NavController,
	settingsViewModel: SettingsViewModel = hiltViewModel()
) {

	val uiState by settingsViewModel.uiState.collectAsState()
	val context = LocalContext.current

	LaunchedEffect(settingsViewModel, context) {
		settingsViewModel.statusNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					Toast.makeText(context, result.data.toString(), Toast.LENGTH_SHORT)
						.show()
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						settingsViewModel.logout()
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
							Icons.Filled.Settings,
							contentDescription = "settings"
						)
						Spacer(modifier = Modifier.width(8.dp))
						Text(text = "Settings", style = Typography.titleMedium)
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
				modifier = Modifier.clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
			)
		}
	) { innerPadding ->
		if (uiState.isLoading) {
			Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
				LoadingIndicator()
			}
		} else {
			Column(modifier = Modifier.padding(innerPadding)) {
				Column(
					modifier = Modifier
						.padding(horizontal = 8.dp, vertical = 16.dp)
						.clip(RoundedCornerShape(8.dp))
						.background(BackgroundElevated)
						.fillMaxWidth()
				) {
					TableRow(
						label = "Erase all Data",
						isDestructive = true,
						modifier = Modifier.clickable {
							settingsViewModel.showEraseDataWarning()
						}
					)
					Row(
						modifier = Modifier
							.background(BackgroundElevated)
							.height(1.dp)
					) {
						HorizontalDivider(thickness = 1.dp, color = DividerColor)
					}
					TableRow(
						label = "Log out",
						isDestructive = true,
						modifier = Modifier.clickable {
							settingsViewModel.showLogoutWarning()
						}
					)
				}
			}

			if (uiState.logoutWarningVisible) {
				AlertDialog(
					onDismissRequest = settingsViewModel::hideLogoutWarning,
					confirmButton = {
						OutlinedButton(
							onClick = {
								settingsViewModel.logout()
								settingsViewModel.hideLogoutWarning()
								navController.navigate("signin")
							},
							border = BorderStroke(1.dp, Destructive)
						) {
							Text(text = "Yes", color = Color.White)
						}
					},
					dismissButton = {
						OutlinedButton(
							onClick = settingsViewModel::hideLogoutWarning
						) {
							Text(text = "No", color = Color.White)
						}
					},
					title = { Text(text = "Logout", style = Typography.headlineLarge) },
					text = {
						Text(
							text = "Are you sure you want to logout?",
							style = Typography.labelMedium
						)
					},
					iconContentColor = Destructive,
					titleContentColor = Destructive,
					containerColor = BackgroundElevated,
					shape = RoundedCornerShape(20.dp)
				)
			}

			if (uiState.eraseDataWarningVisible) {
				AlertDialog(
					onDismissRequest = settingsViewModel::hideEraseDataWarning,
					confirmButton = {
						OutlinedButton(
							onClick = {
								settingsViewModel.eraseAllData()
								settingsViewModel.hideEraseDataWarning()
							},
							border = BorderStroke(1.dp, Destructive)
						) {
							Text(text = "Yes, i'm sure", color = Color.White)
						}
					},
					dismissButton = {
						OutlinedButton(
							onClick = settingsViewModel::hideEraseDataWarning
						) {
							Text(text = "No", color = Color.White)
						}
					},
					title = { Text(text = "Erase all data", style = Typography.headlineLarge) },
					text = {
						Text(
							text = "Are you sure you want to erase all data? " +
									  "This will permanently delete all of your account's data",
							style = Typography.labelMedium
						)
					},
					icon = {
						Icon(
							painterResource(id = R.drawable.icon_delete),
							contentDescription = "Erase"
						)
					},
					iconContentColor = Destructive,
					titleContentColor = Destructive,
					containerColor = BackgroundElevated,
					shape = RoundedCornerShape(20.dp)
				)
			}
		}
	}
}
