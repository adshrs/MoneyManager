package com.example.moneymanager.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneymanager.R
import com.example.moneymanager.components.TableRow
import com.example.moneymanager.components.UnstyledTextField
import com.example.moneymanager.ui.theme.BackgroundElevated
import com.example.moneymanager.ui.theme.Destructive
import com.example.moneymanager.ui.theme.DividerColor
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.Primary
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.viewmodels.CategoriesViewModel
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Categories(
	navController: NavController,
	categoriesViewModel: CategoriesViewModel = viewModel()
) {
	val uiState by categoriesViewModel.uiState.collectAsState()

	val colorPickerController = rememberColorPickerController()
	val keyboardController = LocalSoftwareKeyboardController.current

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
								Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
								contentDescription = "Menu"
							)
							Text(text = "Menu", textAlign = TextAlign.Center)
						}
					}
				}
			)
		},
		content = { innerPadding ->
			Column(
				modifier = Modifier
					.padding(innerPadding)
					.fillMaxHeight(),
				verticalArrangement = Arrangement.SpaceBetween
			) {
				Column(modifier = Modifier.weight(1f)) {
					AnimatedVisibility(visible = true) {
						LazyColumn(
							modifier = Modifier
								.padding(horizontal = 8.dp, vertical = 16.dp)
								.clip(RoundedCornerShape(8.dp))
//								.background(BackgroundElevated)
								.fillMaxWidth()
						) {
							itemsIndexed(
								uiState.categories,
								key = { _, category -> category.name }
							) { index, category ->
								SwipeableActionsBox(
									endActions = listOf(
										SwipeAction(
											icon = painterResource(id = R.drawable.icon_delete),
											background = Destructive,
											onSwipe = { categoriesViewModel.deleteCategory(category) }
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
												color = category.color,
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
								if (index < uiState.categories.size - 1) {
									Row(modifier = Modifier
										.background(BackgroundElevated)
										.height(1.dp)) {
										HorizontalDivider(thickness = 1.dp, color = DividerColor)
									}
								}
							}
						}
					}
				}
				Row(
					modifier = Modifier
						.padding(horizontal = 8.dp)
						.padding(bottom = 16.dp)
						.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					if (uiState.colorPickerOpened) {
						Dialog(onDismissRequest = categoriesViewModel::hideColorPicker) {
							Surface(
								color = BackgroundElevated,
								shape = RoundedCornerShape(10.dp)
							) {
								Column(
									modifier = Modifier.padding(all = 30.dp),
									horizontalAlignment = Alignment.CenterHorizontally
								) {
									Text(text = "Choose a color", style = Typography.titleMedium)
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
										onColorChanged = {
											categoriesViewModel.setNewCategoryColor(it.color)
										},
										initialColor = Primary
									)
									TextButton(
										onClick = categoriesViewModel::hideColorPicker,
										modifier = Modifier
											.fillMaxWidth()
									) {
										Text(text = "Done")
									}
								}
							}
						}
					}
					Surface(
						onClick = categoriesViewModel::showColorPicker,
						shape = RoundedCornerShape(6.dp),
						color = uiState.newCategoryColor,
						border = BorderStroke(
							width = 2.dp,
							color = Color.White
						),
						modifier = Modifier.size(24.dp)
					) {}
					Surface(
						color = BackgroundElevated,
						modifier = Modifier
							.height(44.dp)
							.weight(1f)
							.padding(start = 16.dp),
						shape = RoundedCornerShape(10.dp)
					) {
						Column(
							verticalArrangement = Arrangement.Center,
							modifier = Modifier
								.fillMaxSize()
								.padding(start = 16.dp)
						) {
							UnstyledTextField(
								value = uiState.newCategoryName,
								onValueChange = categoriesViewModel::setNewCategoryName,
								placeholder = { Text(text = "Enter new category name") },
								modifier = Modifier.fillMaxWidth(),
								singleLine = true
							)
						}
					}
					IconButton(
						onClick = {
							categoriesViewModel.createNewCategory()
							keyboardController?.hide()
						},
						modifier = Modifier.padding(start = 16.dp)
					) {
						Icon(
							Icons.AutoMirrored.Rounded.Send,
							contentDescription = "Create new category"
						)
					}
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