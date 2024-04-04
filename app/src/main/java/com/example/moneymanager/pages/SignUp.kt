package com.example.moneymanager.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.moneymanager.R
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.Secondary
import com.example.moneymanager.ui.theme.Surface
import com.example.moneymanager.ui.theme.Typography
import com.example.moneymanager.ui.theme.Watermark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp(navController: NavController) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = {  },
				colors = TopAppBarDefaults.topAppBarColors(
					containerColor = Surface
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
							Text(text = "Back", textAlign = TextAlign.Center)
						}
					}
				}
			)
		},
		content = { innerPadding ->
			Column(
				modifier = Modifier
					.padding(innerPadding)
					.fillMaxSize(),
				verticalArrangement = Arrangement.Center,
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = "Create an Account",
					style = Typography.titleLarge,
					modifier = Modifier.padding(bottom = 48.dp)
				)
				OutlinedTextField(
					value = "Full Name",
					onValueChange = {},
					modifier = Modifier
						.padding(horizontal = 16.dp, vertical = 8.dp)
						.fillMaxWidth(),
					shape = RoundedCornerShape(10.dp)
				)
				OutlinedTextField(
					value = "Username",
					onValueChange = {},
					modifier = Modifier
						.padding(horizontal = 16.dp, vertical = 8.dp)
						.fillMaxWidth(),
					shape = RoundedCornerShape(10.dp)
				)
				OutlinedTextField(
					value = "Email",
					onValueChange = {},
					modifier = Modifier
						.padding(horizontal = 16.dp, vertical = 8.dp)
						.fillMaxWidth(),
					shape = RoundedCornerShape(10.dp)
				)
				OutlinedTextField(
					value = "Password",
					onValueChange = {},
					modifier = Modifier
						.padding(horizontal = 16.dp, vertical = 8.dp)
						.fillMaxWidth(),
					shape = RoundedCornerShape(10.dp),
					trailingIcon = {
						Icon(
							painterResource(
							id = R.drawable.icon_visibility_off),
							contentDescription = "Password visibility off"
						)
					}
				)
				Button(
					onClick = {},
					modifier = Modifier
						.padding(horizontal = 16.dp)
						.padding(top = 48.dp, bottom = 32.dp)
						.fillMaxWidth()
						.height(50.dp),
					shape = RoundedCornerShape(10.dp),
					colors = ButtonColors(
						containerColor = Secondary,
						contentColor = Color.Black,
						disabledContainerColor = Secondary,
						disabledContentColor = Color.Black
					)
				) {
					Text(text = "Register")
				}
				Row {
					Text(text = "Already have an account? ")
					Text(
						text = "Sign In",
						style = LocalTextStyle.current.copy(fontWeight = FontWeight.SemiBold),
						modifier = Modifier.clickable { navController.navigate("signin") }
					)
				}
				Text(
					text = "Money Manager",
					style = Typography.headlineSmall.copy(
						fontWeight = FontWeight.Bold,
						color = Watermark
					),
					modifier = Modifier.padding(top = 40.dp)
				)
			}
		}
	)
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUp() {
	MoneyManagerTheme {
		SignUp(navController = rememberNavController())
	}
}