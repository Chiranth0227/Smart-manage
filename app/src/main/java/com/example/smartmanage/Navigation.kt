package com.example.smartmanage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.smartmanage.data.AppDatabase

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    var darkModeEnabled by remember { mutableStateOf(false) }

    AppNavGraph(
        navController = navController,
        database = database,
        darkModeEnabled = darkModeEnabled,
        onDarkModeChange = { darkModeEnabled = it }
    )
}
