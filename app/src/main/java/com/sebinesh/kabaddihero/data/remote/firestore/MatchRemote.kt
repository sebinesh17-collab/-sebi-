package com.sebinesh.kabaddihero.data.remote.firestore

import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.model.Match
import com.sebinesh.kabaddihero.data.model.MatchEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MatchRemote {
    fun observeLiveMatches(): Flow<List<Match>> = callbackFlow {
        val col = FirestoreRefs.matches()
        if (col == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        val listener = col.whereEqualTo("status", "LIVE")
            .addSnapshotListener { snap, _ ->
                if (snap != null) {
                    trySend(snap.toObjects(Match::class.java))
                }
            }
        awaitClose { listener.remove() }
    }

    fun observeMatch(matchId: String): Flow<Match?> = callbackFlow {
        val col = FirestoreRefs.matches()
        if (col == null) {
            trySend(null)
            close()
            return@callbackFlow
        }
        val listener = col.document(matchId).addSnapshotListener { snap, _ ->
            trySend(snap?.toObject(Match::class.java))
        }
        awaitClose { listener.remove() }
    }

    suspend fun saveMatch(match: Match): Result<Unit> {
        val col = FirestoreRefs.matches() ?: return Result.Success(Unit)
        return try {
            col.document(match.matchId).set(match).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Failed to update match: ${e.localizedMessage}")
        }
    }

    suspend fun recordEvent(event: MatchEvent): Result<Unit> {
        val col = FirestoreRefs.matchEvents() ?: return Result.Success(Unit)
        return try {
            val id = if (event.eventId.isBlank()) col.document().id else event.eventId
            col.document(id).set(event.copy(eventId = id)).await()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Failed to record event: ${e.localizedMessage}")
        }
    }
}
