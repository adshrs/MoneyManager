package com.example.moneymanager

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moneymanager.pages.Expenses
import com.example.moneymanager.pages.Settings
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.TopAppBarBackground

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		enableEdgeToEdge(
			statusBarStyle = SystemBarStyle.light(
				Color.TRANSPARENT, Color.TRANSPARENT
			),
			navigationBarStyle = SystemBarStyle.light(
				Color.TRANSPARENT, Color.TRANSPARENT
			)
		)
		super.onCreate(savedInstanceState)
		setContent {
			MoneyManagerTheme {
				val navController = rememberNavController()
				val backStackEntry = navController.currentBackStackEntryAsState()

				Scaffold(
					bottomBar = {
						NavigationBar(containerColor = TopAppBarBackground) {
							NavigationBarItem(
								selected = backStackEntry.value?.destination?.route == "expenses",
								onClick = { navController.navigate("expenses") },
								label = {
									Text("Expenses")
								},
								icon = {
									Icon(
										painterResource(id = R.drawable.icon_navbar_expenses),
										contentDescription = "Expenses"
									)
								}
							)
							NavigationBarItem(
								selected = backStackEntry.value?.destination?.route == "insights",
								onClick = { navController.navigate("insights") },
								label = {
									Text("Insights")
								},
								icon = {
									Icon(
										painterResource(id = R.drawable.icon_navbar_insights),
										contentDescription = "Insights"
									)
								}
							)
							NavigationBarItem(
								selected = backStackEntry.value?.destination?.route == "add",
								onClick = { navController.navigate("add") },
								label = {
									Text("Add")
								},
								icon = {
									Icon(
										painterResource(id = R.drawable.icon_navbar_add),
										contentDescription = "Add"
									)
								}
							)
							NavigationBarItem(
								selected = backStackEntry.value?.destination?.route?.startsWith("settings") ?: false,
								onClick = { navController.navigate("settings") },
								label = {
									Text("Settings")
								},
								icon = {
									Icon(
										painterResource(id = R.drawable.icon_navbar_settings),
										contentDescription = "Settings"
									)
								}
							)
						}
					},
					content = { innerPadding ->
						NavHost(navController = navController, startDestination = "expenses") {
							composable("expenses") {
								Surface(
									modifier = Modifier
										.fillMaxSize()
										.padding(innerPadding)
								) {
									Expenses(navController = navController)
								}
							}
							composable("insights") {
								Surface(
									modifier = Modifier
										.fillMaxSize()
										.padding(innerPadding)
								) {
									Greeting("Insights")
								}
							}
							composable("add") {
								Surface(
									modifier = Modifier
										.fillMaxSize()
										.padding(innerPadding)
								) {
									Greeting("Add")
								}
							}
							composable("settings") {
								Surface(
									modifier = Modifier
										.fillMaxSize()
										.padding(innerPadding)
								) {
									Settings(navController = navController)
								}
							}
							composable("settings/categories") {
								Surface(
									modifier = Modifier
										.fillMaxSize()
										.padding(innerPadding)
								) {
									Greeting("Categories")
								}
							}
						}
					}
				)
			}
		}
	}
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
	Text(
		text = "Hello $name!",
		modifier = modifier
	)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	MoneyManagerTheme {
		Surface {
			Greeting("Android")
		}
	}
}