package com.sebinesh.kabaddihero.ui.home

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.R
import com.sebinesh.kabaddihero.data.model.Match
import com.sebinesh.kabaddihero.data.model.Tournament
import com.sebinesh.kabaddihero.ui.components.CourtBackdrop
import com.sebinesh.kabaddihero.ui.components.LanguageToggle
import com.sebinesh.kabaddihero.ui.components.TournamentCard
import com.sebinesh.kabaddihero.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onTournamentClick: (String) -> Unit,
    onViewRulesClick: (String) -> Unit,
    onRegisterClick: (String) -> Unit,
    onLiveMatchClick: (String) -> Unit,
    onAiChatClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onExploreClick: () -> Unit,
    onCreateTournamentClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val shareTournament: (Tournament) -> Unit = { t ->
        val text = """
            Kabaddi Hero
            New Tournament Announcement:
            Tournament: ${t.name}
            Date: ${t.date} (${t.startTime})
            Venue: ${t.venueName}, ${t.district}
            Prize: ₹${t.prizeAmount.toInt()}
            Category: ${t.category} (${t.gender})
            Weight Limit: ${t.weightLimitKg ?: "Open"} KG
            Contact Organizer: ${t.organizerPhone}
            View & Register on Kabaddi Hero: https://kabaddihero.app/tournament/${t.tournamentId}
        """.trimIndent()
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, t.name)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, "Share Kabaddi Tournament"))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Kabaddi Hero",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "by Sebinesh",
                            color = PurpleGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onNotificationsClick,
                        modifier = Modifier.testTag("btn_top_notifications")
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = TextPrimary)
                    }
                    IconButton(
                        onClick = onProfileClick,
                        modifier = Modifier.testTag("btn_top_profile")
                    ) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkPurpleSurface)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAiChatClick,
                icon = {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Ask Kabaddi AI",
                        tint = Color.White
                    )
                },
                text = {
                    Text(
                        text = "Kabaddi AI",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                containerColor = PurplePrimary,
                modifier = Modifier.testTag("fab_kabaddi_ai")
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Search Bar header
            item {
                SearchBarHeader(
                    query = state.searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChanged(it) },
                    onExploreClick = onExploreClick
                )
            }

            // Hero Banner (Featured / Live Kabaddi)
            item {
                HeroBanner(
                    liveMatches = state.liveMatches,
                    onLiveMatchClick = onLiveMatchClick,
                    onAiClick = onAiChatClick,
                    onCreateClick = onCreateTournamentClick
                )
            }

            // Kabaddi AI Assistant Showcase Card
            item {
                KabaddiAiHomeCard(onOpenAi = onAiChatClick)
            }

            // Live matches section (if any)
            if (state.liveMatches.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Live Kabaddi",
                        subtitle = "Real-time Mat & Clay court matches",
                        onSeeAll = onExploreClick
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.liveMatches) { match ->
                            LiveMatchMiniCard(
                                match = match,
                                onClick = { onLiveMatchClick(match.matchId) }
                            )
                        }
                    }
                }
            }

            // Trending Tournaments (Horizontal) - Only if tournaments exist
            if (state.tournaments.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Trending Tournaments",
                        subtitle = "Most registered teams",
                        onSeeAll = onExploreClick
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(state.tournaments.take(5)) { tournament ->
                            Box(modifier = Modifier.width(310.dp)) {
                                TournamentCard(
                                    tournament = tournament,
                                    isSaved = viewModel.isSaved(tournament.tournamentId),
                                    onCardClick = { onTournamentClick(tournament.tournamentId) },
                                    onRegisterClick = { onRegisterClick(tournament.tournamentId) },
                                    onViewRulesClick = { onViewRulesClick(tournament.tournamentId) },
                                    onShareClick = { shareTournament(tournament) },
                                    onToggleSave = { viewModel.toggleSave(tournament.tournamentId) }
                                )
                            }
                        }
                    }
                }
            }

            val filteredList = if (state.searchQuery.isBlank()) {
                state.tournaments
            } else {
                state.tournaments.filter {
                    it.name.contains(state.searchQuery, ignoreCase = true) ||
                            it.district.contains(state.searchQuery, ignoreCase = true) ||
                            it.venueName.contains(state.searchQuery, ignoreCase = true)
                }
            }

            if (filteredList.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkPurpleSurface),
                        border = BorderStroke(1.dp, DarkPurpleBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(PurpleContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SportsKabaddi,
                                    contentDescription = null,
                                    tint = PurplePrimaryBright,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No Tournaments Posted Yet",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "No Kabaddi tournaments have been posted yet. Create and publish the first tournament for your club or town!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Button(
                                onClick = onCreateTournamentClick,
                                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Post Tournament (+)",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            } else {
                item {
                    SectionHeader(
                        title = "Nearby Tournaments",
                        subtitle = "Tournaments near your region",
                        onSeeAll = onExploreClick
                    )
                }

                items(filteredList) { tournament ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        TournamentCard(
                            tournament = tournament,
                            isSaved = viewModel.isSaved(tournament.tournamentId),
                            onCardClick = { onTournamentClick(tournament.tournamentId) },
                            onRegisterClick = { onRegisterClick(tournament.tournamentId) },
                            onViewRulesClick = { onViewRulesClick(tournament.tournamentId) },
                            onShareClick = { shareTournament(tournament) },
                            onToggleSave = { viewModel.toggleSave(tournament.tournamentId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBarHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    onExploreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search tournament, district or venue...", fontSize = 14.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PurplePrimaryBright) },
            modifier = Modifier
                .weight(1f)
                .testTag("home_search_input"),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PurplePrimaryBright,
                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilledTonalIconButton(
            onClick = onExploreClick,
            modifier = Modifier.size(50.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Default.FilterList, contentDescription = "Filters", tint = PurplePrimaryBright)
        }
    }
}

@Composable
fun HeroBanner(
    liveMatches: List<Match>,
    onLiveMatchClick: (String) -> Unit,
    onAiClick: () -> Unit,
    onCreateClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(185.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkPurpleSurface),
        border = BorderStroke(1.dp, DarkPurpleBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background hero image with dark purple scrim
            Image(
                painter = painterResource(id = R.drawable.kabaddi_hero_banner_1790261642022),
                contentDescription = "Kabaddi Action",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Deep purple gradient scrim
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                DarkPurpleSurface.copy(alpha = 0.5f),
                                DarkPurpleBackground.copy(alpha = 0.92f)
                            )
                        )
                    )
            )

            CourtBackdrop(alpha = 0.18f)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (liveMatches.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(AlertRed)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "● LIVE MATCHES AVAILABLE",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(PurpleContainer)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "OFFICIAL KABADDI PLATFORM",
                                color = PurplePrimaryBright,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // AI Assistant chip button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.horizontalGradient(listOf(PurplePrimary, PurpleAccent)))
                            .clickable(onClick = onAiClick)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Kabaddi AI", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Column {
                    Text(
                        text = "Kabaddi Hero",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Post tournaments • Live scoreboards • AI rules assistant",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onCreateClick,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Post Tournament", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        if (liveMatches.isNotEmpty()) {
                            OutlinedButton(
                                onClick = { onLiveMatchClick(liveMatches.first().matchId) },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("Watch Live", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PurplePrimaryBright)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String,
    onSeeAll: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
        TextButton(onClick = onSeeAll) {
            Text(text = "See All", fontSize = 12.sp, color = PurplePrimaryBright, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LiveMatchMiniCard(
    match: Match,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkPurpleSurface),
        border = BorderStroke(1.dp, DarkPurpleBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.stage,
                    color = PurpleGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(AlertRed)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = "LIVE", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Score Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = match.teamAName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(text = "${match.teamAPlayersOnCourt} players", color = TextTertiary, fontSize = 10.sp)
                }

                Text(
                    text = "${match.scoreTeamA} - ${match.scoreTeamB}",
                    color = PurplePrimaryBright,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = match.teamBName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(text = "${match.teamBPlayersOnCourt} players", color = TextTertiary, fontSize = 10.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Raider: ${match.activeRaider.ifBlank { "Ready" }}",
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
fun KabaddiAiHomeCard(
    onOpenAi: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable(onClick = onOpenAi)
            .testTag("card_kabaddi_ai_home"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = DarkPurpleSurface),
        border = BorderStroke(1.dp, PurplePrimary.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Brush.linearGradient(listOf(PurplePrimary, PurpleAccent)), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Kabaddi Hero AI",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Rules • Tactics • Poster Scanner • Eligibility",
                            style = MaterialTheme.typography.bodySmall,
                            color = PurpleGold,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(PurplePrimary.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Google AI Studio",
                        color = PurplePrimaryBright,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Instant answers for tournament rules, bonus line disputes, weight categories, and raider tactics using Gemini 3.5 Flash.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onOpenAi,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_ask_kabaddi_ai"),
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ask AI Coach", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

