package com.sebinesh.kabaddihero.data.remote.firestore

import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.model.Tournament
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TournamentRemote {
    suspend fun getTournaments(): Result<List<Tournament>> {
        val col = FirestoreRefs.tournaments() ?: return Result.Success(emptyList())
        return try {
            val snapshot = col.get().await()
            val list = snapshot.toObjects(Tournament::class.java)
            Result.Success(list)
        } catch (e: Exception) {
            Result.Error(e, "Error loading tournaments: ${e.localizedMessage}")
        }
    }

    fun observeTournaments(): Flow<List<Tournament>> = callbackFlow {
        val col = FirestoreRefs.tournaments()
        if (col == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        val listener = col.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(emptyList())
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val list = snapshot.toObjects(Tournament::class.java)
                trySend(list)
            }
        }
        awaitClose { listener.remove() }
    }

    suspend fun getTournamentById(id: String): Result<Tournament?> {
        val col = FirestoreRefs.tournaments() ?: return Result.Success(null)
        return try {
            val doc = col.document(id).get().await()
            Result.Success(doc.toObject(Tournament::class.java))
        } catch (e: Exception) {
            Result.Error(e, "Error loading tournament details: ${e.localizedMessage}")
        }
    }

    suspend fun saveTournament(tournament: Tournament): Result<String> {
        val col = FirestoreRefs.tournaments()
            ?: return Result.Success(tournament.tournamentId.ifEmpty { "local_${System.currentTimeMillis()}" })
        return try {
            val id = if (tournament.tournamentId.isBlank()) {
                col.document().id
            } else {
                tournament.tournamentId
            }
            val entity = tournament.copy(tournamentId = id, updatedAt = System.currentTimeMillis())
            col.document(id).set(entity).await()
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error(e, "Failed to publish tournament: ${e.localizedMessage}")
        }
    }

    suspend fun findPossibleDuplicates(name: String, date: String, district: String): List<Tournament> {
        val col = FirestoreRefs.tournaments() ?: return emptyList()
        return try {
            val snapshot = col.whereEqualTo("date", date)
                .whereEqualTo("district", district)
                .get()
                .await()
            val allOnDate = snapshot.toObjects(Tournament::class.java)
            allOnDate.filter {
                it.name.contains(name.trim(), ignoreCase = true) ||
                        name.contains(it.name.trim(), ignoreCase = true)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
