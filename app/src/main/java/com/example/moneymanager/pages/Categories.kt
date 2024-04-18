package com.example.moneymanager.pages

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.Primary
import com.example.moneymanager.ui.theme.TextSecondary
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.utils.parseColorString
import com.example.moneymanager.viewmodels.CategoriesViewModel
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
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

	val colorPickerController = rememberColorPickerController()
	val keyboardController = LocalSoftwareKeyboardController.current

	LaunchedEffect(categoriesViewModel, context) {
		categoriesViewModel.categoryNetworkResults.collect { result ->
			when (result) {
				is NetworkResult.Success -> {
					categoriesViewModel.updateCategories(result.data!!)
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
					navController.navigate("menu/categories") {
						popUpTo("menu/categories") {
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
			MediumTopAppBar(
				title = { Text(text = "Categories", style = Typography.titleMedium) },
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
		bottomBar = {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.height(80.dp)
					.background(Color.Transparent),
				verticalAlignment = Alignment.Top,
				horizontalArrangement = Arrangement.Center
			) {
				ExtendedFloatingActionButton(
					onClick = {
						navController.navigate("menu/categories/addcategory")
					},
					modifier = Modifier
						.height(50.dp)
						.border(4.dp, Primary, RoundedCornerShape(100f)),
					shape = RoundedCornerShape(100f),
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
			} else {
				Column(
					modifier = Modifier
						.padding(innerPadding)
						.fillMaxHeight(),
					verticalArrangement = Arrangement.Top
				) {

					AnimatedVisibility(visible = true) {
						LazyColumn(
							modifier = Modifier
								.padding(horizontal = 8.dp, vertical = 16.dp)
								.clip(RoundedCornerShape(8.dp))
								.fillMaxWidth()
						) {
							itemsIndexed(
								uiState.categories,
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
								if (index < uiState.categories.size - 1) {
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
								text = "After deleting this category, all existing expenses that belongs " +
										  "to this category will be tagged with a default category 'expense'.",
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