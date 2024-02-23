package com.example.moneymanager.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(navController: NavController) {
	Scaffold(
		topBar = {
			MediumTopAppBar(
				title = { Text(text = "Settings") },
				colors = TopAppBarDefaults.mediumTopAppBarColors(
					containerColor = TopAppBarBackground
				)
			)
		},
		content = { innerPadding ->
			Column(modifier = Modifier.padding(innerPadding)) {
				Column(modifier = Modifier
					.padding(16.dp)
					.clip(RoundedCornerShape(8.dp))
					.background(BackgroundElevated)
					.fillMaxWidth()
				) {
					TableRow("Categories", hasArrow = true, onClick = {
						navController.navigate("settings/categories")
					})
					Divider(thickness = 1.dp, color = DividerColor)
					TableRow("Erase all Data", isDestructive = true, onClick = {})
				}
			}
		}
	)
}