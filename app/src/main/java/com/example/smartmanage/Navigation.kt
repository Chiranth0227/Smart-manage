package com.example.smartmanage

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.smartmanage.data.AppDatabase

@Composable
fun AppNavigation(
    darkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)

    AppNavGraph(
        navController = navController,
        database = database,
        darkModeEnabled = darkModeEnabled,
        onDarkModeChange = onDarkModeChange
    )
}
