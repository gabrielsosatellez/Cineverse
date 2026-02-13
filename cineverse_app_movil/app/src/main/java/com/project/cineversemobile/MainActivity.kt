package com.project.cineversemobile

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.project.cineversemobile.Navigation.Navigation
import com.project.cineversemobile.ViewModels.CartViewModel
import com.project.cineversemobile.ViewModels.ChatViewModel
import com.project.cineversemobile.ui.theme.CineverseMobileTheme
import kotlin.getValue
import com.project.cineversemobile.ViewModels.UserViewModel
import com.project.cineversemobile.ViewModels.MovieViewModel
import com.project.cineversemobile.ViewModels.MyTicketsViewModel
import com.project.cineversemobile.ViewModels.SeatSelectionViewModel
import com.project.cineversemobile.ViewModels.SessionViewModel

class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private val movieViewModel: MovieViewModel by viewModels()
    private val sessionViewModel: SessionViewModel by viewModels()
    private val seatSelectionViewModel: SeatSelectionViewModel by viewModels();
    private val myTicketsViewModel: MyTicketsViewModel by viewModels();
    private val chatViewModel: ChatViewModel by viewModels();

    private val cartViewModel: CartViewModel by viewModels();

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CineverseMobileTheme {
                Navigation(
                    userViewModel,
                    movieViewModel,
                    sessionViewModel,
                    seatSelectionViewModel,
                    cartViewModel,
                    myTicketsViewModel,
                    chatViewModel
                )
            }
        }
    }
}

