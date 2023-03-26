package com.example.movieapp.ui

sealed class Screen(val route: String) {
    object MainScreen : Screen("homescreen")
    object DetailScreen : Screen("detailscreen")

    fun withArgs(vararg  args: String): String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
