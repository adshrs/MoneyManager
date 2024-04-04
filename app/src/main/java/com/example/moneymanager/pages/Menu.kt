package com.example.moneymanager.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moneymanager.components.TableRow
import com.example.moneymanager.ui.theme.BackgroundElevated
import com.example.moneymanager.ui.theme.DividerColor
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(navController: NavController) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(text = "Menu", style = Typography.titleMedium) },
				colors = TopAppBarDefaults.mediumTopAppBarColors(
					containerColor = TopAppBarBackground
				)
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
					TableRow(label = "Account", hasArrow = true, modifier = Modifier.clickable {
						navController.navigate("menu/account")
					})
					HorizontalDivider(thickness = 1.dp, color = DividerColor)
					TableRow(label = "Categories", hasArrow = true, modifier = Modifier.clickable {
						navController.navigate("menu/categories")
					})
					HorizontalDivider(thickness = 1.dp, color = DividerColor)
					TableRow(label = "Settings", hasArrow = true, modifier = Modifier.clickable {
						navController.navigate("menu/settings")
					})
				}
			}
		}
	)
}