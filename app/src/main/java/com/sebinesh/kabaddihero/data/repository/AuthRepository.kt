package com.sebinesh.kabaddihero.data.repository

import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.model.User
import com.sebinesh.kabaddihero.data.model.UserRole
import com.sebinesh.kabaddihero.data.remote.auth.AuthRemote
import com.sebinesh.kabaddihero.data.remote.firestore.UserRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepository(
    private val authRemote: AuthRemote,
    private val userRemote: UserRemote
) {
    private val _currentUserState = MutableStateFlow<User?>(null)
    val currentUserState: StateFlow<User?> = _currentUserState.asStateFlow()

    init {
        val fbUser = authRemote.getCurrentUser()
        if (fbUser != null) {
            _currentUserState.value = User(
                uid = fbUser.uid,
                displayName = fbUser.displayName?.ifBlank { null } ?: "Player",
                email = fbUser.email ?: "",
                role = UserRole.USER
            )
        } else {
            // First login is required to use the app
            _currentUserState.value = null
        }
    }

    fun isUserLoggedIn(): Boolean = _currentUserState.value != null

    suspend fun signIn(email: String, pass: String): Result<User> {
        val res = authRemote.signInWithEmail(email, pass)
        return when (res) {
            is Result.Success -> {
                val fbUser = res.data
                val uid = fbUser?.uid ?: "user_${System.currentTimeMillis()}"
                val userRes = userRemote.getUser(uid)
                val user = userRes.getOrNull() ?: User(
                    uid = uid,
                    email = email,
                    displayName = fbUser?.displayName ?: email.substringBefore("@"),
                    role = UserRole.USER
                )
                _currentUserState.value = user
                Result.Success(user)
            }
            is Result.Error -> Result.Error(res.exception, res.userMessage)
            is Result.Loading -> Result.Loading
        }
    }

    suspend fun signUp(email: String, pass: String, displayName: String, role: UserRole): Result<User> {
        val res = authRemote.signUpWithEmail(email, pass)
        return when (res) {
            is Result.Success -> {
                val uid = res.data?.uid ?: "user_${System.currentTimeMillis()}"
                val newUser = User(
                    uid = uid,
                    email = email,
                    displayName = displayName,
                    role = role
                )
                userRemote.saveUser(newUser)
                _currentUserState.value = newUser
                Result.Success(newUser)
            }
            is Result.Error -> Result.Error(res.exception, res.userMessage)
            is Result.Loading -> Result.Loading
        }
    }

    suspend fun signInAsGuest(guestName: String = ""): Result<User> {
        val cleanName = guestName.trim().ifBlank { "Guest Player" }
        val authRes = authRemote.signInAnonymously(cleanName)
        val uid = when (authRes) {
            is Result.Success -> authRes.data?.uid ?: "guest_${System.currentTimeMillis()}"
            else -> "guest_${System.currentTimeMillis()}"
        }
        val guest = User(
            uid = uid,
            displayName = cleanName,
            role = UserRole.USER,
            district = "Madurai"
        )
        userRemote.saveUser(guest)
        _currentUserState.value = guest
        return Result.Success(guest)
    }

    fun signOut() {
        authRemote.signOut()
        _currentUserState.value = null
    }

    fun updateCurrentUserRole(role: UserRole) {
        _currentUserState.value = _currentUserState.value?.copy(role = role)
    }
}
