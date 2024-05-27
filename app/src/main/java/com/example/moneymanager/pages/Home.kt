package com.example.moneymanager.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonColors
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moneymanager.R
import com.example.moneymanager.ui.theme.TopAppBarBackground
import com.example.moneymanager.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navController: NavController) {

	val options = listOf<String>("Expense", "Income")
	var selectedIndex by remember {
		mutableIntStateOf(0)
	}

	Scaffold(
		topBar = {
			TopAppBar(
				title = {
					Column(
						modifier = Modifier
							.padding(end = 16.dp),
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						SingleChoiceSegmentedButtonRow(
							modifier = Modifier.fillMaxWidth()
						) {
							options.forEachIndexed { index, option ->
								SegmentedButton(
									selected = selectedIndex == index,
									onClick = {
										selectedIndex = index
									},
									icon = { SegmentedButtonDefaults.Icon(active = false) },
									colors = SegmentedButtonColors(
										activeContainerColor = Color.White.copy(alpha = 0.9f),
										activeContentColor = Color.Black,
										activeBorderColor = Color.White,
										inactiveContainerColor = Color.Black,
										inactiveContentColor = Color.White,
										inactiveBorderColor = Color.White,
										disabledActiveContainerColor = Color.White,
										disabledActiveContentColor = Color.White,
										disabledActiveBorderColor = Color.White,
										disabledInactiveContainerColor = Color.Black,
										disabledInactiveContentColor = Color.White,
										disabledInactiveBorderColor = Color.White
									),
									shape = SegmentedButtonDefaults.itemShape(
										index = index,
										count = options.size,
										baseShape = if (index == 0) SegmentedButtonDefaults.baseShape.copy(
											topStart = CornerSize(20.dp),
											bottomStart = CornerSize(20.dp)
										)
										else SegmentedButtonDefaults.baseShape.copy(
											topEnd = CornerSize(20.dp),
											bottomEnd = CornerSize(20.dp)
										)
									),
									border = SegmentedButtonDefaults.borderStroke(
										color = Color.Black,
										width = 0.dp
									),
									modifier = Modifier.height(40.dp)
								) {
									Row(verticalAlignment = Alignment.CenterVertically) {
										if (option == options[0]) {
											Icon(
												painterResource(id = R.drawable.icon_expense),
												contentDescription = "expense"
											)
										} else {
											Icon(
												painterResource(id = R.drawable.icon_income),
												contentDescription = "income"
											)
										}
										Spacer(modifier = Modifier.width(8.dp))
										Text(text = option, style = Typography.headlineLarge)
									}
								}
							}
						}
					}
				},
				colors = TopAppBarDefaults.mediumTopAppBarColors(
					containerColor = TopAppBarBackground
				),
				modifier = Modifier.clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
			)
		},
		content = { innerPadding ->
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(innerPadding)
			) {
				if (selectedIndex == 0) {
					Expenses(navController = navController)
				} else {
					Incomes(navController = navController)
				}
			}
		}
	)
}