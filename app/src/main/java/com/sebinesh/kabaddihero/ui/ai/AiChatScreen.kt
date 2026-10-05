package com.sebinesh.kabaddihero.ui.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sebinesh.kabaddihero.data.remote.ai.ChatMessage
import com.sebinesh.kabaddihero.ui.components.CourtBackdrop
import com.sebinesh.kabaddihero.ui.components.ErrorBanner
import com.sebinesh.kabaddihero.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatScreen(
    viewModel: AiChatViewModel,
    onBack: () -> Unit,
    onNavigateToPosterAnalyzer: () -> Unit = {},
    onNavigateToRules: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    var inputText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.initializeTts(context)
    }

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    val quickQuestions = listOf(
        "What are the Bonus point rules?",
        "When is a Super Tackle awarded?",
        "How to execute a perfect Dubki?",
        "How does a Do-or-Die raid work?",
        "What is Senior Men's weight limit?",
        "How to defend against running hand touches?",
        "How many points for an All-Out?"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PurplePrimaryBright, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Kabaddi Hero AI", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
                        }
                        Text(
                            text = if (state.isHighThinkingEnabled) "Google AI Studio • Gemini 3.1 Pro (Deep Thinking)" else if (state.isFastModeEnabled) "Google AI Studio • Gemini 3.1 Flash Lite" else "Google AI Studio • Gemini 3.5 Flash",
                            color = if (state.isHighThinkingEnabled) PurpleGold else PurplePrimaryBright,
                            fontSize = 11.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToPosterAnalyzer) {
                        Icon(Icons.Default.DocumentScanner, contentDescription = "Scan Poster", tint = PurpleGold)
                    }
                    IconButton(onClick = { viewModel.clearHistory() }) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "Clear", tint = TextTertiary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkPurpleSurface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CourtBackdrop(alpha = 0.08f)

            Column(modifier = Modifier.fillMaxSize()) {
                // Mode Toggle Bar (High Thinking & Fast Mode chips)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkPurpleSurface)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilterChip(
                        selected = state.isHighThinkingEnabled,
                        onClick = { viewModel.toggleHighThinking() },
                        leadingIcon = {
                            Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(14.dp))
                        },
                        label = { Text("High Thinking (Pro)", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PurpleGold.copy(alpha = 0.25f),
                            selectedLabelColor = PurpleGold
                        )
                    )

                    FilterChip(
                        selected = state.isFastModeEnabled,
                        onClick = { viewModel.toggleFastMode() },
                        leadingIcon = {
                            Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(14.dp))
                        },
                        label = { Text("Fast Mode (Lite)", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PurplePrimaryBright.copy(alpha = 0.25f),
                            selectedLabelColor = PurplePrimaryBright
                        )
                    )

                    SuggestionChip(
                        onClick = onNavigateToRules,
                        label = { Text("Rules Center", fontSize = 11.sp) },
                        icon = { Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(14.dp), tint = TextSecondary) }
                    )
                }

                // Chat Messages Thread
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(state.messages) { message ->
                        ChatBubble(
                            message = message,
                            onSpeak = { viewModel.speakMessage(message.text) }
                        )
                    }

                    if (state.isLoading) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = StadiumNightAlt),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            color = ElectricCyan,
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = if (state.isHighThinkingEnabled) "Analyzing official rules with High Thinking..." else "Typing answer...",
                                            fontSize = 12.sp,
                                            color = SoftSteel
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                if (state.errorMessage != null) {
                    ErrorBanner(message = state.errorMessage!!)
                }

                // Quick Question Suggestions
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(quickQuestions) { q ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { viewModel.sendMessage(q) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(text = q, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                // Input Bar
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            placeholder = { Text("Ask about Kabaddi rules, tactics, tournaments...", fontSize = 13.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("chat_input_text"),
                            shape = RoundedCornerShape(24.dp),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        FilledIconButton(
                            onClick = {
                                if (inputText.isNotBlank()) {
                                    viewModel.sendMessage(inputText)
                                    inputText = ""
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("btn_send_chat"),
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = MatClay)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    onSpeak: () -> Unit
) {
    val isUser = message.sender == "USER"

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MatClayDeep),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.SportsGymnastics, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) MatClay else StadiumNightAlt
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    color = Color.White,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                if (!isUser) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = onSpeak,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Speak TTS",
                                tint = ElectricCyan,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
