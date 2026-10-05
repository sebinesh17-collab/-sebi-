package com.sebinesh.kabaddihero.ui.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.ui.components.EmptyState
import com.sebinesh.kabaddihero.ui.components.TournamentCard
import com.sebinesh.kabaddihero.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel,
    onTournamentClick: (String) -> Unit,
    onRegisterClick: (String) -> Unit,
    onViewRulesClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Explore Tournaments", fontWeight = FontWeight.Bold, color = TextPrimary) },
                actions = {
                    // List / Map toggle
                    IconButton(onClick = { viewModel.setMapView(!state.isMapView) }) {
                        Icon(
                            imageVector = if (state.isMapView) Icons.Default.List else Icons.Default.Map,
                            contentDescription = "Toggle Map/List",
                            tint = PurplePrimaryBright
                        )
                    }
                    IconButton(onClick = { viewModel.setFilterSheetVisible(true) }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filters", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkPurpleSurface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search field
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = { Text("Search tournament, town or venue...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PurplePrimaryBright) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("explore_search_field"),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )

            if (state.isMapView) {
                // Map View representation showing markers for coordinates
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .background(DarkPurpleSurface, RoundedCornerShape(18.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Place, contentDescription = null, tint = PurplePrimaryBright)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Map View",
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                            Text(
                                text = "${state.filteredTournaments.size} tournaments",
                                color = PurplePrimaryBright,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Interactive markers list
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(state.filteredTournaments) { t ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onTournamentClick(t.tournamentId) },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Default.Place, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(text = t.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            Text(text = "${t.venueName} • ${t.district}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                        Text(text = "₹${t.prizeAmount.toInt()}", fontWeight = FontWeight.Bold, color = PurpleGold)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // List View
                if (state.filteredTournaments.isEmpty()) {
                    EmptyState(
                        title = "No Tournaments Found",
                        subtitle = "No tournaments match your current filters or search query.",
                        actionButtonText = "Clear Filters",
                        onActionClick = { viewModel.clearFilters() }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.filteredTournaments) { tournament ->
                            TournamentCard(
                                tournament = tournament,
                                isSaved = false,
                                onCardClick = { onTournamentClick(tournament.tournamentId) },
                                onRegisterClick = { onRegisterClick(tournament.tournamentId) },
                                onViewRulesClick = { onViewRulesClick(tournament.tournamentId) },
                                onShareClick = {},
                                onToggleSave = {}
                            )
                        }
                    }
                }
            }
        }

        if (state.isFilterSheetVisible) {
            FiltersSheet(
                currentFilters = state.filters,
                onApply = { viewModel.applyNewFilters(it) },
                onReset = { viewModel.clearFilters() },
                onDismiss = { viewModel.setFilterSheetVisible(false) }
            )
        }
    }
}
