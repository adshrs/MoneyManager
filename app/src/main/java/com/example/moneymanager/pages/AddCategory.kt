package com.example.moneymanager.pages

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumTopAppBar
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneymanager.components.textFields.UnstyledTextField
import com.example.moneymanager.models.category.CategoryRequest
import com.example.moneymanager.ui.theme.BackgroundElevated
import com.example.moneymanager.ui.theme.DisabledButton
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.Primary
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.utils.NetworkResult
import com.example.moneymanager.viewmodels.AddCategoryViewModel
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategory(
	navController: NavController,
	addCategoryViewModel: AddCategoryViewModel = hiltViewModel()
) {

	val uiState by addCategoryViewModel.uiState.collectAsState()
	val context = LocalContext.current

	val colorPickerController = rememberColorPickerController()
	val keyboardController = LocalSoftwareKeyboardController.current

	LaunchedEffect(addCategoryViewModel, context) {
		addCategoryViewModel.statusNetworkResults.collect { result ->

			when (result) {
				is NetworkResult.Success -> {
					Toast.makeText(context, result.data, Toast.LENGTH_SHORT)
						.show()
					navController.navigate("menu/categories") {
						popUpTo("menu/categories") {
							inclusive = true
						}
					}
				}

				is NetworkResult.Error -> {
					if (result.message!!.startsWith("JWT expired")) {
						addCategoryViewModel.removeToken()
						navController.navigate("signin")
						Toast.makeText(context, "Session Expired, sign in again", Toast.LENGTH_SHORT)
							.show()
					}
					else if (result.message.startsWith("Invalid compact JWT string")) {
						navController.navigate("signin")
					}
					else {
						Toast.makeText(context, result.message.toString(), Toast.LENGTH_SHORT)
							.show()
					}
				}

				is NetworkResult.Loading -> {}
			}
		}
	}

	Scaffold(
		topBar = {
			MediumTopAppBar(
				title = { Text(text = "Add Category", style = Typography.titleMedium) },
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
								contentDescription = "Back"
							)
							Text(text = "Categories", textAlign = TextAlign.Center)
						}
					}
				}
			)
		},
		content = { innerPadding ->
			Column(
				modifier = Modifier
					.padding(innerPadding)
					.padding(horizontal = 8.dp, vertical = 16.dp)
					.fillMaxHeight(),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Row(
					modifier = Modifier
						.padding(bottom = 16.dp)
						.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					Button(
						onClick = {
							keyboardController?.hide()
							addCategoryViewModel.createCategory(
								CategoryRequest(
									uiState.newCategoryName,
									uiState.newCategoryColor.toString(),
									uiState.userId
								)
							)
						},
						modifier = Modifier
							.fillMaxWidth()
							.height(50.dp)
							.border(5.dp, uiState.newCategoryColor, RoundedCornerShape(100f)),
						shape = RoundedCornerShape(100f),
						colors = ButtonColors(
							containerColor = com.example.moneymanager.ui.theme.Button,
							contentColor = Color.Black,
							disabledContainerColor = DisabledButton,
							disabledContentColor = Color.Black
						)
					) {
						Text(text = "Create")
					}
				}

				Row(
					modifier = Modifier
						.padding(bottom = 16.dp)
						.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {

					Surface(
						shape = RoundedCornerShape(6.dp),
						color = uiState.newCategoryColor,
						border = BorderStroke(
							width = 2.dp,
							color = Color.White
						),
						modifier = Modifier.size(40.dp)
					) {}

					Surface(
						color = BackgroundElevated,
						modifier = Modifier
							.height(44.dp)
							.weight(1f)
							.padding(start = 16.dp)
							.border(2.dp, uiState.newCategoryColor, RoundedCornerShape(10.dp)),
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
								onValueChange = addCategoryViewModel::setNewCategoryName,
								placeholder = { Text(text = "Enter new category name") },
								modifier = Modifier
									.fillMaxWidth(),
								singleLine = true
							)
						}
					}
				}
				Row(
					modifier = Modifier
						.padding(bottom = 16.dp)
						.fillMaxWidth(),
					verticalAlignment = Alignment.CenterVertically
				) {
					Surface(
						color = BackgroundElevated,
						shape = RoundedCornerShape(10.dp)
					) {
						Column(
							modifier = Modifier.padding(all = 30.dp),
							horizontalAlignment = Alignment.CenterHorizontally
						) {
							Row(
								modifier = Modifier
									.fillMaxWidth(),
								horizontalArrangement = Arrangement.Center,
								verticalAlignment = Alignment.CenterVertically
							) {
							}
							HsvColorPicker(
								modifier = Modifier
									.fillMaxWidth()
									.height(300.dp)
									.padding(10.dp),
								controller = colorPickerController,
								onColorChanged = {
									addCategoryViewModel.setNewCategoryColor(it.color)
								},
								initialColor = Primary
							)
						}
					}
				}
			}
		}
	)
}

@Preview
@Composable
fun AddCategoryPreview() {
	MoneyManagerTheme {
		AddCategory(navController = rememberNavController())
	}
}