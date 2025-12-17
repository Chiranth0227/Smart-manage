package com.example.smartmanage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartmanage.data.AppDatabase
import com.example.smartmanage.data.Template
import com.example.smartmanage.data.Transaction
import com.example.smartmanage.ui.screens.AnalyticsScreen
import com.example.smartmanage.ui.screens.BudgetScreen
import com.example.smartmanage.ui.screens.DashboardScreen
import com.example.smartmanage.ui.screens.SettingsScreen
import com.example.smartmanage.ui.screens.TemplatesScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph(
    navController: NavHostController,
    database: AppDatabase,
    darkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // ---------------- LOGIN ----------------
        composable("login") {
            LoginScreen(
                onLogin = {
                    navController.navigate("dashboard") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onDemoLogin = {
                    scope.launch {
                        val dao = database.transactionDao()
                        dao.insert(Transaction(amount = 5000.0, type = "income", note = "Salary"))
                        dao.insert(Transaction(amount = 1500.0, type = "expense", note = "Rent"))
                        dao.insert(Transaction(amount = 300.0, type = "expense", note = "Groceries"))
                        dao.insert(Transaction(amount = 100.0, type = "expense", note = "Transport"))
                        dao.insert(Transaction(amount = 1200.0, type = "income", note = "Freelance Project"))
                        dao.insert(Transaction(amount = 450.0, type = "expense", note = "Dinner & Movies"))
                        dao.insert(Transaction(amount = 80.0, type = "expense", note = "Coffee"))
                        
                        // Seed some templates
                        dao.insertTemplate(Template(name = "Monthly Salary", amount = 5000.0, type = "INCOME", category = "Salary", isRecurring = true, frequency = "monthly"))
                        dao.insertTemplate(Template(name = "Rent Payment", amount = 1200.0, type = "EXPENSE", category = "Housing", isRecurring = true, frequency = "monthly"))
                        dao.insertTemplate(Template(name = "Personal Finance", amount = 1000.0, type = "EXPENSE", category = "Personal", isRecurring = true, frequency = "monthly"))
                    }
                    navController.navigate("dashboard") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // ---------------- DASHBOARD ----------------
        composable("dashboard") {
            DashboardScreen(
                navController = navController,
                database = database
            )
        }

        // ---------------- TRANSACTIONS ----------------
        composable("transactions") {
            TransactionsScreen(
                navController = navController,
                database = database
            )
        }

        // ---------------- ANALYTICS ----------------
        composable("analytics") {
            AnalyticsScreen(database = database)
        }

        // ---------------- TEMPLATES ----------------
        composable("templates") {
            TemplatesScreen(database = database)
        }

        // ---------------- BUDGET ----------------
        composable("budget") {
            BudgetScreen(database = database)
        }

        // ---------------- ADD INCOME ----------------
        composable("add_income") {
            AddIncomeScreen(onSaveIncome = { amount, note ->
                scope.launch {
                    database.transactionDao().insert(Transaction(amount = amount, type = "income", note = note))
                }
                navController.popBackStack()
            })
        }

        // ---------------- ADD EXPENSE ----------------
        composable("add_expense") {
            AddExpenseScreen(onSaveExpense = { amount, note ->
                scope.launch {
                    database.transactionDao().insert(Transaction(amount = amount, type = "expense", note = note))
                }
                navController.popBackStack()
            })
        }

        // ---------------- PROFILE ----------------
        composable("profile") {
            ProfileScreen(
                darkModeEnabled = darkModeEnabled,
                onToggleDarkMode = onDarkModeChange,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // ---------------- SETTINGS ----------------
        composable("settings") {
            SettingsScreen(
                useDarkTheme = darkModeEnabled,
                onDarkModeChange = onDarkModeChange,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
