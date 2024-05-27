package com.example.moneymanager.pages

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.example.moneymanager.components.LoadingIndicator
import com.example.moneymanager.components.TableRow
import com.example.moneymanager.ui.theme.BackgroundElevated
import com.example.moneymanager.ui.theme.Button
import com.example.moneymanager.ui.theme.Destructive
import com.example.moneymanager.ui.theme.DividerColor
import com.example.moneymanager.ui.theme.FillTertiary
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.SystemGray04
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.utils.parseColorString
import com.example.moneymanager.viewmodels.CategoriesViewModel
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Categories(
	navController: NavController,
	categoriesViewModel: CategoriesViewModel = hiltViewModel()
) {
	val uiState by categoriesViewModel.uiState.collectAsState()
	val context = LocalContext.current

	LaunchedEffect(categoriesViewModel, context) {
		categoriesViewModel.expenseCategoryNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					categoriesViewModel.updateExpenseCategories(result.data!!)
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						categoriesViewModel.removeToken()
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

	LaunchedEffect(categoriesViewModel, context) {
		categoriesViewModel.incomeCategoryNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					categoriesViewModel.updateIncomeCategories(result.data!!)
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						categoriesViewModel.removeToken()
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

	LaunchedEffect(categoriesViewModel, context) {
		categoriesViewModel.statusNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					navController.navigate("categories") {
						popUpTo("categories") {
							inclusive = true
						}
					}
					Toast.makeText(context, "Category Deleted", Toast.LENGTH_SHORT)
						.show()
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						categoriesViewModel.removeToken()
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
					Row(verticalAlignment = Alignment.CenterVertically) {
						Icon(
							painterResource(id = R.drawable.icon_navbar_categories_selected),
							contentDescription = "categories"
						)
						Spacer(modifier = Modifier.width(8.dp))
						Text(text = "Categories", style = Typography.titleMedium)
					}
				},
				colors = TopAppBarDefaults.mediumTopAppBarColors(
					containerColor = TopAppBarBackground
				),
				actions = {
					var typeMenuOpened by remember {
						mutableStateOf(false)
					}

					Surface(
						modifier = Modifier.padding(horizontal = 16.dp),
						shape = RoundedCornerShape(6.dp),
						color = FillTertiary,
						onClick = { typeMenuOpened = true },
					) {
						Row(
							modifier = Modifier
								.padding(
									start = 10.dp,
									end = 7.dp,
									top = 8.dp,
									bottom = 8.dp
								)
								.width(90.dp),
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.SpaceEvenly
						) {
							Text(
								text = uiState.dropdownSelection,
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
							expanded = typeMenuOpened,
							onDismissRequest = { typeMenuOpened = false },
							modifier = Modifier
								.background(
									color = com.example.moneymanager.ui.theme.Surface,
									shape = RoundedCornerShape(4.dp)
								)
								.border(1.dp, SystemGray04, RoundedCornerShape(4.dp))
						) {
							DropdownMenuItem(
								text = { Text(text = "Expense") },
								onClick = {
									categoriesViewModel.setDropdownSelection("Expense")
									typeMenuOpened = false
								}
							)
							DropdownMenuItem(
								text = { Text(text = "Income") },
								onClick = {
									categoriesViewModel.setDropdownSelection("Income")
									typeMenuOpened = false
								}
							)
						}
					}
				},
				modifier = Modifier.clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
			)
		},
		floatingActionButton = {
			Row(
				modifier = Modifier
					.padding(bottom = 55.dp)
			) {
				ExtendedFloatingActionButton(
					onClick = {
						navController.navigate("categories/addcategory")
					},
					modifier = Modifier
						.height(50.dp)
						.border(
							4.dp,
							Color.Gray,
							RoundedCornerShape(
								topStart = 20.dp,
								topEnd = 20.dp,
								bottomStart = 20.dp,
								bottomEnd = 5.dp
							)
						),
					shape = RoundedCornerShape(
						topStart = 20.dp,
						topEnd = 20.dp,
						bottomStart = 20.dp,
						bottomEnd = 5.dp
					),
					containerColor = Button,
					contentColor = Color.Black
				) {
					Icon(
						Icons.Filled.Add,
						contentDescription = "Create new category"
					)
					Spacer(modifier = Modifier.width(8.dp))
					Text(text = "New", modifier = Modifier.padding(end = 5.dp))
				}
			}
		},
		content = { innerPadding ->
			if (uiState.isLoading) {
				Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
					LoadingIndicator()
				}
			}

			if (uiState.dropdownSelection == "Expense" && uiState.expenseCategories.isEmpty()) {
				Column(
					modifier = Modifier.fillMaxSize(),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Center
				) {
					Text(text = "No categories.")
					Text(text = "Click the '+ New' button to add a expense")
					Text(text = "category.")
				}
			}

			if (uiState.dropdownSelection == "Income" && uiState.incomeCategories.isEmpty()) {
				Column(
					modifier = Modifier.fillMaxSize(),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Center
				) {
					Text(text = "No categories.")
					Text(text = "Click the '+ New' button to add a income")
					Text(text = "category.")
				}
			} else {
				Column(
					modifier = Modifier
						.padding(innerPadding)
						.fillMaxHeight()
						.padding(top = 16.dp, bottom = 55.dp),
					verticalArrangement = Arrangement.Top
				) {
					val items = when (uiState.dropdownSelection) {
						"Expense" -> uiState.expenseCategories
						"Income" -> uiState.incomeCategories
						else -> null
					}

					AnimatedVisibility(visible = true) {
						LazyColumn(
							modifier = Modifier
								.padding(horizontal = 8.dp)
								.clip(RoundedCornerShape(8.dp))
								.fillMaxWidth()
						) {
							itemsIndexed(
								items = items!!,
								key = { _, category -> category.id }
							) { index, category ->
								SwipeableActionsBox(
									endActions = listOf(
										SwipeAction(
											icon = painterResource(id = R.drawable.icon_delete),
											background = Destructive,
											onSwipe = {
												categoriesViewModel.showDeleteWarning(category.id)
											}
										)
									),
									swipeThreshold = 80.dp,
									modifier = Modifier.animateItemPlacement()
								) {
									TableRow(
										modifier = Modifier.background(BackgroundElevated),
										detailContent = {
											Row(
												verticalAlignment = Alignment.CenterVertically
											) {
												Icon(
													Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
													contentDescription = "Swipe to Remove",
													tint = TextSecondary
												)
												Text(
													text = "Remove",
													modifier = Modifier,
													style = Typography.labelMedium,
													color = TextSecondary
												)
											}
										}
									) {
										Row(verticalAlignment = Alignment.CenterVertically) {
											Surface(
												color = parseColorString(category.color),
												shape = RoundedCornerShape(6.dp),
												border = BorderStroke(
													width = 2.dp,
													color = Color.White
												),
												modifier = Modifier.size(18.dp)
											) {}
											Text(
												text = category.name,
												modifier = Modifier.padding(
													horizontal = 16.dp,
													vertical = 10.dp
												)
											)
										}
									}
								}
								if (uiState.dropdownSelection == "Expense" && index < uiState.expenseCategories.size - 1) {
									Row(
										modifier = Modifier
											.background(BackgroundElevated)
											.height(1.dp)
									) {
										HorizontalDivider(thickness = 1.dp, color = DividerColor)
									}
								}
								if (uiState.dropdownSelection == "Income" && index < uiState.incomeCategories.size - 1) {
									Row(
										modifier = Modifier
											.background(BackgroundElevated)
											.height(1.dp)
									) {
										HorizontalDivider(thickness = 1.dp, color = DividerColor)
									}
								}

							}
						}
					}
				}

				if (uiState.deleteWarningVisible) {
					AlertDialog(
						onDismissRequest = categoriesViewModel::hideDeleteWarning,
						confirmButton = {
							OutlinedButton(
								onClick = {
									categoriesViewModel.deleteCategory(uiState.categoryIdToDelete)
									categoriesViewModel.hideDeleteWarning()
								},
								border = BorderStroke(1.dp, Destructive)
							) {
								Text(text = "Delete", color = Color.White)
							}
						},
						dismissButton = {
							OutlinedButton(
								onClick = categoriesViewModel::hideDeleteWarning
							) {
								Text(text = "Back", color = Color.White)
							}
						},
						title = { Text(text = "Remove Permanently?", style = Typography.headlineLarge) },
						text = {
							Text(
								text =
								if (uiState.dropdownSelection == "Expense")
									"After deleting this category, all existing expenses that belongs " +
											  "to this category will be tagged with a default category 'expense'."
								else
									"After deleting this category, all existing incomes that belongs " +
											  "to this category will be tagged with a default category 'income'.",
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
		}
	)
}

@Preview
@Composable
fun CategoriesPreview() {
	MoneyManagerTheme {
		Categories(navController = rememberNavController())
	}
}