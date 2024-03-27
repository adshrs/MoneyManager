package com.example.moneymanager.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.navigation.NavController
import com.example.moneymanager.components.TableRow
import com.example.moneymanager.ui.theme.BackgroundElevated
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(navController: NavController) {
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
		},
		content = { innerPadding ->
			Column(modifier = Modifier.padding(innerPadding)) {
				Column(modifier = Modifier
					.padding(horizontal = 8.dp, vertical = 16.dp)
					.clip(RoundedCornerShape(8.dp))
					.background(BackgroundElevated)
					.fillMaxWidth()
				) {
					TableRow(label = "Erase all Data", isDestructive = true)
				}
			}
		}
	)
}