package com.example.myapplication.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.myapplication.notification.WorkoutReminderReceiver
import com.example.myapplication.viewmodel.UserStatsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoginScreen(
    onGoToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    val userStatsViewModel: UserStatsViewModel = viewModel()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colors.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text("Welcome Back 💪", fontSize = 28.sp)
            Spacer(Modifier.height(12.dp))
            Text("Login to continue", fontSize = 15.sp)

            Spacer(Modifier.height(30.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(16.dp))
            }

            Button(
                enabled = !loading,
                onClick = {

                    val cleanEmail = email.trim()

                    if (cleanEmail.isBlank() || password.isBlank()) {
                        errorMessage = "Fill all fields"
                        return@Button
                    }

                    loading = true
                    errorMessage = ""

                    auth.signInWithEmailAndPassword(cleanEmail, password)
                        .addOnSuccessListener { result ->

                            val uid = result.user?.uid

                            if (uid == null) {
                                loading = false
                                errorMessage = "Login failed"
                                return@addOnSuccessListener
                            }

                            // ================= FIREBASE CHECK =================
                            db.collection("Users")
                                .document(uid)
                                .get()
                                .addOnSuccessListener { doc ->

                                    if (!doc.exists()) {
                                        loading = false
                                        errorMessage = "User profile not found"
                                        return@addOnSuccessListener
                                    }

                                    // ================= SAFE INIT =================
                                    userStatsViewModel.startListening()

                                    loading = false

                                    WorkoutReminderReceiver.sendLoginNotification(context)

                                    onLoginSuccess()
                                }
                                .addOnFailureListener {
                                    loading = false
                                    errorMessage = "Firestore error"
                                }
                        }
                        .addOnFailureListener {
                            loading = false
                            errorMessage = "Login failed"
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(if (loading) "Loading..." else "Login")
            }

            Spacer(Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                Text("Don't have an account? ")

                Text(
                    "Register",
                    modifier = Modifier.clickable {
                        onGoToRegister()
                    },
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}