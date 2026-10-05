package com.sebinesh.kabaddihero.data.remote.firestore

import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.model.Report
import kotlinx.coroutines.tasks.await

class ReportRemote {
    suspend fun submitReport(report: Report): Result<String> {
        val col = FirestoreRefs.reports() ?: return Result.Success("local_rep_${System.currentTimeMillis()}")
        return try {
            val id = if (report.reportId.isBlank()) col.document().id else report.reportId
            col.document(id).set(report.copy(reportId = id)).await()
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error(e, "Failed to submit report: ${e.localizedMessage}")
        }
    }

    suspend fun getOpenReports(): Result<List<Report>> {
        val col = FirestoreRefs.reports() ?: return Result.Success(emptyList())
        return try {
            val snapshot = col.whereEqualTo("status", "OPEN").get().await()
            Result.Success(snapshot.toObjects(Report::class.java))
        } catch (e: Exception) {
            Result.Error(e, "Failed to fetch reports: ${e.localizedMessage}")
        }
    }
}
