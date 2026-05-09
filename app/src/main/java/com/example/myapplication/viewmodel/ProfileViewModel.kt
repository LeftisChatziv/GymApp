package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ProfileViewModel : ViewModel() {

    fun deleteUserProfile(
        onDone: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        val user = FirebaseAuth.getInstance().currentUser

        if (user == null) {
            onError(Exception("No authenticated user"))
            return
        }

        user.delete()
            .addOnSuccessListener {
                FirebaseAuth.getInstance().signOut() // 🔥 safety
                onDone()
            }
            .addOnFailureListener {
                onError(it)
            }
    }
}