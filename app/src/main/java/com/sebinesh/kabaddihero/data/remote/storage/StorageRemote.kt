package com.sebinesh.kabaddihero.data.remote.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.sebinesh.kabaddihero.core.result.Result
import kotlinx.coroutines.tasks.await

class StorageRemote {
    private val storage: FirebaseStorage? by lazy {
        try {
            FirebaseStorage.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun uploadTournamentPoster(tournamentId: String, fileUri: Uri): Result<String> {
        val s = storage ?: return Result.Success(fileUri.toString())
        return try {
            val ref = s.reference.child("tournaments/$tournamentId/poster/${System.currentTimeMillis()}.jpg")
            ref.putFile(fileUri).await()
            val downloadUrl = ref.downloadUrl.await().toString()
            Result.Success(downloadUrl)
        } catch (e: Exception) {
            Result.Error(e, "Image upload failed: ${e.localizedMessage}")
        }
    }

    suspend fun uploadEvidencePhoto(tournamentId: String, fileUri: Uri): Result<String> {
        val s = storage ?: return Result.Success(fileUri.toString())
        return try {
            val ref = s.reference.child("tournaments/$tournamentId/evidence/${System.currentTimeMillis()}.jpg")
            ref.putFile(fileUri).await()
            val downloadUrl = ref.downloadUrl.await().toString()
            Result.Success(downloadUrl)
        } catch (e: Exception) {
            Result.Error(e, "Evidence upload failed: ${e.localizedMessage}")
        }
    }
}
