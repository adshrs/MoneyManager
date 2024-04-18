package com.example.moneymanager.pages

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moneymanager.R
import com.example.moneymanager.components.LoadingIndicator
import com.example.moneymanager.components.expensesList.ExpensesList
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.ui.theme.BackgroundElevated
import com.example.moneymanager.ui.theme.Destructive
import com.example.moneymanager.ui.theme.FillTertiary
import com.example.moneymanager.ui.theme.Surface
import com.example.moneymanager.ui.theme.SystemGray04
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.viewmodels.ExpensesViewModel
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Expenses(
	navController: NavController,
	expensesViewModel: ExpensesViewModel = hiltViewModel()
) {
	val uiState by expensesViewModel.uiState.collectAsState()
	val expensesDayGroupUiState by expensesViewModel.expensesDayGroupUiState.collectAsState()
	val context = LocalContext.current

	val recurrences = listOf(
		Recurrence.Daily,
		Recurrence.Weekly,
		Recurrence.Monthly,
		Recurrence.Yearly
	)

	var recurrenceMenuOpened by remember {
		mutableStateOf(false)
	}

	LaunchedEffect(expensesViewModel, context) {
		expensesViewModel.categoryNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					expensesViewModel.updateCategories(result.data!!)
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						expensesViewModel.removeToken()
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

	LaunchedEffect(expensesViewModel, context) {
		expensesViewModel.expenseNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					expensesViewModel.updateExpenses(result.data!!)
					expensesViewModel.setRecurrence(uiState.recurrence)
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						expensesViewModel.removeToken()
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

	LaunchedEffect(expensesViewModel, context) {
		expensesViewModel.statusNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					navController.navigate("home/expenses") {
						popUpTo("home/expenses") {
							inclusive = true
						}
					}
					Toast.makeText(context, "Expense Deleted", Toast.LENGTH_SHORT)
						.show()
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						expensesViewModel.removeToken()
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



	if (uiState.isLoading) {
		Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
			LoadingIndicator()
		}
	} else {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 8.dp)
				.padding(top = 16.dp, bottom = 55.dp),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Row(verticalAlignment = Alignment.CenterVertically) {
				Text(
					text = "Total for:",
					style = Typography.bodyMedium
				)
				Surface(
					modifier = Modifier.padding(start = 16.dp),
					shape = RoundedCornerShape(6.dp),
					color = FillTertiary,
					onClick = { recurrenceMenuOpened = true },
				) {
					Row(
						modifier = Modifier
							.padding(
								start = 10.dp,
								end = 7.dp,
								top = 8.dp,
								bottom = 8.dp
							)
							.width(110.dp),
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.SpaceEvenly
					) {
						Text(
							text = uiState.recurrence.target,
							style = Typography.bodySmall,
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
						expanded = recurrenceMenuOpened,
						onDismissRequest = { recurrenceMenuOpened = false },
						modifier = Modifier
							.background(
								color = Surface,
								shape = RoundedCornerShape(4.dp)
							)
							.border(1.dp, SystemGray04, RoundedCornerShape(4.dp))
					) {
						recurrences.forEach { recurrence ->
							DropdownMenuItem(
								text = { Text(text = recurrence.target) },
								onClick = {
									expensesViewModel.setRecurrence(recurrence)
									recurrenceMenuOpened = false
								}
							)
						}
					}
				}
			}
			Row(modifier = Modifier.padding(vertical = 32.dp)) {
				Text(
					text = "Rs.",
					style = Typography.bodyMedium,
					color = TextSecondary,
					modifier = Modifier.padding(end = 4.dp)
				)
				Text(
					text = DecimalFormat("0.#").format(uiState.sumTotal),
					style = Typography.titleLarge,
					color = Destructive
				)
			}
			ExpensesList(
				expenses = uiState.filteredExpenses,
				categories = uiState.categories,
				modifier = Modifier
					.padding(0.dp)
					.verticalScroll(rememberScrollState())
			)
		}

		if (expensesDayGroupUiState.deleteWarningVisible) {
			AlertDialog(
				onDismissRequest = expensesViewModel::hideDeleteWarning,
				confirmButton = {
					OutlinedButton(
						onClick = {
							expensesViewModel.deleteExpense(expensesDayGroupUiState.expenseIdToDelete)
							expensesViewModel.hideDeleteWarning()
						},
						border = BorderStroke(1.dp, Destructive)
					) {
						Text(text = "Delete", color = Color.White)
					}
				},
				dismissButton = {
					OutlinedButton(
						onClick = expensesViewModel::hideDeleteWarning
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
						contentDescription = "Delete expense"
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