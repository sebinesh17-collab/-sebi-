package com.sebinesh.kabaddihero.data.repository

import com.sebinesh.kabaddihero.core.result.Result
import com.sebinesh.kabaddihero.data.model.Report
import com.sebinesh.kabaddihero.data.remote.firestore.ReportRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReportRepository(
    private val reportRemote: ReportRemote
) {
    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports.asStateFlow()

    suspend fun submitReport(report: Report): Result<String> {
        val res = reportRemote.submitReport(report)
        val id = res.getOrNull() ?: "report_${System.currentTimeMillis()}"
        _reports.value = _reports.value + report.copy(reportId = id)
        return Result.Success(id)
    }

    suspend fun loadReports(): List<Report> {
        val res = reportRemote.getOpenReports()
        return if (res is Result.Success) {
            _reports.value = res.data
            res.data
        } else {
            _reports.value
        }
    }
}
