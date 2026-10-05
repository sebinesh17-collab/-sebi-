package com.sebinesh.kabaddihero.data.remote.firestore

import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.model.User
import kotlinx.coroutines.tasks.await

class UserRemote {
    suspend fun getUser(uid: String): Result<User?> {
        val col = FirestoreRefs.users() ?: return Result.Success(null)
        return try {
            val snapshot = col.document(uid).get().await()
            if (snapshot.exists()) {
                Result.Success(snapshot.toObject(User::class.java))
            } else {
                Result.Success(null)
            }
        } catch (e: Exception) {
            Result.Error(e, "Failed to load user profile: ${e.localizedMessage}")
        }
    }

    suspend fun saveUser(user: User): Result<Unit> {
        val col = FirestoreRefs.users() ?: return Result.Success(Unit)
        return try {
            col.document(user.uid).set(user).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Failed to update profile: ${e.localizedMessage}")
        }
    }
}
