package com.prathi.skillexchange.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.prathi.skillexchange.repository.AuthRepository
import com.prathi.skillexchange.ui.screens.AuthScreen
import com.prathi.skillexchange.ui.screens.ChatScreen
import com.prathi.skillexchange.ui.screens.HomeScreen
import com.prathi.skillexchange.ui.screens.ProfileScreen
import com.prathi.skillexchange.ui.screens.SwapScreen

@Composable
fun AppNavigation() {

    val navController =
        rememberNavController()

    val authRepository =
        AuthRepository()

    val startDest =

        if (authRepository.isUserLoggedIn()) {
            "home"
        } else {
            "auth"
        }

    NavHost(

        navController = navController,

        startDestination = startDest
    ) {

        composable("auth") {

            AuthScreen(

                onNavigateToHome = {

                    navController.navigate("home") {

                        popUpTo("auth") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("home") {

            HomeScreen(

                navController = navController,

                onNavigateToProfile = {

                    navController.navigate("profile")
                },

                onNavigateToChat = { chatData ->

                    navController.navigate(
                        "chat/$chatData"
                    )
                }
            )
        }

        composable("swap") {

            SwapScreen(

                onNavigateHome = {

                    navController.navigate("home")
                },

                onNavigateChat = {

                    navController.navigate("chat")
                },

                onNavigateProfile = {

                    navController.navigate("profile")
                }
            )
        }

        // GENERAL CHAT SCREEN
        composable("chat") {

            ChatScreen(

                otherUserId = "",

                userName = "Community Chat",

                onNavigateBack = {

                    navController.popBackStack()
                },

                onNavigateHome = {

                    navController.navigate("home")
                },

                onNavigateSwap = {

                    navController.navigate("swap")
                },

                onNavigateProfile = {

                    navController.navigate("profile")
                }
            )
        }

        // REAL USER CHAT
        composable(

            route =
                "chat/{otherUserId}/{userName}",

            arguments = listOf(

                navArgument("otherUserId") {
                    type = NavType.StringType
                },

                navArgument("userName") {
                    type = NavType.StringType
                }
            )

        ) { backStackEntry ->

            val otherUserId =

                backStackEntry.arguments
                    ?.getString("otherUserId")
                    ?: ""

            val userName =

                backStackEntry.arguments
                    ?.getString("userName")
                    ?: "User"

            ChatScreen(

                otherUserId = otherUserId,

                userName = userName,

                onNavigateBack = {

                    navController.popBackStack()
                },

                onNavigateHome = {

                    navController.navigate("home")
                },

                onNavigateSwap = {

                    navController.navigate("swap")
                },

                onNavigateProfile = {

                    navController.navigate("profile")
                }
            )
        }

        composable("profile") {

            ProfileScreen(

                onBack = {

                    navController.popBackStack()
                },

                onLogout = {

                    navController.navigate("auth") {

                        popUpTo(0)
                    }
                }
            )
        }
    }
}