package com.sebinesh.kabaddihero.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.sebinesh.kabaddihero.core.di.ServiceLocator
import com.sebinesh.kabaddihero.ui.admin.AdminDashboard
import com.sebinesh.kabaddihero.ui.ai.*
import com.sebinesh.kabaddihero.ui.auth.*
import com.sebinesh.kabaddihero.ui.explore.*
import com.sebinesh.kabaddihero.ui.home.*
import com.sebinesh.kabaddihero.ui.live.*
import com.sebinesh.kabaddihero.ui.notifications.NotificationsScreen
import com.sebinesh.kabaddihero.ui.onboarding.OnboardingScreen
import com.sebinesh.kabaddihero.ui.player.PlayerProfileScreen
import com.sebinesh.kabaddihero.ui.profile.ProfileScreen
import com.sebinesh.kabaddihero.ui.profile.SettingsScreen
import com.sebinesh.kabaddihero.ui.saved.SavedScreen
import com.sebinesh.kabaddihero.ui.splash.SplashScreen
import com.sebinesh.kabaddihero.ui.team.TeamProfileScreen
import com.sebinesh.kabaddihero.ui.theme.*
import com.sebinesh.kabaddihero.ui.tournament.TournamentDetailScreen
import com.sebinesh.kabaddihero.ui.tournament.TournamentDetailViewModel
import com.sebinesh.kabaddihero.ui.tournament.create.CreateTournamentFlow
import com.sebinesh.kabaddihero.ui.tournament.create.CreateTournamentViewModel
import com.sebinesh.kabaddihero.ui.tournament.rules.RulesCenterScreen
import com.sebinesh.kabaddihero.ui.tournament.rules.TournamentRulesScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var showCreateOptionsDialog by remember { mutableStateOf(false) }

    val bottomBarRoutes = listOf(
        Screen.Home.route,
        Screen.Explore.route,
        Screen.Live.route,
        Screen.Profile.route
    )

    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = DarkPurpleSurface,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        selected = currentRoute == Screen.Home.route,
                        onClick = { navController.navigate(Screen.Home.route) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PurplePrimaryBright,
                            selectedTextColor = PurplePrimaryBright,
                            unselectedIconColor = TextTertiary,
                            unselectedTextColor = TextTertiary,
                            indicatorColor = PurpleContainer
                        ),
                        modifier = Modifier.testTag("nav_home")
                    )

                    NavigationBarItem(
                        selected = currentRoute == Screen.Explore.route,
                        onClick = { navController.navigate(Screen.Explore.route) },
                        icon = { Icon(Icons.Default.Search, contentDescription = "Explore") },
                        label = { Text("Tournaments", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PurplePrimaryBright,
                            selectedTextColor = PurplePrimaryBright,
                            unselectedIconColor = TextTertiary,
                            unselectedTextColor = TextTertiary,
                            indicatorColor = PurpleContainer
                        ),
                        modifier = Modifier.testTag("nav_explore")
                    )

                    // Visually Prominent Create Button in Center
                    NavigationBarItem(
                        selected = false,
                        onClick = { showCreateOptionsDialog = true },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(androidx.compose.ui.graphics.Brush.linearGradient(listOf(PurplePrimary, PurpleAccent)), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Create", tint = Color.White, modifier = Modifier.size(24.dp))
                            }
                        },
                        label = { Text("Create", fontSize = 11.sp, color = PurplePrimaryBright, fontWeight = FontWeight.Bold) },
                        modifier = Modifier.testTag("nav_create_prominent")
                    )

                    NavigationBarItem(
                        selected = currentRoute == Screen.Live.route,
                        onClick = { navController.navigate(Screen.Live.route) },
                        icon = { Icon(Icons.Default.Scoreboard, contentDescription = "Live") },
                        label = { Text("Live", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PurplePrimaryBright,
                            selectedTextColor = PurplePrimaryBright,
                            unselectedIconColor = TextTertiary,
                            unselectedTextColor = TextTertiary,
                            indicatorColor = PurpleContainer
                        ),
                        modifier = Modifier.testTag("nav_live")
                    )

                    NavigationBarItem(
                        selected = currentRoute == Screen.Profile.route,
                        onClick = { navController.navigate(Screen.Profile.route) },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile", fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PurplePrimaryBright,
                            selectedTextColor = PurplePrimaryBright,
                            unselectedIconColor = TextTertiary,
                            unselectedTextColor = TextTertiary,
                            indicatorColor = PurpleContainer
                        ),
                        modifier = Modifier.testTag("nav_profile")
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onAnimationComplete = {
                        val isLoggedIn = ServiceLocator.authRepository.isUserLoggedIn()
                        val destination = if (isLoggedIn) Screen.Home.route else Screen.Login.route
                        navController.navigate(destination) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    onFinish = {
                        val isLoggedIn = ServiceLocator.authRepository.isUserLoggedIn()
                        val destination = if (isLoggedIn) Screen.Home.route else Screen.Login.route
                        navController.navigate(destination) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                    onNavigateToPhoneAuth = { navController.navigate("phone_auth") },
                    onGuestBrowse = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Register.route) { inclusive = true }
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.navigate(Screen.Login.route) }
                )
            }

            composable("phone_auth") {
                PhoneAuthScreen(
                    onVerificationSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo("phone_auth") { inclusive = true }
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Home.route) {
                val homeVm: HomeViewModel = viewModel()
                HomeScreen(
                    viewModel = homeVm,
                    onTournamentClick = { id -> navController.navigate(Screen.TournamentDetail.createRoute(id)) },
                    onViewRulesClick = { id -> navController.navigate(Screen.TournamentRules.createRoute(id)) },
                    onRegisterClick = { id -> navController.navigate(Screen.TournamentDetail.createRoute(id)) },
                    onLiveMatchClick = { id -> navController.navigate(Screen.LiveScoreboard.createRoute(id)) },
                    onAiChatClick = { navController.navigate(Screen.AiChat.route) },
                    onNotificationsClick = { navController.navigate(Screen.Notifications.route) },
                    onProfileClick = { navController.navigate(Screen.Profile.route) },
                    onExploreClick = { navController.navigate(Screen.Explore.route) },
                    onCreateTournamentClick = { navController.navigate(Screen.CreateTournament.route) }
                )
            }

            composable(Screen.Explore.route) {
                val exploreVm: ExploreViewModel = viewModel()
                ExploreScreen(
                    viewModel = exploreVm,
                    onTournamentClick = { id -> navController.navigate(Screen.TournamentDetail.createRoute(id)) },
                    onRegisterClick = { id -> navController.navigate(Screen.TournamentDetail.createRoute(id)) },
                    onViewRulesClick = { id -> navController.navigate(Screen.TournamentRules.createRoute(id)) }
                )
            }

            composable(Screen.Live.route) {
                LiveListScreen(
                    onMatchClick = { id -> navController.navigate(Screen.LiveScoreboard.createRoute(id)) },
                    onScorerClick = { id -> navController.navigate(Screen.Scorer.createRoute(id)) }
                )
            }

            composable(Screen.CreateTournament.route) {
                val createVm: CreateTournamentViewModel = viewModel()
                CreateTournamentFlow(
                    viewModel = createVm,
                    onFinished = { id -> navController.navigate(Screen.TournamentDetail.createRoute(id)) },
                    onCancel = { navController.popBackStack() },
                    onNavigateToPosterAnalyzer = { navController.navigate(Screen.PosterAnalyzer.route) }
                )
            }

            composable(
                route = Screen.TournamentDetail.route,
                arguments = listOf(navArgument("tournamentId") { type = NavType.StringType })
            ) { backStack ->
                val id = backStack.arguments?.getString("tournamentId") ?: ""
                val detailVm = remember(id) { TournamentDetailViewModel(id) }
                TournamentDetailScreen(
                    viewModel = detailVm,
                    onBack = { navController.popBackStack() },
                    onViewRules = { tId -> navController.navigate(Screen.TournamentRules.createRoute(tId)) },
                    onCheckEligibility = { tId -> navController.navigate(Screen.EligibilityChecker.createRoute(tId)) }
                )
            }

            composable(
                route = Screen.TournamentRules.route,
                arguments = listOf(navArgument("tournamentId") { type = NavType.StringType })
            ) { backStack ->
                val id = backStack.arguments?.getString("tournamentId") ?: ""
                TournamentRulesScreen(
                    tournamentId = id,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.RulesCenter.route) {
                RulesCenterScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.AiChat.route) {
                val chatVm: AiChatViewModel = viewModel()
                AiChatScreen(
                    viewModel = chatVm,
                    onBack = { navController.popBackStack() },
                    onNavigateToPosterAnalyzer = { navController.navigate(Screen.PosterAnalyzer.route) },
                    onNavigateToRules = { navController.navigate(Screen.RulesCenter.route) }
                )
            }

            composable(
                route = Screen.EligibilityChecker.route,
                arguments = listOf(navArgument("tournamentId") { type = NavType.StringType })
            ) { backStack ->
                val id = backStack.arguments?.getString("tournamentId") ?: ""
                EligibilityScreen(
                    tournamentId = id,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.PosterAnalyzer.route) {
                PosterAnalyzerScreen(
                    onBack = { navController.popBackStack() },
                    onUseExtractedData = {
                        navController.navigate(Screen.CreateTournament.route)
                    }
                )
            }

            composable(
                route = Screen.LiveScoreboard.route,
                arguments = listOf(navArgument("matchId") { type = NavType.StringType })
            ) { backStack ->
                val id = backStack.arguments?.getString("matchId") ?: ""
                val scoreVm = remember(id) { ScoreboardViewModel(id) }
                LiveScoreboardScreen(
                    viewModel = scoreVm,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.Scorer.route,
                arguments = listOf(navArgument("matchId") { type = NavType.StringType })
            ) { backStack ->
                val id = backStack.arguments?.getString("matchId") ?: ""
                val scoreVm = remember(id) { ScoreboardViewModel(id) }
                ScorerScreen(
                    viewModel = scoreVm,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.PlayerProfile.route,
                arguments = listOf(navArgument("playerId") { type = NavType.StringType })
            ) { backStack ->
                val id = backStack.arguments?.getString("playerId") ?: ""
                PlayerProfileScreen(
                    playerId = id,
                    onBack = { navController.popBackStack() },
                    onEdit = {}
                )
            }

            composable(
                route = Screen.TeamProfile.route,
                arguments = listOf(navArgument("teamId") { type = NavType.StringType })
            ) { backStack ->
                val id = backStack.arguments?.getString("teamId") ?: ""
                TeamProfileScreen(
                    teamId = id,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Notifications.route) {
                NotificationsScreen(
                    onBack = { navController.popBackStack() },
                    onNotificationClick = { targetId, type ->
                        if (!targetId.isNullOrBlank()) {
                            if (type == "MATCH") navController.navigate(Screen.LiveScoreboard.createRoute(targetId))
                            else navController.navigate(Screen.TournamentDetail.createRoute(targetId))
                        }
                    }
                )
            }

            composable(Screen.Saved.route) {
                SavedScreen(
                    onBack = { navController.popBackStack() },
                    onTournamentClick = { id -> navController.navigate(Screen.TournamentDetail.createRoute(id)) }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                    onNavigateToSaved = { navController.navigate(Screen.Saved.route) },
                    onNavigateToRules = { navController.navigate(Screen.RulesCenter.route) },
                    onNavigateToAdmin = { navController.navigate(Screen.AdminDashboard.route) },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.AdminDashboard.route) {
                AdminDashboard(onBack = { navController.popBackStack() })
            }
        }

        // Prominent Create Dialog
        if (showCreateOptionsDialog) {
            AlertDialog(
                onDismissRequest = { showCreateOptionsDialog = false },
                title = { Text("Create or Start Match", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("START A TOURNAMENT / CREATE EVENT", fontSize = 12.sp, color = PurplePrimaryBright, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedButton(
                             onClick = {
                                 showCreateOptionsDialog = false
                                 navController.navigate(Screen.CreateTournament.route)
                             },
                             modifier = Modifier.fillMaxWidth()
                         ) {
                             Icon(Icons.Default.AddCircle, contentDescription = null, tint = PurplePrimary)
                             Spacer(modifier = Modifier.width(8.dp))
                             Text("Create Tournament")
                         }

                        OutlinedButton(
                             onClick = {
                                 showCreateOptionsDialog = false
                                 navController.navigate(Screen.PosterAnalyzer.route)
                             },
                             modifier = Modifier.fillMaxWidth()
                         ) {
                             Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PurpleGold)
                             Spacer(modifier = Modifier.width(8.dp))
                             Text("Import from Poster (AI)")
                         }

                        OutlinedButton(
                             onClick = {
                                 showCreateOptionsDialog = false
                                 navController.navigate(Screen.Live.route)
                             },
                             modifier = Modifier.fillMaxWidth()
                         ) {
                             Icon(Icons.Default.SportsScore, contentDescription = null, tint = PurplePrimaryBright)
                             Spacer(modifier = Modifier.width(8.dp))
                             Text("Start Live Match Scoring")
                         }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showCreateOptionsDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}
