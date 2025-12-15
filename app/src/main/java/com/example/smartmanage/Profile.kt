package com.example.smartmanage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    onLogout: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onToggleDarkMode: (Boolean) -> Unit = {},
    darkModeEnabled: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ---------- Title ----------
        Text(
            text = "My Profile",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // ---------- Profile Picture (Static for now, can update later) ----------
        Box(contentAlignment = Alignment.BottomEnd) {

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
            )

            // Upload Button
            IconButton(
                onClick = {
                    // Implement actual photo picker later
                },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Upload",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // ---------- User Info Card ----------
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                DetailItem("Name", "Chiranth C")
                DetailItem("Email", "chiranth@example.com")
                DetailItem("Member Since", "January 2024")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ---------- Settings ----------
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(5.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                ProfileActionItem(
                    icon = Icons.Default.Lock,
                    label = "Change Password",
                    onClick = onChangePassword
                )

                ProfileActionItemSwitch(
                    icon = Icons.Default.DarkMode,
                    label = "Dark Mode",
                    checked = darkModeEnabled,
                    onCheckedChange = onToggleDarkMode
                )

                ProfileActionItem(
                    icon = Icons.Default.Logout,
                    label = "Logout",
                    textColor = Color.Red,
                    onClick = onLogout
                )
            }
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ProfileActionItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    textColor: Color = Color.Unspecified,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ProfileActionItemSwitch(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))

        Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))

        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
