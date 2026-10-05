package com.sebinesh.kabaddihero.data.remote.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sebinesh.kabaddihero.core.result.Result
import kotlinx.coroutines.tasks.await

class AuthRemote {
    private val auth: FirebaseAuth? by lazy {
        try {
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    fun getCurrentUser(): FirebaseUser? = auth?.currentUser

    fun isUserLoggedIn(): Boolean = auth?.currentUser != null

    suspend fun signInWithEmail(email: String, pass: String): Result<FirebaseUser?> {
        val a = auth ?: return Result.Success(null)
        return try {
            val res = a.signInWithEmailAndPassword(email, pass).await()
            Result.Success(res.user)
        } catch (e: Exception) {
            Result.Error(e, e.localizedMessage ?: "Sign in failed")
        }
    }

    suspend fun signUpWithEmail(email: String, pass: String): Result<FirebaseUser?> {
        val a = auth ?: return Result.Success(null)
        return try {
            val res = a.createUserWithEmailAndPassword(email, pass).await()
            Result.Success(res.user)
        } catch (e: Exception) {
            Result.Error(e, e.localizedMessage ?: "Registration failed")
        }
    }

    suspend fun signInAnonymously(displayName: String? = null): Result<FirebaseUser?> {
        val a = auth ?: return Result.Success(null)
        return try {
            val res = a.signInAnonymously().await()
            val user = res.user
            if (!displayName.isNullOrBlank() && user != null) {
                try {
                    val profileUpdates = com.google.firebase.auth.UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName.trim())
                        .build()
                    user.updateProfile(profileUpdates).await()
                } catch (_: Exception) {}
            }
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e, e.localizedMessage ?: "Guest sign in failed")
        }
    }

    fun signOut() {
        auth?.signOut()
    }
}
