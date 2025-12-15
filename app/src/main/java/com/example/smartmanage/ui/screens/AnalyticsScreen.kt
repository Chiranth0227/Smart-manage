package com.example.smartmanage.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartmanage.data.AppDatabase
import com.example.smartmanage.formatCurrency
import kotlinx.coroutines.flow.map
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(database: AppDatabase) {
    var selectedPeriod by remember { mutableStateOf("This Month") }
    var showPeriodMenu by remember { mutableStateOf(false) }

    // Fetch data from database
    val transactions by database.transactionDao().getAllTransactions().collectAsState(initial = emptyList())

    // Process data for charts
    val incomeTransactions = transactions.filter { it.type == "income" }
    val expenseTransactions = transactions.filter { it.type == "expense" }

    val totalIncome = incomeTransactions.sumOf { it.amount }
    val totalExpense = expenseTransactions.sumOf { it.amount }
    val savingsRate = if (totalIncome > 0) ((totalIncome - totalExpense) / totalIncome * 100).toInt() else 0

    // Group expenses by note (or category if you add a category field later)
    val expensesByCategory = expenseTransactions
        .groupBy { it.note ?: "Others" }
        .map { (category, list) ->
            CategoryData(
                name = category,
                amount = list.sumOf { it.amount },
                color = generateColorForCategory(category)
            )
        }
        .sortedByDescending { it.amount }
        .take(6) // Top 6 categories

    // For simplicity, we'll just mock monthly trend data for now based on the total,
    // or you could implement actual date-based grouping if your Transaction entity had a date field.
    // Since Transaction only has id, amount, type, note, we can't do accurate monthly trends yet.
    // We will show the current totals as a "trend" for demonstration.
    val monthlyData = listOf(
        MonthData("Current", totalIncome, totalExpense)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Analytics",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Financial insights & trends",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Box {
                    OutlinedButton(
                        onClick = { showPeriodMenu = true },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(selectedPeriod)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }

                    DropdownMenu(
                        expanded = showPeriodMenu,
                        onDismissRequest = { showPeriodMenu = false }
                    ) {
                        listOf("This Week", "This Month", "This Year", "All Time").forEach { period ->
                            DropdownMenuItem(
                                text = { Text(period) },
                                onClick = {
                                    selectedPeriod = period
                                    showPeriodMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Summary Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    title = "Income",
                    value = formatCurrency(totalIncome),
                    icon = Icons.Default.TrendingUp,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Expense",
                    value = formatCurrency(totalExpense),
                    icon = Icons.Default.TrendingDown,
                    color = Color(0xFFF44336),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            SavingsRateCard(savingsRate)
        }

        // Line Chart (Placeholder for now as we lack date data)
        /*
        item {
            Card(...) { ... }
        }
        */

        // Pie Chart
        if (expensesByCategory.isNotEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Expense by Category",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Distribution breakdown",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(24.dp))

                        PieChart(
                            data = expensesByCategory,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        expensesByCategory.forEach { category ->
                            CategoryItem(category, totalExpense)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        } else {
            item {
                Text(
                    text = "No expense data available for analysis",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

fun generateColorForCategory(name: String): Color {
    val hash = name.hashCode()
    val r = (hash and 0xFF0000 shr 16) / 255f
    val g = (hash and 0x00FF00 shr 8) / 255f
    val b = (hash and 0x0000FF) / 255f
    return Color(r, g, b, 1f).copy(alpha = 0.6f).compositeOver(Color.White)
}

fun Color.compositeOver(background: Color): Color {
    val alpha = this.alpha
    val oneMinusAlpha = 1f - alpha
    val r = this.red * alpha + background.red * oneMinusAlpha
    val g = this.green * alpha + background.green * oneMinusAlpha
    val b = this.blue * alpha + background.blue * oneMinusAlpha
    return Color(r, g, b, 1f)
}


@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(32.dp)
            )

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}

@Composable
fun SavingsRateCard(savingsRate: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Savings Rate",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$savingsRate%",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                CircularProgressIndicator(
                    progress = savingsRate / 100f,
                    modifier = Modifier.size(80.dp),
                    strokeWidth = 8.dp,
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.3f)
                )
            }
        }
    }
}

// LineChart and BarChart removed or commented out as they relied on monthlyData which we don't fully have from DB yet.
// If you implement dates in Transaction, you can uncomment and adapt them.

@Composable
fun PieChart(data: List<CategoryData>, modifier: Modifier = Modifier) {
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatedProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    val total = data.sumOf { it.amount }

    Canvas(modifier = modifier) {
        val radius = min(size.width, size.height) / 2.5f
        val center = Offset(size.width / 2, size.height / 2)

        var startAngle = -90f

        data.forEach { category ->
            val sweepAngle = (category.amount / total * 360 * animatedProgress.value).toFloat()

            drawArc(
                color = category.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )

            startAngle += sweepAngle
        }

        // Draw center circle for donut effect
        drawCircle(
            color = Color.White,
            radius = radius * 0.5f,
            center = center
        )
    }
}

@Composable
fun CategoryItem(category: CategoryData, totalExpense: Double) {
    val percentage = if (totalExpense > 0) (category.amount / totalExpense * 100).toInt() else 0

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(category.color)
            )

            Column {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "$percentage% of expenses",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        Text(
            text = formatCurrency(category.amount),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
            )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

data class MonthData(val month: String, val income: Double, val expense: Double)
data class CategoryData(val name: String, val amount: Double, val color: Color)
