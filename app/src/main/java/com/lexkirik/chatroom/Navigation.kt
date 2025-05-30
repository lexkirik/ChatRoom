package com.lexkirik.chatroom

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lexkirik.chatroom.screen.ChatRoomListScreen
import com.lexkirik.chatroom.screen.ChatScreen
import com.lexkirik.chatroom.screen.LogInScreen
import com.lexkirik.chatroom.screen.Screen
import com.lexkirik.chatroom.screen.SignUpScreen
import com.lexkirik.chatroom.viewmodel.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SignUpScreen.route
    ) {
        composable(Screen.SignUpScreen.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogIn = {
                navController.navigate(Screen.LogInScreen.route)
            })
        }

        composable(Screen.LogInScreen.route) {
            LogInScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate(Screen.SignUpScreen.route) },
                onSignInSuccess = {}
            )
            navController.navigate(Screen.ChatRoomsScreen.route)
        }

        composable(Screen.ChatRoomsScreen.route) {
            ChatRoomListScreen{
                navController.navigate("${Screen.ChatScreen.route}/${it.id}")
            }
        }

        composable("${Screen.ChatScreen.route}/{roomID}") {
            val roomID: String = it
                .arguments?.getString("roomID") ?: ""
            ChatScreen(roomID = roomID)
        }
    }
}