package com.example.smartmanage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class TemplateData(
    val title: String,
    val description: String,
    val budget: String,
    val icon: ImageVector,
    val iconColor: Color,
    val budgetColor: Color
)

@Composable
fun TemplatesGrid() {
    val templates = listOf(
        TemplateData(
            "Personal Finance",
            "Track daily expenses and personal budget",
            "₹1,000 budget",
            Icons.Default.Person,
            Color(0xFF5E35B1), // Deep Purple
            Color(0xFFE3F2FD)  // Light Blue bg for badge
        ),
        TemplateData(
            "Family Budget",
            "Manage household expenses together",
            "₹3,000 budget",
            Icons.Default.Group,
            Color(0xFF43A047), // Green
            Color(0xFFE8F5E9)  // Light Green bg for badge
        ),
        TemplateData(
            "Business Expenses",
            "Track business costs and receipts",
            "₹5,000 budget",
            Icons.Default.BusinessCenter,
            Color(0xFF795548), // Brown
            Color(0xFFEFEBE9)  // Light Brown bg for badge
        ),
        TemplateData(
            "Travel & Vacation",
            "Plan and track travel expenses",
            "₹2,000 budget",
            Icons.Default.Flight,
            Color(0xFF1E88E5), // Blue
            Color(0xFFF3E5F5)  // Light Purple bg for badge
        )
    )

    Column {
        val rows = templates.chunked(2)
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { item ->
                    TemplateCard(item, Modifier.weight(1f))
                }
                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun TemplateCard(template: TemplateData, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp), // Fixed height to align items
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = template.icon,
                contentDescription = null,
                tint = template.iconColor,
                modifier = Modifier.size(40.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = template.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = template.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Surface(
                color = template.budgetColor, // Using budgetColor as bg for badge
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = template.budget,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}
