package com.example.moneymanager.pages

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneymanager.R
import com.example.moneymanager.components.AnalyticPage
import com.example.moneymanager.components.LoadingIndicator
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.ui.theme.MoneyManagerTheme
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

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = "Analytics", style = Typography.titleMedium) },
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
						modifier = Modifier
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
				}
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
				} else {
					if (uiState.expenses.isNotEmpty() && uiState.categories.isNotEmpty()) {
						AnalyticPage(
							innerPadding,
							page,
							uiState.recurrence,
							uiState.expenses,
							uiState.categories
						)
					}
				}
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