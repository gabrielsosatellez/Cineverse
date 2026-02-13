package com.project.cineversemobile.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.cineversemobile.Data.Session
import com.project.cineversemobile.Data.Ticket
import com.project.cineversemobile.Navigation.Routes
import com.project.cineversemobile.Util.BottomBarApp
import com.project.cineversemobile.Util.TopBarApp
import com.project.cineversemobile.ViewModels.MyTicketsViewModel
import com.project.cineversemobile.ViewModels.UserViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTicketsScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    myTicketsViewModel: MyTicketsViewModel
) {
    // Estado reactivo de los tickets del usuario y del proceso de carga
    val tickets by myTicketsViewModel.tickets.collectAsState()
    val isLoading by myTicketsViewModel.isLoading.collectAsState()

    // Estado del menú lateral (drawer)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val darkColor = Color(0xFF0F172A)
    val lightText = Color.White

    // Usuario autenticado
    val user by userViewModel.loggedUser.observeAsState()

    // Agrupa los tickets por sesión y los ordena por número de asiento
    val groupedTickets = remember(tickets) {
        tickets
            .groupBy { it.session.id }
            .mapValues { entry ->
                entry.value.sortedBy { it.seatNumber }
            }
    }

    // Carga las entradas del usuario cuando se dispone de su identificador
    LaunchedEffect(user?.id) {
        user?.id?.let { myTicketsViewModel.loadUserTickets(it) }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = darkColor) {

                Spacer(Modifier.height(24.dp))

                Text(
                    text = "CineVerse",
                    color = lightText,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(24.dp))
                Divider(color = lightText.copy(alpha = 0.2f))

                DrawerItem(text = "Películas") { navController.navigate(Routes.Main.route) }
                DrawerItem(text = "Comprar tickets") { navController.navigate(Routes.Session.route) }
                DrawerItem(text = "Mis tickets") { navController.navigate(Routes.MyTickets.route) }

                Spacer(Modifier.height(24.dp))
                Divider(color = lightText.copy(alpha = 0.2f))

                DrawerItem(
                    text = "Cerrar sesión",
                    onClick = {
                        userViewModel.logout()
                        navController.navigate(Routes.Login.route) {
                            popUpTo(Routes.Main.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopBarApp(navController = navController, showBack = true, showHome = true)
            },
            bottomBar = {
                BottomBarApp(
                    fullName = user?.fullName ?: "Usuario",
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
        ) { padding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Gestión de estados de carga, lista vacía y contenido
                when {
                    isLoading -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }

                    tickets.isEmpty() -> {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No tienes tickets comprados aún")
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            groupedTickets.forEach { (_, sessionTickets) ->
                                val session = sessionTickets.first().session

                                item {
                                    ExpandableSessionCard(
                                        session = session,
                                        tickets = sessionTickets.sortedBy { it.seatNumber }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpandableSessionCard(
    session: Session,
    tickets: List<Ticket>
) {
    // Control del estado expandido/colapsado de la tarjeta
    var expanded by remember { mutableStateOf(false) }

    // Formatea la fecha de la sesión para su visualización
    val formattedDate = remember(session.dateTime) {
        try {
            val input = LocalDateTime.parse(session.dateTime)
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy · HH:mm")
            input.format(formatter)
        } catch (e: Exception) {
            session.dateTime.replace("T", " ")
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            session.movieTitle?.let { Text(it, style = MaterialTheme.typography.titleMedium) }
            Text(formattedDate, style = MaterialTheme.typography.bodyMedium)
            Text("Sala: ${session.roomName}", style = MaterialTheme.typography.bodyMedium)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (!expanded)
                        Icons.Default.KeyboardArrowDown
                    else
                        Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = if (!expanded) "Mostrar asientos" else "Ocultar asientos",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Despliegue del listado de asientos comprados para la sesión
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    tickets.forEach { ticket ->
                        Text("Asiento ${ticket.seatNumber}")
                    }
                }
            }
        }
    }
}