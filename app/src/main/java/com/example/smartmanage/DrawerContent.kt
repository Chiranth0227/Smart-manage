package com.example.smartmanage

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun DrawerContent(navController: NavHostController, closeDrawer: () -> Unit) {
    var selectedItem by remember { mutableStateOf<String?>(null) }

    Spacer(modifier = Modifier.height(12.dp))
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
        label = { Text("Profile") },
        selected = selectedItem == "profile",
        onClick = {
            selectedItem = "profile"
            navController.navigate("profile")
            closeDrawer()
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
        label = { Text("Settings") },
        selected = selectedItem == "settings",
        onClick = {
            selectedItem = "settings"
            navController.navigate("settings")
            closeDrawer()
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
    NavigationDrawerItem(
        icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
        label = { Text("Logout") },
        selected = selectedItem == "logout",
        onClick = {
            selectedItem = "logout"
            navController.navigate("login") {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                launchSingleTop = true
            }
            closeDrawer()
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}
