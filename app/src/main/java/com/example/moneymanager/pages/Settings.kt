package com.example.moneymanager.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.moneymanager.components.TableRow
import com.example.moneymanager.ui.theme.BackgroundElevated
import com.example.moneymanager.ui.theme.DividerColor
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
	navController: NavController,
	settingsViewModel: SettingsViewModel = hiltViewModel()
) {

	Scaffold(
		topBar = {
			MediumTopAppBar(
				title = { Text(text = "Settings", style = Typography.titleMedium) },
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
		}
	) { innerPadding ->
		Column(modifier = Modifier.padding(innerPadding)) {
			Column(
				modifier = Modifier
					.padding(horizontal = 8.dp, vertical = 16.dp)
					.clip(RoundedCornerShape(8.dp))
					.background(BackgroundElevated)
					.fillMaxWidth()
			) {
				TableRow(label = "Erase all Data", isDestructive = true)
				Row(
					modifier = Modifier
						.background(BackgroundElevated)
						.height(1.dp)
				) {
					HorizontalDivider(thickness = 1.dp, color = DividerColor)
				}
				TableRow(
					label = "Log out",
					isDestructive = true,
					modifier = Modifier.clickable {
						settingsViewModel.logout()
						navController.navigate("signin")
					}
				)
			}
		}
	}
}