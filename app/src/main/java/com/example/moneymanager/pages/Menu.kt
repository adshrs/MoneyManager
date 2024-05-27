package com.example.moneymanager.pages

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moneymanager.R
import com.example.moneymanager.components.TableRow
import com.example.moneymanager.ui.theme.BackgroundElevated
import com.example.moneymanager.ui.theme.DividerColor
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.utils.ReportContent
import com.example.moneymanager.utils.generatePdf
import com.example.moneymanager.viewmodels.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
	navController: NavController,
	menuViewModel: MenuViewModel = hiltViewModel()
) {

	val uiState by menuViewModel.uiState.collectAsState()
	val context = LocalContext.current

	LaunchedEffect(menuViewModel, context) {
		menuViewModel.userNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					menuViewModel.updateUser(result.data!!)
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						menuViewModel.removeToken()
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

	LaunchedEffect(menuViewModel, context) {
		menuViewModel.expenseNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					menuViewModel.updateExpenses(result.data!!)
					menuViewModel.updateTotalExpense(result.data)
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						menuViewModel.removeToken()
						navController.navigate("signin")
						Toast.makeText(context, "Session Expired, sign in again", Toast.LENGTH_SHORT)
							.show()
					} else if (result.message.startsWith("Invalid compact JWT string")) {
						navController.navigate("signin")
					} else {
						Toast.makeText(context, result.message, Toast.LENGTH_SHORT)
							.show()
					}
				}

				is NetworkResult.Loading -> {}
			}
		}
	}

	LaunchedEffect(menuViewModel, context) {
		menuViewModel.incomeNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					menuViewModel.updateIncomes(result.data!!)
					menuViewModel.updateTotalIncome(result.data)
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						menuViewModel.removeToken()
						navController.navigate("signin")
						Toast.makeText(context, "Session Expired, sign in again", Toast.LENGTH_SHORT)
							.show()
					} else if (result.message.startsWith("Invalid compact JWT string")) {
						navController.navigate("signin")
					} else {
						Toast.makeText(context, result.message, Toast.LENGTH_SHORT)
							.show()
					}
				}

				is NetworkResult.Loading -> {}
			}
		}
	}

	val createDocumentLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.StartActivityForResult(),
		onResult = {
			it.data?.data?.let {
				context.contentResolver.openOutputStream(it)?.let {
					generatePdf(
						ReportContent(
							name = uiState.user!!.name,
							totalExpense = uiState.totalExpense,
							expenseData = uiState.expenses,
							totalIncome = uiState.totalIncome,
							incomeData = uiState.incomes
						),
						it
					)
				}
			}
		}
	)

	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Row(verticalAlignment = Alignment.CenterVertically) {
						Icon(
							painterResource(id = R.drawable.icon_navbar_menu_selected),
							contentDescription = "menu"
						)
						Spacer(modifier = Modifier.width(8.dp))
						Text(text = "Menu", style = Typography.titleMedium)
					}
				},
				colors = TopAppBarDefaults.mediumTopAppBarColors(
					containerColor = TopAppBarBackground
				),
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
				) {
					TableRow(
						content = {
							Row(verticalAlignment = Alignment.CenterVertically) {
								Icon(
									imageVector = Icons.Outlined.AccountCircle,
									contentDescription = "Account"
								)
								Text(
									text = "Account",
									modifier = Modifier.padding(
										horizontal = 16.dp,
										vertical = 10.dp
									)
								)
							}
						},
						hasArrow = true,
						modifier = Modifier.clickable {
							navController.navigate("menu/account")
						})
					HorizontalDivider(thickness = 1.dp, color = DividerColor)
					TableRow(
						content = {
							Row(verticalAlignment = Alignment.CenterVertically) {
								Icon(
									painterResource(id = R.drawable.icon_download_report),
									contentDescription = "Generate Report"
								)
								Text(
									text = "Generate Report",
									modifier = Modifier.padding(
										horizontal = 16.dp,
										vertical = 10.dp
									)
								)
							}
						},
						hasArrow = false,
						modifier = Modifier.clickable {
							Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
								type = "application/pdf"
								putExtra(Intent.EXTRA_TITLE, "money_manager_report.pdf")
							}.also {
								createDocumentLauncher.launch(it)
							}
						})
					HorizontalDivider(thickness = 1.dp, color = DividerColor)
					TableRow(
						content = {
							Row(verticalAlignment = Alignment.CenterVertically) {
								Icon(
									imageVector = Icons.Outlined.Settings,
									contentDescription = "Settings"
								)
								Text(
									text = "Settings",
									modifier = Modifier.padding(
										horizontal = 16.dp,
										vertical = 10.dp
									)
								)
							}
						},
						hasArrow = true,
						modifier = Modifier.clickable {
							navController.navigate("menu/settings")
						})
				}
			}
		}
	)
}