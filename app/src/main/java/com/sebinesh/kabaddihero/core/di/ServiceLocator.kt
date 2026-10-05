package com.sebinesh.kabaddihero.core.di

import android.annotation.SuppressLint
import android.content.Context
import com.sebinesh.kabaddihero.data.local.DataStorePrefs
import com.sebinesh.kabaddihero.data.remote.ai.*
import com.sebinesh.kabaddihero.data.remote.auth.AuthRemote
import com.sebinesh.kabaddihero.data.remote.firestore.*
import com.sebinesh.kabaddihero.data.remote.storage.StorageRemote
import com.sebinesh.kabaddihero.data.repository.*

@SuppressLint("StaticFieldLeak")
object ServiceLocator {
    lateinit var appContext: Context
        private set

    val dataStorePrefs: DataStorePrefs by lazy { DataStorePrefs(appContext) }

    // Remotes
    val authRemote: AuthRemote by lazy { AuthRemote() }
    val userRemote: UserRemote by lazy { UserRemote() }
    val tournamentRemote: TournamentRemote by lazy { TournamentRemote() }
    val matchRemote: MatchRemote by lazy { MatchRemote() }
    val reportRemote: ReportRemote by lazy { ReportRemote() }
    val ruleProfileRemote: RuleProfileRemote by lazy { RuleProfileRemote() }
    val storageRemote: StorageRemote by lazy { StorageRemote() }

    // AI
    val geminiService: GeminiService by lazy { GeminiService() }
    val aiChatService: AiChatService by lazy { AiChatService(geminiService) }
    val aiRulesService: AiRulesService by lazy { AiRulesService(geminiService) }
    val aiPosterExtractionService: AiPosterExtractionService by lazy { AiPosterExtractionService(geminiService) }
    val aiEligibilityService: AiEligibilityService by lazy { AiEligibilityService(geminiService) }
    val aiTournamentSummaryService: AiTournamentSummaryService by lazy { AiTournamentSummaryService(geminiService) }

    // Repositories
    val authRepository: AuthRepository by lazy { AuthRepository(authRemote, userRemote) }
    val tournamentRepository: TournamentRepository by lazy { TournamentRepository(tournamentRemote) }
    val playerRepository: PlayerRepository by lazy { PlayerRepository() }
    val teamRepository: TeamRepository by lazy { TeamRepository() }
    val matchRepository: MatchRepository by lazy { MatchRepository(matchRemote) }
    val ruleProfileRepository: RuleProfileRepository by lazy { RuleProfileRepository(ruleProfileRemote) }
    val notificationRepository: NotificationRepository by lazy { NotificationRepository() }
    val reportRepository: ReportRepository by lazy { ReportRepository(reportRemote) }

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }
}
