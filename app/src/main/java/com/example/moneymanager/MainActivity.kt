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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moneymanager.components.KeyboardAware
import com.example.moneymanager.pages.Add
import com.example.moneymanager.pages.Categories
import com.example.moneymanager.pages.Expenses
import com.example.moneymanager.pages.Menu
import com.example.moneymanager.pages.Settings
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.TopAppBarBackground


class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		enableEdgeToEdge(
			statusBarStyle = SystemBarStyle.auto(
				Color.TRANSPARENT, Color.TRANSPARENT
			),
			navigationBarStyle = SystemBarStyle.auto(
				Color.TRANSPARENT, Color.TRANSPARENT
			)
		)
		super.onCreate(savedInstanceState)
		setContent {
			MoneyManagerTheme {
				val navController = rememberNavController()
				val backStackEntry by navController.currentBackStackEntryAsState()
				var showBottomBar by rememberSaveable { mutableStateOf(true) }

				showBottomBar =  when (backStackEntry?.destination?.route) {
					"menu/categories" -> false
					else -> true
				}

				KeyboardAware  {
					Scaffold(
						bottomBar = {
							if (showBottomBar) {
								NavigationBar(containerColor = TopAppBarBackground) {
									NavigationBarItem(
										selected = backStackEntry?.destination?.route == "home/expenses",
										onClick = { navController.navigate("home/expenses") },
										icon = {
											Icon(
												painterResource(id = R.drawable.icon_navbar_home),
												contentDescription = "Expenses"
											)
										}
									)
									NavigationBarItem(
										selected = backStackEntry?.destination?.route == "insights",
										onClick = { navController.navigate("insights") },
										icon = {
											Icon(
												painterResource(id = R.drawable.icon_navbar_insights),
												contentDescription = "Insights"
											)
										}
									)
									NavigationBarItem(
										selected = backStackEntry?.destination?.route == "add",
										onClick = { navController.navigate("add") },
										icon = {
											Icon(
												painterResource(id = R.drawable.icon_navbar_add),
												contentDescription = "Add"
											)
										}
									)
									NavigationBarItem(
										selected = backStackEntry?.destination?.route?.startsWith("budgets&goals")
											?: false,
										onClick = { navController.navigate("budgets&goals") },
										icon = {
											Icon(
												painterResource(id = R.drawable.icon_navbar_budgetsandgoals),
												contentDescription = "Settings"
											)
										}
									)
									NavigationBarItem(
										selected = backStackEntry?.destination?.route?.startsWith("menu")
											?: false,
										onClick = { navController.navigate("menu") },
										icon = {
											Icon(
												painterResource(id = R.drawable.icon_navbar_menu),
												contentDescription = "Menu"
											)
										}
									)
								}
							}
						},
						content = { innerPadding ->
							NavHost(navController = navController, startDestination = "home/expenses") {
								composable("home/expenses") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(innerPadding)
									) {
										Expenses(navController = navController)
									}
								}
								composable("home/incomes") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(innerPadding)
									) {
										Greeting("Incomes")
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
										Add(navController = navController)
									}
								}
								composable("budgets&goals") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(innerPadding)
									) {
										Greeting("Budgets & Goals")
									}
								}
								composable("budgets&goals/budgets") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(innerPadding)
									) {
										Greeting("Budgets")
									}
								}
								composable("budgets&goals/goals") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(innerPadding)
									) {
										Greeting("Goals")
									}
								}
								composable("menu") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(innerPadding)
									) {
										Menu(navController = navController)
									}
								}
								composable("menu/account") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(innerPadding)
									) {
										Greeting("Account")
									}
								}
								composable("menu/categories") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(innerPadding)
									) {
										Categories(navController = navController)
									}
								}
								composable("menu/settings") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(innerPadding)
									) {
										Settings(navController = navController)
									}
								}
							}
						}
					)
				}
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