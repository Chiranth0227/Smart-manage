package com.example.smartmanage.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.smartmanage.data.AppDatabase
import com.example.smartmanage.data.Budget
import com.example.smartmanage.formatCurrency
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(database: AppDatabase) {
    val scope = rememberCoroutineScope()
    val budgets by database.transactionDao().getAllBudgets().collectAsState(initial = emptyList())
    var showAddDialog by remember { mutableStateOf(false) }
    var editingBudget by remember { mutableStateOf<Budget?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Budgets", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Budget")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (budgets.isEmpty()) {
                item {
                    EmptyBudgetState()
                }
            } else {
                items(budgets) { budget ->
                    BudgetCard(
                        budget = budget,
                        database = database,
                        onEdit = { editingBudget = budget },
                        onDelete = {
                            scope.launch {
                                database.transactionDao().deleteBudget(budget)
                            }
                        }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddEditBudgetDialog(
            budget = null,
            onDismiss = { showAddDialog = false },
            onSave = { newBudget ->
                scope.launch {
                    database.transactionDao().insertBudget(newBudget)
                }
                showAddDialog = false
            }
        )
    }

    if (editingBudget != null) {
        AddEditBudgetDialog(
            budget = editingBudget,
            onDismiss = { editingBudget = null },
            onSave = { updatedBudget ->
                scope.launch {
                    database.transactionDao().updateBudget(updatedBudget)
                }
                editingBudget = null
            }
        )
    }
}

@Composable
fun BudgetCard(
    budget: Budget,
    database: AppDatabase,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var spentAmount by remember { mutableStateOf(0.0) }
    var showMenu by remember { mutableStateOf(false) }
    var showConfirmDelete by remember { mutableStateOf(false) }

    LaunchedEffect(budget.category) {
        database.transactionDao().getSpentByCategory(budget.category).collectLatest {
            spentAmount = it
        }
    }

    val progress = if (budget.amount > 0) (spentAmount / budget.amount).toFloat() else 0f
    val progressColor = when {
        progress >= 1f -> Color(0xFFF44336) // Red
        progress >= 0.8f -> Color(0xFFFF9800) // Orange
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = budget.category,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = budget.period,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                onEdit()
                                showMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Delete", color = Color(0xFFF44336)) },
                            onClick = {
                                showConfirmDelete = true
                                showMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFF44336)) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Spent: ${formatCurrency(spentAmount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = progressColor,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Limit: ${formatCurrency(budget.amount)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            
            if (progress >= 1f) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Budget Exceeded!",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFF44336),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showConfirmDelete) {
        AlertDialog(
            onDismissRequest = { showConfirmDelete = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null) },
            title = { Text("Delete Budget?") },
            text = { Text("Are you sure you want to delete the budget for ${budget.category}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete()
                        showConfirmDelete = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFF44336))
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDelete = false }) { Text("Cancel") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBudgetDialog(
    budget: Budget?,
    onDismiss: () -> Unit,
    onSave: (Budget) -> Unit
) {
    var category by remember { mutableStateOf(budget?.category ?: "") }
    var amount by remember { mutableStateOf(budget?.amount?.toString() ?: "") }
    var period by remember { mutableStateOf(budget?.period ?: "Monthly") }

    val categories = listOf("Food", "Housing", "Transport", "Bills", "Health", "Entertainment", "Shopping", "Salary", "Freelance", "Investment", "Others", "Personal", "Family", "Business", "Travel")
    val periods = listOf("Weekly", "Monthly", "Yearly")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (budget == null) "Create Budget" else "Edit Budget",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                var expandedCategory by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { expandedCategory = it }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryEditable, true)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Limit Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                var expandedPeriod by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expandedPeriod,
                    onExpandedChange = { expandedPeriod = it }
                ) {
                    OutlinedTextField(
                        value = period,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Period") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPeriod) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable, true)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedPeriod,
                        onDismissRequest = { expandedPeriod = false }
                    ) {
                        periods.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p) },
                                onClick = {
                                    period = p
                                    expandedPeriod = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull()
                    if (category.isNotEmpty() && amountDouble != null && amountDouble > 0) {
                        onSave(
                            Budget(
                                id = budget?.id ?: 0,
                                category = category,
                                amount = amountDouble,
                                period = period
                            )
                        )
                    }
                }
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun EmptyBudgetState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.AccountBalanceWallet,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("No budgets set", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        Spacer(modifier = Modifier.height(8.dp))
        Text("Create a budget to track your spending limits", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
    }
}
