package com.example.moneymanager.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneymanager.components.TableRow
import com.example.moneymanager.ui.theme.BackgroundElevated
import com.example.moneymanager.ui.theme.DividerColor
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.viewmodels.CategoriesViewModel
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Categories(
	navController: NavController,
	categoriesViewModel: CategoriesViewModel = viewModel()
) {
	val uiState by categoriesViewModel.uiState.collectAsState()

	val colorPickerController = rememberColorPickerController()

	Scaffold(
		topBar = {
			MediumTopAppBar(
				title = { Text(text = "Categories") },
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
								Icons.Rounded.KeyboardArrowLeft,
								contentDescription = "Menu"
							)
							Text(text = "Menu", textAlign = TextAlign.Center)
						}
					}
				}
			)
		},
		content = { innerPadding ->
			Column(modifier = Modifier.padding(innerPadding)) {
				Column(
					modifier = Modifier
						.padding(16.dp)
						.clip(RoundedCornerShape(8.dp))
						.background(BackgroundElevated)
						.fillMaxWidth()
				) {
					TableRow("Groceries")
					Divider(thickness = 1.dp, color = DividerColor)
					TableRow("Food")
					Divider(thickness = 1.dp, color = DividerColor)
					TableRow("Stationary")
					Divider(thickness = 1.dp, color = DividerColor)
					TableRow("Cosmetics")
					Divider(thickness = 1.dp, color = DividerColor)
					TableRow("Furniture")
					Divider(thickness = 1.dp, color = DividerColor)
					TableRow("Electronics")
				}
				Row(modifier = Modifier.padding(horizontal = 16.dp)) {
					if (uiState.colorPickerOpened) {
						Dialog(onDismissRequest = categoriesViewModel::hideColorPicker) {
							Surface(
								color = BackgroundElevated,
								shape = RoundedCornerShape(10.dp)
							) {
								Column(
									modifier = Modifier
										.padding(all = 30.dp)
								) {
									Text(text = "Choose a color", style = Typography.titleLarge)
									Row(
										modifier = Modifier
											.fillMaxWidth()
											.padding(top = 24.dp),
										horizontalArrangement = Arrangement.Center,
										verticalAlignment = Alignment.CenterVertically
									) {
										AlphaTile(
											modifier = Modifier
												.fillMaxWidth()
												.height(60.dp)
												.clip(RoundedCornerShape(6.dp)),
											controller = colorPickerController
										)
									}
									HsvColorPicker(
										modifier = Modifier
											.fillMaxWidth()
											.height(300.dp)
											.padding(10.dp),
										controller = colorPickerController,
										onColorChanged = {}
									)
									BrightnessSlider(
										modifier = Modifier
											.fillMaxWidth()
											.padding(10.dp)
											.height(35.dp),
										controller = colorPickerController,
									)
									TextButton(
										onClick = categoriesViewModel::hideColorPicker,
										modifier = Modifier
											.fillMaxWidth()
											.padding(top = 24.dp)
									) {
										Text(text = "Done")
									}
								}
							}
						}
					}
					Surface(
						onClick = categoriesViewModel::showColorPicker,
						shape = CircleShape,
						color = uiState.newCategoryColor,
						modifier = Modifier.size(24.dp)
					) {}
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