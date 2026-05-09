package com.example.myapplication.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisterScreen(
    onGoToLogin: () -> Unit
) {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Text("Create Account 💪", fontSize = 30.sp)

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it.trim() },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(Modifier.height(20.dp))

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(12.dp))
            }

            Button(
                enabled = !loading,
                onClick = {

                    if (name.isBlank() || email.isBlank() || password.isBlank()) {
                        errorMessage = "Please fill all fields"
                        return@Button
                    }

                    if (password.length < 6) {
                        errorMessage = "Password must be at least 6 characters"
                        return@Button
                    }

                    loading = true
                    errorMessage = ""

                    auth.createUserWithEmailAndPassword(email.trim(), password)
                        .addOnSuccessListener { result ->

                            val uid = result.user?.uid

                            if (uid == null) {
                                loading = false
                                errorMessage = "User creation failed"
                                return@addOnSuccessListener
                            }

                            val userData = hashMapOf(
                                "name" to name.trim(),
                                "email" to email.trim(),
                                "rank" to "Beginner",
                                "streak" to 0,
                                "score" to 0.0,
                                "totalWorkouts" to 0,
                                "weight" to 70,
                                "lastWorkoutDate" to 0L,
                                "created" to Timestamp.now()
                            )

                            db.collection("Users")
                                .document(uid)
                                .set(userData)
                                .addOnSuccessListener {

                                    loading = false
                                    onGoToLogin()
                                }
                                .addOnFailureListener { e ->
                                    loading = false
                                    errorMessage = "Firestore Error: ${e.message}"
                                }
                        }
                        .addOnFailureListener { e ->
                            loading = false
                            errorMessage = "Auth Error: ${e.message}"
                        }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(if (loading) "Creating..." else "Register")
            }

            Spacer(Modifier.height(16.dp))

            TextButton(onClick = onGoToLogin) {
                Text("Already have an account? Login")
            }
        }
    }
}