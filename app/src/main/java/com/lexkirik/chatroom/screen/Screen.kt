package com.lexkirik.chatroom.screen

sealed class Screen(val route: String) {
    object LogInScreen: Screen("loginscreen")
    object SignUpScreen: Screen("signupscreen")
    object ChatRoomsScreen: Screen("chatroomsscreen")
    object ChatScreen: Screen("chatscreen")
}