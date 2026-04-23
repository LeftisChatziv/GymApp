package com.example.myapplication.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    onGoToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {

    val auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colors.background
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = "Welcome Back 💪",
                    color = colors.onBackground,
                    fontSize = 28.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Login to continue",
                    color = colors.onSurfaceVariant,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(30.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Button(
                    enabled = !loading,
                    onClick = {
                        loading = true
                        errorMessage = ""

                        auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                loading = false
                                onLoginSuccess()
                            }
                            .addOnFailureListener { e ->
                                loading = false
                                errorMessage = e.message ?: "Login failed"
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.primary,
                        contentColor = colors.onPrimary
                    )
                ) {
                    Text(if (loading) "Loading..." else "Login", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        "Don't have an account? ",
                        color = colors.onSurfaceVariant,
                        fontSize = 15.sp
                    )

                    Text(
                        "Register",
                        color = colors.secondary,
                        fontSize = 15.sp,
                        modifier = Modifier.clickable { onGoToRegister() }
                    )
                }
            }
        }
    }
}