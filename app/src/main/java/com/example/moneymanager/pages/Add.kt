package com.example.moneymanager.pages

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneymanager.components.TableRow
import com.example.moneymanager.components.UnstyledTextField
import com.example.moneymanager.ui.theme.BackgroundElevated
import com.example.moneymanager.ui.theme.DividerColor
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.Primary
import com.example.moneymanager.ui.theme.TopAppBarBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Add(navController: NavController) {
	val recurrences = listOf(
		"None",
		"Daily",
		"Weekly",
		"Monthly",
		"Yearly"
	)
	var selectedRecurrence by remember {
		mutableStateOf(recurrences[0])
	}

	val categories = listOf(
		"Groceries",
		"Food",
		"Stationary",
		"Cosmetics",
		"Furniture",
		"Electronics"
	)
	var selectedCategory by remember {
		mutableStateOf(categories[0])
	}

	val context = LocalContext.current

	val calendar = Calendar.getInstance()

	val year = calendar.get(Calendar.YEAR)
	val month = calendar.get(Calendar.MONTH) + 1
	val day = calendar.get(Calendar.DAY_OF_MONTH)

	var date by remember {
		mutableStateOf("${calendar.get(Calendar.DAY_OF_MONTH)}-${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.YEAR)}")
	}

	val datePicker = DatePickerDialog(
		context,
		{ _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
			date = "${selectedDay}-${selectedMonth + 1}-${selectedYear}"
		},
		year,
		month,
		day
	)

	datePicker.datePicker.maxDate = calendar.timeInMillis

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
			Column(
				modifier = Modifier.padding(innerPadding),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Column(modifier = Modifier
					.padding(16.dp)
					.clip(RoundedCornerShape(10.dp))
					.background(BackgroundElevated)
					.fillMaxWidth()
				) {
					TableRow("Amount"){
						UnstyledTextField(
							value = "0",
							onValueChange = {},
							modifier = Modifier.fillMaxWidth(),
							textStyle = TextStyle(
								textAlign = TextAlign.End
							),
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
							modifier = Modifier.widthIn(min = 1.dp)
						) {
							Text(text = selectedRecurrence)
							DropdownMenu(
								expanded = recurrenceMenuOpened,
								onDismissRequest = { recurrenceMenuOpened = false },
								modifier = Modifier
							) {
								recurrences.forEach { recurrence ->
									DropdownMenuItem(
										text = { Text(text = recurrence) },
										onClick = {
											selectedRecurrence = recurrence
											recurrenceMenuOpened = false
										}
									)
								}
							}
						}
					}

					Divider(thickness = 1.dp, color = DividerColor)

					TableRow("Date") {
						TextButton(
							onClick = { datePicker.show() },
							contentPadding = PaddingValues(),
							modifier = Modifier.widthIn(min = 1.dp)
						) {
							Text(text = date)
						}
					}

					Divider(thickness = 1.dp, color = DividerColor)

					TableRow("Note") {
						UnstyledTextField(
							value = "Leave a note",
							onValueChange = {},
							modifier = Modifier.fillMaxWidth(),
							textStyle = TextStyle(
								textAlign = TextAlign.End
							),
							keyboardOptions = KeyboardOptions(
								keyboardType = KeyboardType.Number
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
							Text(text = selectedCategory)
							DropdownMenu(
								expanded = categoriesMenuOpened,
								onDismissRequest = { categoriesMenuOpened = false },
								modifier = Modifier
							) {
								categories.forEach { category ->
									DropdownMenuItem(
										text = {
											Row(verticalAlignment = Alignment.CenterVertically) {
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
											selectedCategory = category
											categoriesMenuOpened = false
										}
									)
								}
							}
						}
					}
				}

				Button(
					onClick = {},
					modifier = Modifier
						.padding(16.dp)
						.width(200.dp),
					shape = RoundedCornerShape(10.dp)
				) {
					Text(text = "Add")
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