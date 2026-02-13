package com.project.cineversemobile.Navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.project.cineversemobile.Screens.CartScreen
import com.project.cineversemobile.Screens.ChatScreen
import com.project.cineversemobile.Screens.LoginScreen
import com.project.cineversemobile.Screens.MainScreen
import com.project.cineversemobile.Screens.MyTicketsScreen
import com.project.cineversemobile.Screens.RegisterScreen
import com.project.cineversemobile.Screens.SeatSelectionScreen
import com.project.cineversemobile.Screens.SessionScreen
import com.project.cineversemobile.Screens.SplashScreen
import com.project.cineversemobile.ViewModels.CartViewModel
import com.project.cineversemobile.ViewModels.ChatViewModel
import com.project.cineversemobile.ViewModels.MovieViewModel
import com.project.cineversemobile.ViewModels.MyTicketsViewModel
import com.project.cineversemobile.ViewModels.SeatSelectionViewModel
import com.project.cineversemobile.ViewModels.SessionViewModel
import com.project.cineversemobile.ViewModels.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(
    userViewModel: UserViewModel,
    movieViewModel: MovieViewModel,
    sessionViewModel: SessionViewModel,
    seatSelectionViewModel: SeatSelectionViewModel,
    cartViewModel: CartViewModel,
    myTicketsViewModel: MyTicketsViewModel,
    chatViewModel: ChatViewModel
) {
    // Controlador de navegación de la aplicación
    val navController = rememberNavController()

    // Definición del grafo de navegación principal
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        // Pantalla de splash inicial
        composable(Routes.Splash.route) {
            SplashScreen(navController, userViewModel)
        }

        // Pantalla de inicio de sesión
        composable(Routes.Login.route) {
            LoginScreen(navController, userViewModel)
        }

        // Pantalla de registro de nuevos usuarios
        composable(Routes.Register.route) {
            RegisterScreen(navController, userViewModel)
        }

        // Pantalla principal con el listado de películas
        composable(Routes.Main.route) {
            MainScreen(navController, userViewModel, movieViewModel)
        }

        // Pantalla de selección de sesión de una película
        composable(Routes.Session.route) {
            SessionScreen(navController, userViewModel, sessionViewModel)
        }

        // Pantalla de selección de asientos para una sesión concreta
        composable(
            route = Routes.Seats.route,
            arguments = listOf(
                navArgument("sessionId") { type = NavType.LongType },
                navArgument("price") { type = NavType.FloatType }
            )
        ) { backStackEntry ->

            val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L
            val price = backStackEntry.arguments?.getFloat("price")?.toDouble() ?: 0.0

            SeatSelectionScreen(
                navController = navController,
                sessionId = sessionId,
                pricePerSeat = price,
                seatSelectionViewModel = seatSelectionViewModel,
                cartViewModel = cartViewModel
            )
        }

        // Pantalla del carrito de compra de entradas
        composable(Routes.Cart.route) {
            CartScreen(
                navController = navController,
                userViewModel = userViewModel,
                cartViewModel = cartViewModel
            )
        }

        // Pantalla de consulta de entradas del usuario
        composable(Routes.MyTickets.route) {
            MyTicketsScreen(
                navController = navController,
                userViewModel = userViewModel,
                myTicketsViewModel = myTicketsViewModel
            )
        }

        // Pantalla de chat de soporte dentro de la aplicación móvil
        composable(Routes.Chat.route) {
            ChatScreen(
                navController = navController,
                chatViewModel = chatViewModel,
                userViewModel = userViewModel
            )
        }
    }
}