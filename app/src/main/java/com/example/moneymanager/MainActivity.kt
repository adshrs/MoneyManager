package com.example.moneymanager

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moneymanager.components.KeyboardAware
import com.example.moneymanager.pages.Add
import com.example.moneymanager.pages.Analytics
import com.example.moneymanager.pages.Categories
import com.example.moneymanager.pages.Expenses
import com.example.moneymanager.pages.Menu
import com.example.moneymanager.pages.Settings
import com.example.moneymanager.ui.theme.MoneyManagerTheme
import com.example.moneymanager.ui.theme.TopAppBarBackground


class MainActivity : ComponentActivity() {
	@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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

				showBottomBar = when (backStackEntry?.destination?.route) {
					"menu/categories" -> false
					else -> true
				}

				KeyboardAware {
					Scaffold(
						bottomBar = {
							if (showBottomBar) {
								NavigationBar(
									containerColor = TopAppBarBackground,
									modifier = Modifier.height(70.dp)
								) {
									NavigationBarItem(
										selected = backStackEntry?.destination?.route?.startsWith("home/expenses")
											?: false,
										onClick = { navController.navigate("home/expenses") },
										icon = {
											if (backStackEntry?.destination?.route == "home/expenses") {
												Icon(
													painterResource(id = R.drawable.icon_navbar_home_selected),
													contentDescription = "Expenses"
												)
											} else {
												Icon(
													painterResource(id = R.drawable.icon_navbar_home),
													contentDescription = "Expenses"
												)
											}
										},
										colors = NavigationBarItemDefaults.colors(
											indicatorColor = androidx.compose.ui.graphics.Color.Transparent,
											selectedIconColor = androidx.compose.ui.graphics.Color.White,
											unselectedIconColor = androidx.compose.ui.graphics.Color.White
										)
									)
									NavigationBarItem(
										selected = backStackEntry?.destination?.route?.startsWith("analytics")
											?: false,
										onClick = { navController.navigate("analytics") },
										icon = {
											if (backStackEntry?.destination?.route == "analytics") {
												Icon(
													painterResource(id = R.drawable.icon_navbar_analytics_selected),
													contentDescription = "analytics"
												)
											} else {
												Icon(
													painterResource(id = R.drawable.icon_navbar_analytics),
													contentDescription = "analytics"
												)
											}
										},
										colors = NavigationBarItemDefaults.colors(
											indicatorColor = androidx.compose.ui.graphics.Color.Transparent,
											selectedIconColor = androidx.compose.ui.graphics.Color.White,
											unselectedIconColor = androidx.compose.ui.graphics.Color.White
										)
									)
									NavigationBarItem(
										selected = backStackEntry?.destination?.route?.startsWith("add")
											?: false,
										onClick = { navController.navigate("add") },
										icon = {
											if (backStackEntry?.destination?.route == "add") {
												Icon(
													painterResource(id = R.drawable.icon_navbar_add_selected),
													contentDescription = "Add"
												)
											} else {
												Icon(
													painterResource(id = R.drawable.icon_navbar_add),
													contentDescription = "Add"
												)
											}
										},
										colors = NavigationBarItemDefaults.colors(
											indicatorColor = androidx.compose.ui.graphics.Color.Transparent,
											selectedIconColor = androidx.compose.ui.graphics.Color.White,
											unselectedIconColor = androidx.compose.ui.graphics.Color.White
										)
									)
									NavigationBarItem(
										selected = backStackEntry?.destination?.route?.startsWith("budgets&goals")
											?: false,
										onClick = { navController.navigate("budgets&goals") },
										icon = {
											if (backStackEntry?.destination?.route == "budgets&goals") {
												Icon(
													painterResource(id = R.drawable.icon_navbar_budgetsandgoals_selected),
													contentDescription = "Settings"
												)
											} else {
												Icon(
													painterResource(id = R.drawable.icon_navbar_budgetsandgoals),
													contentDescription = "Settings"
												)
											}
										},
										colors = NavigationBarItemDefaults.colors(
											indicatorColor = androidx.compose.ui.graphics.Color.Transparent,
											selectedIconColor = androidx.compose.ui.graphics.Color.White,
											unselectedIconColor = androidx.compose.ui.graphics.Color.White
										)
									)
									NavigationBarItem(
										selected = backStackEntry?.destination?.route?.startsWith("menu")
											?: false,
										onClick = { navController.navigate("menu") },
										icon = {
											if (backStackEntry?.destination?.route == "menu") {
												Icon(
													painterResource(id = R.drawable.icon_navbar_menu_selected),
													contentDescription = "Menu"
												)
											} else {
												Icon(
													painterResource(id = R.drawable.icon_navbar_menu),
													contentDescription = "Menu"
												)
											}
										},
										colors = NavigationBarItemDefaults.colors(
											indicatorColor = androidx.compose.ui.graphics.Color.Transparent,
											selectedIconColor = androidx.compose.ui.graphics.Color.White,
											unselectedIconColor = androidx.compose.ui.graphics.Color.White
										)
									)
								}
							}
						},
						content = { _ ->
							NavHost(navController = navController, startDestination = "home/expenses") {
								composable("home/expenses") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(bottom = 55.dp)
									) {
										Expenses(navController = navController)
									}
								}
								composable("home/incomes") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(bottom = 55.dp)
									) {
										Greeting("Incomes")
									}
								}
								composable("analytics") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(bottom = 55.dp)
									) {
										Analytics(navController = navController)
									}
								}
								composable("add") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(bottom = 55.dp)
									) {
										Add(navController = navController)
									}
								}
								composable("budgets&goals") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(bottom = 55.dp)
									) {
										Greeting("Budgets & Goals")
									}
								}
								composable("budgets&goals/budgets") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(bottom = 55.dp)
									) {
										Greeting("Budgets")
									}
								}
								composable("budgets&goals/goals") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(bottom = 55.dp)
									) {
										Greeting("Goals")
									}
								}
								composable("menu") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(bottom = 55.dp)
									) {
										Menu(navController = navController)
									}
								}
								composable("menu/account") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(bottom = 55.dp)
									) {
										Greeting("Account")
									}
								}
								composable("menu/categories") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(bottom = 0.dp)
									) {
										Categories(navController = navController)
									}
								}
								composable("menu/settings") {
									Surface(
										modifier = Modifier
											.fillMaxSize()
											.padding(bottom = 55.dp)
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