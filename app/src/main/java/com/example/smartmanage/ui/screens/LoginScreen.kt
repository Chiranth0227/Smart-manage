package com.example.smartmanage.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    onDemoLogin: () -> Unit,
    navController: NavHostController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

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
                Text(
                    "Smart Expense Manager",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Track, analyze, and optimize your spending",
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            // --- Login/Signup Card ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Get Started", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(
                        if (isLogin) "Login to your account" else "Create a new account",
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // -- Toggle Buttons --
                    Row {
                        Button(
                            onClick = {
                                isLogin = true
                                errorMessage = ""
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isLogin) MaterialTheme.colorScheme.primary else Color.LightGray
                            ),
                            modifier = Modifier.weight(1f)
                        ) { Text("Login") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                isLogin = false
                                errorMessage = ""
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isLogin) MaterialTheme.colorScheme.primary else Color.LightGray
                            ),
                            modifier = Modifier.weight(1f)
                        ) { Text("Sign Up") }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // -- Input Fields --
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            errorMessage = ""
                        },
                        label = { Text("Email") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = errorMessage.isNotEmpty()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = ""
                        },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = errorMessage.isNotEmpty()
                    )

                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                errorMessage = "Please fill in all fields"
                                return@Button
                            }

                            if (password.length < 6) {
                                errorMessage = "Password must be at least 6 characters"
                                return@Button
                            }

                            isLoading = true
                            errorMessage = ""

                            if (isLogin) {
                                // Login
                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        isLoading = false
                                        if (task.isSuccessful) {
                                            Log.d("LoginScreen", "signInWithEmail:success")
                                            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                                            onLogin()
                                        } else {
                                            Log.w("LoginScreen", "signInWithEmail:failure", task.exception)
                                            errorMessage = task.exception?.message ?: "Login failed"
                                            Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                            } else {
                                // Sign Up
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        isLoading = false
                                        if (task.isSuccessful) {
                                            Log.d("LoginScreen", "createUserWithEmail:success")
                                            Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                                            onLogin()
                                        } else {
                                            Log.w("LoginScreen", "createUserWithEmail:failure", task.exception)
                                            errorMessage = task.exception?.message ?: "Sign up failed"
                                            Toast.makeText(context, "Sign up failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                                        }
                                    }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(if (isLogin) "Login" else "Sign Up")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("OR")
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { onDemoLogin() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        Text("Try Demo Account")
                    }
                }
            }

            Text(
                "Your financial data is stored securely",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}