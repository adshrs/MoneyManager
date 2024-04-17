package com.example.moneymanager.pages

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneymanager.R
import com.example.moneymanager.components.LoadingIndicator
import com.example.moneymanager.components.TableRow
import com.example.moneymanager.components.textFields.UnstyledTextField
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.models.expense.ExpenseRequest
import com.example.moneymanager.ui.theme.BackgroundElevated
import com.example.moneymanager.ui.theme.DisabledButton
import com.example.moneymanager.ui.theme.DividerColor
import com.example.moneymanager.ui.theme.FillTertiary
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.Primary
import com.example.moneymanager.ui.theme.SystemGray04
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.utils.parseColorString
import com.example.moneymanager.viewmodels.AddViewModel
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Add(
	navController: NavController,
	addViewModel: AddViewModel = hiltViewModel()
) {
	val uiState by addViewModel.uiState.collectAsState()
	val context = LocalContext.current

	val keyboardController = LocalSoftwareKeyboardController.current

	val recurrences = listOf(
		Recurrence.None,
		Recurrence.Daily,
		Recurrence.Weekly,
		Recurrence.Monthly,
		Recurrence.Yearly
	)

	LaunchedEffect(addViewModel, context) {
		addViewModel.categoryNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					addViewModel.updateCategories(result.data!!)
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						addViewModel.removeToken()
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

	LaunchedEffect(addViewModel, context) {
		addViewModel.statusNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					addViewModel.revertFields()
					navController.navigate("home/expenses") {
					}
					Toast.makeText(context, "New expense added", Toast.LENGTH_SHORT)
						.show()
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						addViewModel.removeToken()

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
				title = { Text(text = "Add", style = Typography.titleMedium) },
				colors = TopAppBarDefaults.mediumTopAppBarColors(
					containerColor = TopAppBarBackground
				)
			)
		},
		content = { innerPadding ->
			LazyColumn(
				modifier = Modifier.padding(innerPadding),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				val canSubmitForm = uiState.amount.isNotEmpty()
						  && uiState.description.isNotEmpty()
						  && !uiState.categoryName.isNullOrEmpty()

				if (!canSubmitForm) {
					item {
						Surface (
							modifier = Modifier
								.padding(top = 16.dp)
								.padding(horizontal = 8.dp),
							border = BorderStroke(1.5.dp, Primary.copy(alpha = 0.7f)),
							shape = RoundedCornerShape(15.dp),

						){
							Row(
								modifier = Modifier
									.fillMaxWidth()
									.padding(horizontal = 10.dp, vertical = 6.dp),
								verticalAlignment = Alignment.CenterVertically,
							) {
								Icon(
									imageVector = Icons.Filled.Info,
									contentDescription = "form info",
									modifier = Modifier.size(20.dp),
									tint = Primary.copy(alpha = 0.8f)
								)
								Text(
									text = "Fill in all of the fields.",
									style = Typography.headlineSmall,
									modifier = Modifier.padding(start = 8.dp),
									color = Primary.copy(alpha = 0.8f)
								)
							}
						}
					}
				}

				item {
					Column(
						modifier = Modifier
							.padding(horizontal = 8.dp, vertical = 16.dp)
							.clip(RoundedCornerShape(10.dp))
							.background(BackgroundElevated)
							.fillMaxWidth()
					) {
						TableRow(
							modifier = Modifier.padding(vertical = 4.dp),
							label = "Amount",
							detailContent = {
								UnstyledTextField(
									value = uiState.amount,
									onValueChange = addViewModel::setAmount,
									placeholder = { Text(text = "0") },
									modifier = Modifier.fillMaxWidth(),
									arrangement = Arrangement.End,
									textStyle = TextStyle(
										textAlign = TextAlign.End
									),
									singleLine = true,
									keyboardOptions = KeyboardOptions(
										keyboardType = KeyboardType.Number
									)
								)
							}
						)

						HorizontalDivider(thickness = 1.dp, color = DividerColor)

						TableRow(
							modifier = Modifier.padding(vertical = 4.dp),
							label = "Date",
							detailContent = {
								var datePickerOpened by remember {
									mutableStateOf(false)
								}

								Surface(
									shape = RoundedCornerShape(6.dp),
									color = FillTertiary,
									onClick = { datePickerOpened = true },
								) {
									Row(
										modifier = Modifier
											.padding(
												start = 10.dp,
												end = 7.dp,
												top = 8.dp,
												bottom = 8.dp
											)
											.width(150.dp),
										verticalAlignment = Alignment.CenterVertically,
										horizontalArrangement = Arrangement.SpaceEvenly
									) {
										Text(
											text = uiState.date.toString(),
											style = Typography.labelMedium,
											modifier = Modifier.weight(1f)
										)
										Icon(
											painterResource(id = R.drawable.icon_date_range),
											contentDescription = "Open picker",
											modifier = Modifier.padding(start = 6.dp),
											tint = TextSecondary
										)
									}
								}

								if (datePickerOpened) {
									DatePickerDialog(
										onDismissRequest = { datePickerOpened = false },
										onDateChange = {
											addViewModel.setDate(it)
											datePickerOpened = false
										},
										initialDate = uiState.date,
										containerColor = BackgroundElevated
									)
								}
							}
						)

						HorizontalDivider(thickness = 1.dp, color = DividerColor)

						TableRow(
							modifier = Modifier.padding(vertical = 4.dp),
							label = "Description",
							detailContent = {
								UnstyledTextField(
									value = uiState.description,
									placeholder = { Text(text = "Write something") },
									arrangement = Arrangement.End,
									onValueChange = addViewModel::setNote,
									modifier = Modifier.fillMaxWidth(),
									textStyle = TextStyle(
										textAlign = TextAlign.End
									),
									singleLine = true,
									keyboardOptions = KeyboardOptions(
										keyboardType = KeyboardType.Text
									),
								)
							}
						)

						HorizontalDivider(thickness = 1.dp, color = DividerColor)

						TableRow(
							modifier = Modifier.padding(vertical = 4.dp),
							label = "Category",
							detailContent = {
								var categoriesMenuOpened by remember {
									mutableStateOf(false)
								}

								Surface(
									shape = RoundedCornerShape(6.dp),
									color = FillTertiary,
									onClick = { categoriesMenuOpened = true },
								) {
									Row(
										modifier = Modifier
											.padding(
												start = 10.dp,
												end = 7.dp,
												top = 8.dp,
												bottom = 8.dp
											)
											.width(150.dp),
										verticalAlignment = Alignment.CenterVertically,
										horizontalArrangement = Arrangement.SpaceEvenly
									) {
										Row (modifier = Modifier.weight(1f)) {
											if (!uiState.categoryName.isNullOrEmpty()) {
												Surface(
													color = parseColorString(uiState.selectedCategoryColor!!),
													shape = RoundedCornerShape(6.dp),
													border = BorderStroke(
														width = 2.dp,
														color = Color.White
													),
													modifier = Modifier.size(16.dp)
												) {}
												Text(
													text = uiState.categoryName ?: "Select category",
													style = Typography.labelMedium,
													modifier = Modifier.padding(start = 8.dp)
												)
											}
											else {
												Text(
													text = uiState.categoryName ?: "Select category",
													style = Typography.labelMedium,
												)
											}
										}
										Icon(
											painterResource(id = R.drawable.icon_unfold_more),
											contentDescription = "Open picker",
											modifier = Modifier.padding(start = 6.dp),
											tint = TextSecondary
										)
									}
									DropdownMenu(
										expanded = categoriesMenuOpened,
										onDismissRequest = { categoriesMenuOpened = false },
										modifier = Modifier
											.background(
												color = com.example.moneymanager.ui.theme.Surface,
												shape = RoundedCornerShape(4.dp)
											)
											.border(1.dp, SystemGray04, RoundedCornerShape(4.dp))
									) {
										if (uiState.isLoading) {
											LoadingIndicator(size = 24.dp, color = Color.Black)
										}
										uiState.categories.forEach { category ->
											DropdownMenuItem(
												text = {
													Row(verticalAlignment = Alignment.CenterVertically) {
														//TODO: Change the color based on the category
														Surface(
															color = parseColorString(category.color),
															shape = RoundedCornerShape(6.dp),
															border = BorderStroke(
																width = 2.dp,
																color = Color.White
															),
															modifier = Modifier.size(16.dp)
														) {}
														Text(
															text = category.name,
															modifier = Modifier.padding(start = 8.dp)
														)
													}
												},
												onClick = {
													addViewModel.setCategoryName(category.name)
													addViewModel.setCategoryId(category.id)
													addViewModel.setCategoryColor(category.color)
													categoriesMenuOpened = false
												}
											)
										}
									}
								}
							}
						)
					}
				}

				item {
					Button(
						onClick = {
							addViewModel.addExpense(
								ExpenseRequest(
									uiState.amount.toDouble(),
									uiState.date.toString(),
									uiState.description,
									uiState.categoryId!!
								)
							)
							keyboardController?.hide()
						},
						modifier = Modifier
							.padding(16.dp)
							.width(200.dp)
							.height(50.dp)
							.border(4.dp, BackgroundElevated, RoundedCornerShape(100f)),
						shape = RoundedCornerShape(100f),
						colors = ButtonColors(
							containerColor = com.example.moneymanager.ui.theme.Button,
							contentColor = Color.Black,
							disabledContainerColor = DisabledButton,
							disabledContentColor = Color.Black
						),
						enabled = uiState.amount.isNotEmpty()
								  && uiState.description.isNotEmpty()
								  && !uiState.categoryName.isNullOrEmpty()
					) {
						Text(text = "Add")
						if (uiState.isLoading) {
							Box(modifier = Modifier.padding(start = 8.dp)) {
								LoadingIndicator(size = 24.dp, color = Color.Black)
							}
						}
					}
				}
			}
		}
	)
}

@Preview(showBackground = true)
@Composable
fun PreviewAdd() {
	MoneyManagerTheme {
		Add(navController = rememberNavController())
	}
}