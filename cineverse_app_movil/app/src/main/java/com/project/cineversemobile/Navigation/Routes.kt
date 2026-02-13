package com.project.cineversemobile.Navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Login : Routes("login")
    object Register : Routes("register")
    object Main : Routes("main")
    object Cart : Routes("cart")
    object Session : Routes("session")
    object Seats : Routes("seats/{sessionId}/{price}") {
        fun createRoute(sessionId: Long, price: Double): String {
            return "seats/$sessionId/$price"
        }
    }
    object MyTickets : Routes("my-tickets")
    object Chat : Routes("chat")
}
