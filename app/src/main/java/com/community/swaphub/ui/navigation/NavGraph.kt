package com.community.swaphub.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.community.swaphub.ui.screens.admin.AdminDashboardScreen
import com.community.swaphub.ui.screens.auth.LoginScreen
import com.community.swaphub.ui.screens.auth.RegisterScreen
import com.community.swaphub.ui.screens.chat.ChatScreen
import com.community.swaphub.ui.screens.home.HomeScreen
import com.community.swaphub.ui.screens.itemdetail.ItemDetailScreen
import com.community.swaphub.ui.screens.post.PostItemScreen
import com.community.swaphub.ui.screens.profile.ProfileScreen
import com.community.swaphub.viewmodel.AuthViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object ItemDetail : Screen("item_detail/{itemId}") {
        fun createRoute(itemId: String) = "item_detail/$itemId" // UUID as String
    }
    object PostItem : Screen("post_item")
    object Profile : Screen("profile")
    object Chat : Screen("chat/{otherUserId}/{currentUserId}/{itemId}") {
        fun createRoute(otherUserId: String, currentUserId: String, itemId: String? = null) =
            "chat/$otherUserId/$currentUserId/${itemId ?: ""}" // UUID as String
    }
    object Admin : Screen("admin")
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val currentUser by authViewModel.currentUser.collectAsState()
    val isLoggedIn = currentUser != null
    
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onItemClick = { itemId ->
                    navController.navigate(Screen.ItemDetail.createRoute(itemId))
                },
                onPostItemClick = {
                    navController.navigate(Screen.PostItem.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }
        
        composable(
            route = Screen.ItemDetail.route,
            arguments = listOf(
                androidx.navigation.navArgument("itemId") {
                    type = androidx.navigation.NavType.StringType // UUID as String
                }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            val userId = currentUser?.id ?: ""
            
            ItemDetailScreen(
                itemId = itemId,
                onBackClick = { navController.popBackStack() },
                onChatClick = { otherUserId ->
                    navController.navigate(Screen.Chat.createRoute(otherUserId, userId, itemId))
                },
                onSwapClick = { swapItemId ->
                    // Handle swap request
                }
            )
        }
        
        composable(Screen.PostItem.route) {
            PostItemScreen(
                onBackClick = { navController.popBackStack() },
                onPostSuccess = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onItemClick = { itemId ->
                    navController.navigate(Screen.ItemDetail.createRoute(itemId))
                }
            )
        }
        
        composable(
            route = Screen.Chat.route,
            arguments = listOf(
                androidx.navigation.navArgument("otherUserId") {
                    type = androidx.navigation.NavType.StringType // UUID as String
                },
                androidx.navigation.navArgument("currentUserId") {
                    type = androidx.navigation.NavType.StringType // UUID as String
                },
                androidx.navigation.navArgument("itemId") {
                    type = androidx.navigation.NavType.StringType // UUID as String
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val otherUserId = backStackEntry.arguments?.getString("otherUserId") ?: ""
            val currentUserId = backStackEntry.arguments?.getString("currentUserId") ?: ""
            val itemId = backStackEntry.arguments?.getString("itemId")
            
            ChatScreen(
                otherUserId = otherUserId,
                currentUserId = currentUserId,
                itemId = itemId,
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Admin.route) {
            AdminDashboardScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

