package com.example.smartmanage.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    useDarkTheme: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    var showAboutDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var biometricEnabled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Manage your preferences",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Profile Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "John Doe",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "john.doe@example.com",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }

                        IconButton(onClick = { /* Edit profile */ }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                        }
                    }
                }
            }

            // General Settings
            item {
                Text(
                    text = "General",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        SettingsSwitchItem(
                            icon = Icons.Default.DarkMode,
                            title = "Dark Mode",
                            description = "Enable dark theme",
                            checked = useDarkTheme,
                            onCheckedChange = onDarkModeChange
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingsSwitchItem(
                            icon = Icons.Default.Notifications,
                            title = "Notifications",
                            description = "Receive transaction reminders",
                            checked = notificationsEnabled,
                            onCheckedChange = { notificationsEnabled = it }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingsItem(
                            icon = Icons.Default.Language,
                            title = "Language",
                            description = "English (US)",
                            onClick = { /* Change language */ }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingsItem(
                            icon = Icons.Default.Palette,
                            title = "Currency",
                            description = "INR (₹)",
                            onClick = { /* Change currency */ }
                        )
                    }
                }
            }

            // Security
            item {
                Text(
                    text = "Security & Privacy",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        SettingsSwitchItem(
                            icon = Icons.Default.Fingerprint,
                            title = "Biometric Lock",
                            description = "Use fingerprint or face ID",
                            checked = biometricEnabled,
                            onCheckedChange = { biometricEnabled = it }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingsItem(
                            icon = Icons.Default.Lock,
                            title = "Change PIN",
                            description = "Update your security PIN",
                            onClick = { /* Change PIN */ }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingsItem(
                            icon = Icons.Default.Security,
                            title = "Privacy Policy",
                            description = "Read our privacy policy",
                            onClick = { /* Open privacy policy */ }
                        )
                    }
                }
            }

            // Data Management
            item {
                Text(
                    text = "Data Management",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        SettingsItem(
                            icon = Icons.Default.CloudUpload,
                            title = "Backup Data",
                            description = "Backup to cloud storage",
                            onClick = { /* Backup */ }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingsItem(
                            icon = Icons.Default.CloudDownload,
                            title = "Restore Data",
                            description = "Restore from backup",
                            onClick = { /* Restore */ }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingsItem(
                            icon = Icons.Default.FileDownload,
                            title = "Export Data",
                            description = "Export as CSV or PDF",
                            onClick = { showExportDialog = true }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingsItem(
                            icon = Icons.Default.DeleteSweep,
                            title = "Clear All Data",
                            description = "Delete all transactions",
                            onClick = { /* Clear data */ },
                            textColor = Color(0xFFF44336)
                        )
                    }
                }
            }

            // About
            item {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        SettingsItem(
                            icon = Icons.Default.Info,
                            title = "About App",
                            description = "Version 1.0.0",
                            onClick = { showAboutDialog = true }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingsItem(
                            icon = Icons.Default.Star,
                            title = "Rate Us",
                            description = "Rate on Play Store",
                            onClick = { /* Rate app */ }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingsItem(
                            icon = Icons.Default.Share,
                            title = "Share App",
                            description = "Share with friends",
                            onClick = { /* Share app */ }
                        )

                        Divider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingsItem(
                            icon = Icons.Default.Email,
                            title = "Contact Support",
                            description = "Get help & support",
                            onClick = { /* Contact support */ }
                        )
                    }
                }
            }

            // Logout
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Logout */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF44336)
                    )
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Logout",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Footer
            item {
                Text(
                    text = "Smart App v1.0.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }
        }
    }

    // About Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            icon = {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("About Smart App") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Version: 1.0.0")
                    Text("Smart financial management app to track your income and expenses efficiently.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "© 2024 Smart App. All rights reserved.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Export Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            icon = { Icon(Icons.Default.FileDownload, contentDescription = null) },
            title = { Text("Export Data") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Choose export format:")

                    OutlinedButton(
                        onClick = { /* Export as CSV */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Description, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export as CSV")
                    }

                    OutlinedButton(
                        onClick = { /* Export as PDF */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export as PDF")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (textColor == MaterialTheme.colorScheme.onSurface)
                MaterialTheme.colorScheme.primary else textColor,
            modifier = Modifier.size(24.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = if (textColor == MaterialTheme.colorScheme.onSurface)
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                else
                    textColor.copy(alpha = 0.7f)
            )
        }

        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    }
}

@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
