package com.example.komang_week6_vp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.*
import androidx.navigation.toRoute
import com.example.komang_week6_vp.navigation.ClubProjectsRoute
import com.example.komang_week6_vp.navigation.CommunitiesRoute
import com.example.komang_week6_vp.navigation.DashboardRoute
import com.example.komang_week6_vp.navigation.ProjectDetailRoute
import com.example.komang_week6_vp.ui.screens.ClubProjectsScreen
import com.example.komang_week6_vp.ui.screens.CommunitiesScreen
import com.example.komang_week6_vp.ui.screens.DashboardScreen
import com.example.komang_week6_vp.ui.screens.ProjectDetailScreen
import com.example.komang_week6_vp.viewmodel.CampusViewModel

@Composable
fun CampusApp(viewModel: CampusViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isBottomBarVisible = currentDestination?.hasRoute<DashboardRoute>() == true ||
            currentDestination?.hasRoute<CommunitiesRoute>() == true

    Scaffold(
        bottomBar = {
            if (isBottomBarVisible) {
                NavigationBar {
                    val isDashboard = currentDestination?.hasRoute<DashboardRoute>() == true
                    NavigationBarItem(
                        selected = isDashboard,
                        onClick = {
                            navController.navigate(DashboardRoute) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text("Dashboard") }
                    )
                    val isCommunities = currentDestination?.hasRoute<CommunitiesRoute>() == true
                    NavigationBarItem(
                        selected = isCommunities,
                        onClick = {
                            navController.navigate(CommunitiesRoute) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(Icons.Default.Person, contentDescription = null) },
                        label = { Text("Communities") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DashboardRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<DashboardRoute> {
                DashboardScreen(viewModel = viewModel)
            }
            composable<CommunitiesRoute> {
                CommunitiesScreen(
                    viewModel = viewModel,
                    onNavigateToProjects = { communityId ->
                        navController.navigate(ClubProjectsRoute(communityId))
                    }
                )
            }
            composable<ClubProjectsRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<ClubProjectsRoute>()
                ClubProjectsScreen(
                    communityId = route.communityId,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetail = { projectId ->
                        navController.navigate(ProjectDetailRoute(projectId))
                    }
                )
            }
            composable<ProjectDetailRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<ProjectDetailRoute>()
                ProjectDetailScreen(
                    projectId = route.projectId,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@androidx.compose.runtime.Composable
fun PreviewDashboardScreen() {
    androidx.compose.material3.MaterialTheme {
        com.example.komang_week6_vp.ui.screens.DashboardScreen(
            viewModel = com.example.komang_week6_vp.viewmodel.CampusViewModel()
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@androidx.compose.runtime.Composable
fun PreviewCommunitiesScreen() {
    androidx.compose.material3.MaterialTheme {
        com.example.komang_week6_vp.ui.screens.CommunitiesScreen(
            viewModel = com.example.komang_week6_vp.viewmodel.CampusViewModel(),
            onNavigateToProjects = {}
        )
    }
}