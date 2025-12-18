package com.example.smartmanage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.smartmanage.data.AppDatabase
import com.example.smartmanage.data.Template
import com.example.smartmanage.data.Transaction
import com.example.smartmanage.ui.screens.*
import com.example.smartmanage.ui.screens.LoginScreen
import com.example.smartmanage.ui.screens.SignupScreen
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
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onDemoLogin = {
                    scope.launch {
                        val dao = database.transactionDao()
                        dao.insert(Transaction(amount = 5000.0, type = "income", category = "Salary", note = "Monthly Salary"))
                        dao.insert(Transaction(amount = 1500.0, type = "expense", category = "Housing", note = "Rent"))
                        dao.insert(Transaction(amount = 300.0, type = "expense", category = "Food", note = "Groceries"))
                        dao.insert(Transaction(amount = 100.0, type = "expense", category = "Transport", note = "Transport"))
                        dao.insert(Transaction(amount = 1200.0, type = "income", category = "Freelance", note = "Freelance Project"))
                        dao.insert(Transaction(amount = 450.0, type = "expense", category = "Entertainment", note = "Dinner & Movies"))
                        dao.insert(Transaction(amount = 80.0, type = "expense", category = "Food", note = "Coffee"))

                        // Seed some templates
                        dao.insertTemplate(Template(name = "Monthly Salary", amount = 5000.0, type = "INCOME", category = "Salary", isRecurring = true, frequency = "monthly"))
                        dao.insertTemplate(Template(name = "Rent Payment", amount = 1200.0, type = "EXPENSE", category = "Housing", isRecurring = true, frequency = "monthly"))
                        dao.insertTemplate(Template(name = "Personal Finance", amount = 1000.0, type = "EXPENSE", category = "Personal", isRecurring = true, frequency = "monthly"))
                    }
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                navController = navController
            )
        }

        // ---------------- SIGNUP ----------------
        composable("signup") {
            SignupScreen(
                navController = navController
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
            AnalyticsScreen(
                navController = navController,
                database = database
            )
        }

        // ---------------- TEMPLATES ----------------
        composable("templates") {
            TemplatesScreen(
                navController = navController,
                database = database
            )
        }

        // ---------------- BUDGET ----------------
        composable("budget") {
            BudgetScreen(database = database)
        }

        // ---------------- ADD INCOME ----------------
        composable("add_income") {
            AddIncomeScreen(onSaveIncome = { amount, category, note ->
                scope.launch {
                    database.transactionDao().insert(Transaction(amount = amount, type = "income", category = category, note = note))
                }
                navController.popBackStack()
            })
        }

        // ---------------- ADD EXPENSE ----------------
        composable("add_expense") {
            AddExpenseScreen(
                database = database,
                navController = navController
            )
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
