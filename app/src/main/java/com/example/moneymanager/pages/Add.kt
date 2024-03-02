package com.example.moneymanager.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneymanager.components.TableRow
import com.example.moneymanager.components.UnstyledTextField
import com.example.moneymanager.models.Recurrence
import com.example.moneymanager.ui.theme.BackgroundElevated
import com.example.moneymanager.ui.theme.DividerColor
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.Primary
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.viewmodels.AddViewModel
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Add(navController: NavController, addViewModel: AddViewModel = viewModel()) {
	val state by addViewModel.uiState.collectAsState()
	val recurrences = listOf(
		Recurrence.None,
		Recurrence.Daily,
		Recurrence.Weekly,
		Recurrence.Monthly,
		Recurrence.Yearly
	)
	val categories = listOf(
		"Groceries",
		"Food",
		"Stationary",
		"Cosmetics",
		"Furniture",
		"Electronics"
	)

	Scaffold(
		topBar = {
			MediumTopAppBar(
				title = { Text(text = "Add") },
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
				item {
					Column(
						modifier = Modifier
							.padding(16.dp)
							.clip(RoundedCornerShape(10.dp))
							.background(BackgroundElevated)
							.fillMaxWidth()
					) {
						TableRow("Amount") {
							UnstyledTextField(
								value = state.amount,
								onValueChange = addViewModel::setAmount,
								placeholder = { Text(text = "0") },
								modifier = Modifier.fillMaxWidth(),
								arrangement = Arrangement.End,
								textStyle = TextStyle(
									textAlign = TextAlign.End
								),
								maxLines = 1,
								keyboardOptions = KeyboardOptions(
									keyboardType = KeyboardType.Number
								)
							)
						}

						Divider(thickness = 1.dp, color = DividerColor)

						TableRow("Recurrence") {
							var recurrenceMenuOpened by remember {
								mutableStateOf(false)
							}

							TextButton(
								onClick = { recurrenceMenuOpened = true },
								contentPadding = PaddingValues(),
								modifier = Modifier.widthIn(min = 1.dp),
								shape = RoundedCornerShape(10.dp)
							) {
								Text(text = state.recurrence?.name ?: Recurrence.None.name)
								DropdownMenu(
									expanded = recurrenceMenuOpened,
									onDismissRequest = { recurrenceMenuOpened = false },
									modifier = Modifier
								) {
									recurrences.forEach { recurrence ->
										DropdownMenuItem(
											text = { Text(text = recurrence.name) },
											onClick = {
												addViewModel.setRecurrence(recurrence)
												recurrenceMenuOpened = false
											}
										)
									}
								}
							}
						}

						Divider(thickness = 1.dp, color = DividerColor)

						TableRow("Date") {
							var datePickerOpened by remember {
								mutableStateOf(false)
							}

							TextButton(
								onClick = { datePickerOpened = true },
								contentPadding = PaddingValues(),
								modifier = Modifier.widthIn(min = 1.dp)
							) {
								Text(text = state.date.toString())
							}

							if (datePickerOpened) {
								DatePickerDialog(
									onDismissRequest = { datePickerOpened = false },
									onDateChange = {
										addViewModel.setDate(it)
										datePickerOpened = false
									},
									initialDate = state.date
								)
							}

						}

						Divider(thickness = 1.dp, color = DividerColor)

						TableRow("Note") {
							UnstyledTextField(
								value = state.note,
								placeholder = { Text(text = "Write a note") },
								arrangement = Arrangement.End,
								onValueChange = addViewModel::setNote,
								modifier = Modifier.fillMaxWidth(),
								textStyle = TextStyle(
									textAlign = TextAlign.End
								),
								keyboardOptions = KeyboardOptions(
									keyboardType = KeyboardType.Text
								),
							)
						}

						Divider(thickness = 1.dp, color = DividerColor)

						TableRow("Category") {
							var categoriesMenuOpened by remember {
								mutableStateOf(false)
							}

							TextButton(
								onClick = { categoriesMenuOpened = true },
								contentPadding = PaddingValues(),
								modifier = Modifier.widthIn(min = 1.dp)
							) {
								//TODO: Change the color of the text based on the selected category
								Text(text = state.category ?: "Select a category")
								DropdownMenu(
									expanded = categoriesMenuOpened,
									onDismissRequest = { categoriesMenuOpened = false },
									modifier = Modifier
								) {
									categories.forEach { category ->
										DropdownMenuItem(
											text = {
												Row(verticalAlignment = Alignment.CenterVertically) {
													//TODO: Change the color based on the category
													Surface(
														modifier = Modifier.size(10.dp),
														shape = CircleShape,
														color = Primary
													) {}
													Text(
														text = category,
														modifier = Modifier.padding(start = 8.dp)
													)
												}
											},
											onClick = {
												addViewModel.setCategory(category)
												categoriesMenuOpened = false
											}
										)
									}
								}
							}
						}
					}
				}

				item {
					Button(
						onClick = addViewModel::submit,
						modifier = Modifier
							.padding(16.dp)
							.width(200.dp),
						shape = RoundedCornerShape(10.dp)
					) {
						Text(text = "Add")
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