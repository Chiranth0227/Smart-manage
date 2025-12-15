package com.example.smartmanage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(onLogin: () -> Unit, onDemoLogin: () -> Unit) {
    var email by remember { mutableStateOf("Chiranth") }
    var password by remember { mutableStateOf("Chiru0227") }
    var isLogin by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF3A1C71), Color(0xFFD76D77), Color(0xFFFFAF7B))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // --- App Header ---
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = "Logo",
                    modifier = Modifier.size(64.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Smart Expense Manager", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Track, analyze, and optimize your spending", color = Color.White.copy(alpha = 0.8f))
            }

            // --- Login/Signup Card ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Get Started", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Login to your account or create a new one", color = Color.Gray)
                    Spacer(modifier = Modifier.height(16.dp))

                    // -- Toggle Buttons --
                    Row {
                        Button(
                            onClick = { isLogin = true },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isLogin) MaterialTheme.colorScheme.primary else Color.LightGray),
                            modifier = Modifier.weight(1f)
                        ) { Text("Login") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { isLogin = false },
                            colors = ButtonDefaults.buttonColors(containerColor = if (!isLogin) MaterialTheme.colorScheme.primary else Color.LightGray),
                            modifier = Modifier.weight(1f)
                        ) { Text("Sign Up") }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // -- Input Fields --
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onLogin() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) { Text(if (isLogin) "Login" else "Sign Up") }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("OR")
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(onClick = { onDemoLogin() }, modifier = Modifier.fillMaxWidth()) {
                        Text("Try Demo Account")
                    }
                }
            }
            
            Text("Your financial data is stored securely on your device", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }
}
