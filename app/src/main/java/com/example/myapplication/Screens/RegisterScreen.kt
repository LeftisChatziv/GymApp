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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp

@Composable
fun RegisterScreen(
    onGoToLogin: () -> Unit
) {

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Create Account 💪",
                color = colors.onBackground,
                fontSize = 28.sp
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Register to start your fitness journey",
                color = colors.onSurfaceVariant,
                fontSize = 15.sp
            )

            Spacer(Modifier.height(30.dp))

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

            Spacer(Modifier.height(16.dp))

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

            Spacer(Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = colors.error,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(16.dp))
            }

            Button(
                enabled = !loading,
                onClick = {

                    loading = true
                    errorMessage = ""

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { result ->

                            val uid = result.user?.uid

                            if (uid == null) {
                                loading = false
                                errorMessage = "User ID error"
                                return@addOnSuccessListener
                            }

                            val userMap = hashMapOf(
                                "UID" to uid,
                                "Name" to "",
                                "ProfilePicUrl" to "",
                                "created" to Timestamp.now(),
                                "email" to email,
                                "rank" to "Beginner",
                                "streak" to 0,
                                "totalWorkouts" to 0
                            )

                            db.collection("Users")
                                .document(uid)
                                .collection("profile")
                                .document("id")
                                .set(userMap)
                                .addOnSuccessListener {
                                    loading = false
                                    onGoToLogin()
                                }
                                .addOnFailureListener { e ->
                                    loading = false
                                    errorMessage = e.message ?: "Failed to save user"
                                }
                        }
                        .addOnFailureListener { e ->
                            loading = false
                            errorMessage = e.message ?: "Registration failed"
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
                Text(if (loading) "Loading..." else "Register", fontSize = 18.sp)
            }

            Spacer(Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    "Already have an account? ",
                    color = colors.onSurfaceVariant,
                    fontSize = 15.sp
                )

                Text(
                    "Login",
                    color = colors.primary,
                    fontSize = 15.sp,
                    modifier = Modifier.clickable {
                        onGoToLogin()
                    }
                )
            }
        }
    }
}