package com.example.moneymanager.pages

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneymanager.R
import com.example.moneymanager.components.ExpenseAnalyticPage
import com.example.moneymanager.components.IncomeAnalyticPage
import com.example.moneymanager.components.LoadingIndicator
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.ui.theme.BackgroundElevated
import com.example.moneymanager.ui.theme.Destructive
import com.example.moneymanager.ui.theme.FillTertiary
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.Surface
import com.example.moneymanager.ui.theme.SystemGray04
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.viewmodels.AnalyticsViewModel
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Analytics(
	navController: NavController,
	analyticsViewModel: AnalyticsViewModel = hiltViewModel()
) {

	val uiState by analyticsViewModel.uiState.collectAsState()
	val expensesDayGroupUiState by analyticsViewModel.expensesDayGroupUiState.collectAsState()
	val incomesDauGroupUiState by analyticsViewModel.incomesDayGroupUiState.collectAsState()
	val context = LocalContext.current

	val recurrences = listOf(
		Recurrence.Weekly,
		Recurrence.Monthly,
		Recurrence.Yearly
	)

	LaunchedEffect(analyticsViewModel, context) {
		analyticsViewModel.categoryNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					analyticsViewModel.updateCategories(result.data!!)
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						analyticsViewModel.removeToken()
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

	LaunchedEffect(analyticsViewModel, context) {
		analyticsViewModel.expenseNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					analyticsViewModel.updateExpenses(result.data!!)
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						analyticsViewModel.removeToken()
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

	LaunchedEffect(analyticsViewModel, context) {
		analyticsViewModel.incomeNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					analyticsViewModel.updateIncomes(result.data!!)
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						analyticsViewModel.removeToken()
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

	LaunchedEffect(analyticsViewModel, context) {
		analyticsViewModel.statusNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					navController.navigate("analytics") {
						popUpTo("analytics") {
							inclusive = true
						}
					}
					Toast.makeText(context, "Data Deleted", Toast.LENGTH_SHORT)
						.show()
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						analyticsViewModel.removeToken()
						navController.navigate("signin")
						Toast.makeText(context, "Session Expired, sign in again", Toast.LENGTH_SHORT)
							.show()
					} else if (result.message.startsWith("Invalid compact JWT string")) {
						navController.navigate("signin")
					}
				}

				is NetworkResult.Loading -> {}
			}
		}
	}

	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					var analyticsTypeMenuOpened by remember {
						mutableStateOf(false)
					}

					Row(verticalAlignment = Alignment.CenterVertically) {
						Surface(
							modifier = Modifier,
							shape = RoundedCornerShape(6.dp),
							color = FillTertiary,
							onClick = { analyticsTypeMenuOpened = true },
						) {
							Row(
								modifier = Modifier
									.padding(
										start = 10.dp,
										end = 7.dp,
										top = 8.dp,
										bottom = 8.dp
									)
									.width(250.dp),
								verticalAlignment = Alignment.CenterVertically,
								horizontalArrangement = Arrangement.SpaceEvenly
							) {
								Icon(
									painterResource(id = R.drawable.icon_navbar_analytics),
									contentDescription = "analytics"
								)
								Spacer(modifier = Modifier.width(8.dp))
								Text(
									text = "${uiState.analyticsType} Analytics",
									style = Typography.titleMedium,
									modifier = Modifier.weight(1f)
								)
								Icon(
									painterResource(id = R.drawable.icon_unfold_more),
									contentDescription = "Open picker",
									modifier = Modifier.padding(start = 6.dp),
									tint = TextSecondary
								)
							}
							DropdownMenu(
								expanded = analyticsTypeMenuOpened,
								onDismissRequest = { analyticsTypeMenuOpened = false },
								modifier = Modifier
									.background(
										color = Surface,
										shape = RoundedCornerShape(4.dp)
									)
									.width(250.dp)
									.border(1.dp, SystemGray04, RoundedCornerShape(4.dp))
							) {
								DropdownMenuItem(
									text = { Text(text = "Expense") },
									onClick = {
										analyticsViewModel.updateAnalyticsTypeSelection("Expense")
										analyticsTypeMenuOpened = false
									}
								)
								DropdownMenuItem(
									text = { Text(text = "Income") },
									onClick = {
										analyticsViewModel.updateAnalyticsTypeSelection("Income")
										analyticsTypeMenuOpened = false
									}
								)
							}
						}
					}
				},
				colors = TopAppBarDefaults.mediumTopAppBarColors(
					containerColor = TopAppBarBackground
				),
				actions = {
					IconButton(
						onClick = analyticsViewModel::openDateRangeMenu
					) {
						Icon(
							painterResource(id = R.drawable.icon_date_range),
							contentDescription = "filter date range"
						)
					}
					DropdownMenu(
						expanded = uiState.dateRangeMenuOpened,
						onDismissRequest = analyticsViewModel::closeDateRangeMenu,
						modifier = Modifier.background(Surface),
					) {
						recurrences.forEach { recurrence ->
							DropdownMenuItem(
								text = { Text(text = recurrence.name) },
								onClick = {
									analyticsViewModel.setRecurrence(recurrence)
									analyticsViewModel.closeDateRangeMenu()
								}
							)
						}
					}
				},
				modifier = Modifier.clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
			)
		},
		content = { innerPadding ->
			val pagerState = rememberPagerState(
				pageCount = {
					when (uiState.recurrence) {
						Recurrence.Weekly -> 53
						Recurrence.Monthly -> 12
						Recurrence.Yearly -> 1
						else -> 53
					}
				}
			)
			val settledPage = pagerState.settledPage
			val beyondBoundsPageCount = 0

			HorizontalPager(
				state = pagerState,
				reverseLayout = true,
				beyondBoundsPageCount = beyondBoundsPageCount
			) { page ->

				if (abs(settledPage - page) > beyondBoundsPageCount) {
					Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
						LoadingIndicator()
					}
				}
				if (uiState.isLoading) {
					Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
						LoadingIndicator()
					}
				}
				else {
					if (
						uiState.analyticsType == "Expense" && uiState.expenses.isNotEmpty()
					) {
						ExpenseAnalyticPage(
							innerPadding,
							page,
							uiState.recurrence,
							uiState.expenses,
							uiState.categories
						)
					}
					else if (
						uiState.analyticsType == "Income" && uiState.incomes.isNotEmpty()
					) {
						IncomeAnalyticPage(
							innerPadding,
							page,
							uiState.recurrence,
							uiState.incomes,
							uiState.categories
						)
					}
				}
			}

			if (expensesDayGroupUiState.deleteWarningVisible) {
				AlertDialog(
					onDismissRequest = analyticsViewModel::hideDeleteWarning,
					confirmButton = {
						OutlinedButton(
							onClick = {
								analyticsViewModel.deleteExpense(expensesDayGroupUiState.expenseIdToDelete)
								analyticsViewModel.hideDeleteWarning()
							},
							border = BorderStroke(1.dp, Destructive)
						) {
							Text(text = "Delete", color = Color.White)
						}
					},
					dismissButton = {
						OutlinedButton(
							onClick = analyticsViewModel::hideDeleteWarning
						) {
							Text(text = "Back", color = Color.White)
						}
					},
					title = { Text(text = "Remove Expense?", style = Typography.headlineLarge) },
					text = {
						Text(
							text = "This Expense will be removed permanently.",
							style = Typography.labelMedium
						)
					},
					icon = {
						Icon(
							painterResource(id = R.drawable.icon_delete),
							contentDescription = "Delete category"
						)
					},
					iconContentColor = Destructive,
					titleContentColor = Destructive,
					containerColor = BackgroundElevated,
					shape = RoundedCornerShape(20.dp)
				)
			}

			if (incomesDauGroupUiState.deleteWarningVisible) {
				AlertDialog(
					onDismissRequest = analyticsViewModel::hideDeleteWarning,
					confirmButton = {
						OutlinedButton(
							onClick = {
								analyticsViewModel.deleteIncome(incomesDauGroupUiState.incomeIdToDelete)
								analyticsViewModel.hideDeleteWarning()
							},
							border = BorderStroke(1.dp, Destructive)
						) {
							Text(text = "Delete", color = Color.White)
						}
					},
					dismissButton = {
						OutlinedButton(
							onClick = analyticsViewModel::hideDeleteWarning
						) {
							Text(text = "Back", color = Color.White)
						}
					},
					title = { Text(text = "Remove Income?", style = Typography.headlineLarge) },
					text = {
						Text(
							text = "This income will be removed permanently.",
							style = Typography.labelMedium
						)
					},
					icon = {
						Icon(
							painterResource(id = R.drawable.icon_delete),
							contentDescription = "Delete category"
						)
					},
					iconContentColor = Destructive,
					titleContentColor = Destructive,
					containerColor = BackgroundElevated,
					shape = RoundedCornerShape(20.dp)
				)
			}
		}
	)
}

@Preview
@Composable
fun AnalyticsPreview() {
	MoneyManagerTheme {
		Analytics(navController = rememberNavController())
	}
}