package com.project.cineversemobile.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.cineversemobile.Data.CartItem
import com.project.cineversemobile.Navigation.Routes
import com.project.cineversemobile.Util.TopBarApp
import com.project.cineversemobile.ViewModels.CartViewModel
import com.project.cineversemobile.ViewModels.SeatSelectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatSelectionScreen(
    navController: NavHostController,
    sessionId: Long,
    pricePerSeat: Double,
    seatSelectionViewModel: SeatSelectionViewModel,
    cartViewModel: CartViewModel
) {
    // Estado reactivo de la sala y los asientos
    val totalSeats by seatSelectionViewModel.totalSeats.collectAsState()
    val isLoading by seatSelectionViewModel.isLoading.collectAsState()
    val occupiedSeats by seatSelectionViewModel.occupiedSeats.collectAsState()
    val session by seatSelectionViewModel.session.collectAsState()

    // Configuración del número de asientos por fila y por lado
    val seatsPerSide = 4
    val seatsPerRow = seatsPerSide * 2

    // Asientos que ya están añadidos al carrito para esta sesión
    val cartItems by cartViewModel.items.collectAsState()
    val seatsInCart = remember(cartItems, sessionId) {
        cartItems
            .filter { it.sessionId == sessionId }
            .map { it.seatNumber }
            .toSet()
    }

    // Carga de la información de la sala y de los asientos ocupados
    LaunchedEffect(sessionId) {
        seatSelectionViewModel.loadRoom(sessionId)
    }

    when {
        isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        totalSeats == null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Cargando sala...")
            }
        }

        else -> {
            // Cálculo del número de filas necesarias en función del total de asientos
            val rows = (totalSeats!! + seatsPerRow - 1) / seatsPerRow

            // Asientos seleccionados por el usuario en la sesión actual
            var selectedSeats by remember { mutableStateOf(setOf<Int>()) }

            Scaffold(
                topBar = {
                    TopBarApp(
                        navController = navController,
                        showBack = true,
                        showHome = true
                    )
                },
                bottomBar = {
                    SeatBottomBar(
                        selectedCount = selectedSeats.size,
                        pricePerSeat = pricePerSeat,
                        onBuyClick = {
                            val s = session
                            if (s != null) {
                                val items = selectedSeats.map { seat ->
                                    CartItem(
                                        sessionId = sessionId,
                                        seatNumber = seat,
                                        price = pricePerSeat,
                                        movieTitle = s.movieTitle ?: "Película",
                                        dateTime = s.dateTime,
                                        roomName = s.roomName
                                    )
                                }

                                // Añade los asientos seleccionados al carrito
                                cartViewModel.addItems(items)
                                navController.navigate(Routes.Cart.route)
                            }
                        }
                    )
                }
            ) { padding ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Representación visual de la pantalla de cine
                    CinemaScreenHeader()

                    Spacer(Modifier.height(24.dp))

                    // Renderizado de las filas de asientos
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(rows) { rowIndex ->
                            val startSeat = rowIndex * seatsPerRow + 1

                            SeatRow(
                                startSeatNumber = startSeat,
                                seatsPerSide = seatsPerSide,
                                totalSeats = totalSeats!!,
                                selectedSeats = selectedSeats,
                                occupiedSeats = occupiedSeats,
                                seatsInCart = seatsInCart,
                                onSeatClick = { seat ->
                                    // Permite seleccionar o deseleccionar un asiento solo si no está ocupado ni en el carrito
                                    if (!occupiedSeats.contains(seat) && !seatsInCart.contains(seat)) {
                                        selectedSeats =
                                            if (selectedSeats.contains(seat))
                                                selectedSeats - seat
                                            else
                                                selectedSeats + seat
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SeatRow(
    startSeatNumber: Int,
    seatsPerSide: Int,
    totalSeats: Int,
    selectedSeats: Set<Int>,
    occupiedSeats: Set<Int>,
    seatsInCart: Set<Int>,
    onSeatClick: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Lado izquierdo de la fila
        for (i in 0 until seatsPerSide) {
            val seatNumber = startSeatNumber + i
            if (seatNumber <= totalSeats) {
                SeatItem(
                    seatNumber = seatNumber,
                    isSelected = selectedSeats.contains(seatNumber),
                    isOccupied = occupiedSeats.contains(seatNumber),
                    isInCart = seatsInCart.contains(seatNumber),
                    onClick = { onSeatClick(seatNumber) }
                )
                Spacer(Modifier.width(6.dp))
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        // Lado derecho de la fila
        for (i in 0 until seatsPerSide) {
            val seatNumber = startSeatNumber + seatsPerSide + i
            if (seatNumber <= totalSeats) {
                SeatItem(
                    seatNumber = seatNumber,
                    isSelected = selectedSeats.contains(seatNumber),
                    isOccupied = occupiedSeats.contains(seatNumber),
                    isInCart = seatsInCart.contains(seatNumber),
                    onClick = { onSeatClick(seatNumber) }
                )
                Spacer(Modifier.width(6.dp))
            }
        }
    }
}

@Composable
fun SeatItem(
    seatNumber: Int,
    isSelected: Boolean,
    isOccupied: Boolean,
    isInCart: Boolean,
    onClick: () -> Unit
) {
    // Determina el color del asiento según su estado
    val color = when {
        isOccupied -> Color(0xFFEF4444)
        isInCart -> Color(0xFFF59E0B)
        isSelected -> Color(0xFF22C55E)
        else -> Color(0xFF94A3B8)
    }

    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color)
            .clickable(enabled = !isOccupied && !isInCart) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = seatNumber.toString(),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
    }
}

@Composable
fun SeatBottomBar(
    selectedCount: Int,
    pricePerSeat: Double,
    onBuyClick: () -> Unit
) {
    // Cálculo del importe total en función del número de asientos seleccionados
    val total = selectedCount * pricePerSeat

    BottomAppBar {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
        ) {
            Text("Asientos: $selectedCount")
            Text("Total: %.2f €".format(total))
        }

        Button(
            onClick = onBuyClick,
            enabled = selectedCount > 0
        ) {
            Text("Comprar")
        }
    }
}

@Composable
fun CinemaScreenHeader() {
    // Representación visual de la pantalla del cine
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFCBD5E1)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "PANTALLA",
            style = MaterialTheme.typography.labelLarge,
            color = Color.DarkGray
        )
    }
}