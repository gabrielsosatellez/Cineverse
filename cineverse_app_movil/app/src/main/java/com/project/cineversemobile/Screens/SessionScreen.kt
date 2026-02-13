package com.project.cineversemobile.Screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.cineversemobile.Data.Session
import com.project.cineversemobile.Navigation.Routes
import com.project.cineversemobile.Util.BottomBarApp
import com.project.cineversemobile.Util.TopBarApp
import com.project.cineversemobile.ViewModels.SessionViewModel
import com.project.cineversemobile.ViewModels.UserViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    sessionViewModel: SessionViewModel
) {
    // Estado reactivo del listado de sesiones y del proceso de carga
    val sessions by sessionViewModel.sessions.collectAsState()
    val isLoading by sessionViewModel.isLoading.collectAsState()

    // Estado del menú lateral (drawer)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Estados de UI para el filtrado por película y confirmación de eliminación de cuenta
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedMovie by remember { mutableStateOf<String?>(null) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val darkColor = Color(0xFF0F172A)
    val lightText = Color.White

    // Usuario autenticado
    val user by userViewModel.loggedUser.observeAsState()

    // Obtiene la lista única de títulos de películas disponibles en las sesiones
    val uniqueMovies = remember(sessions) {
        sessions.mapNotNull { it.movieTitle }.distinct().sorted()
    }

    // Filtra las sesiones en función de la película seleccionada
    val filteredSessions = remember(sessions, selectedMovie) {
        if (selectedMovie == null) {
            sessions
        } else {
            sessions.filter { it.movieTitle == selectedMovie }
        }
    }

    // Carga inicial del listado de sesiones desde el backend
    LaunchedEffect(Unit) {
        sessionViewModel.loadAllSessions()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = darkColor
            ) {
                Spacer(Modifier.height(24.dp))

                Text(
                    text = "CineVerse",
                    color = lightText,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(24.dp))

                Divider(color = lightText.copy(alpha = 0.2f))

                DrawerItem(
                    text = "Películas",
                    onClick = { navController.navigate(Routes.Main.route) }
                )
                DrawerItem(
                    text = "Comprar tickets",
                    onClick = {
                        navController.navigate(Routes.Session.route) { }
                    }
                )
                DrawerItem(
                    text = "Mis tickets",
                    onClick = { navController.navigate(Routes.MyTickets.route) }
                )

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

                DrawerItem(
                    text = "Eliminar cuenta",
                    onClick = { showDeleteDialog = true }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopBarApp(
                    navController = navController,
                    showBack = true,
                    showHome = true
                )
            },
            bottomBar = {
                BottomBarApp(
                    fullName = user!!.fullName,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                // Filtro desplegable de películas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { dropdownExpanded = !dropdownExpanded }
                            .padding(12.dp)
                    ) {
                        Text(
                            text = selectedMovie ?: "Selecciona una película",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .fillMaxWidth(0.9f)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }

                    DropdownMenu(
                        expanded = dropdownExpanded,
                        onDismissRequest = { dropdownExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        // Opción para mostrar todas las películas
                        DropdownMenuItem(
                            text = { Text("Todas las películas") },
                            onClick = {
                                selectedMovie = null
                                dropdownExpanded = false
                            }
                        )

                        // Opciones de filtrado por película
                        uniqueMovies.forEach { movie ->
                            DropdownMenuItem(
                                text = { Text(movie) },
                                onClick = {
                                    selectedMovie = movie
                                    dropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Renderizado de la lista de sesiones filtradas
                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredSessions) { session ->
                            SessionItem(
                                session = session,
                                onClick = {
                                    navController.navigate(
                                        Routes.Seats.createRoute(session.id, session.price)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        // Diálogo de confirmación para eliminación de la cuenta de usuario
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar cuenta") },
                text = {
                    Text("¿Seguro que quieres eliminar tu cuenta? Esta acción no se puede deshacer.")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            userViewModel.deleteAccount {
                                navController.navigate(Routes.Login.route) {
                                    popUpTo(Routes.Main.route) { inclusive = true }
                                }
                            }
                        }
                    ) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SessionItem(
    session: Session,
    onClick: () -> Unit
) {
    // Formatea la fecha de la sesión para su presentación en la interfaz
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
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                text = session.movieTitle ?: "Película",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Sala: ${session.roomName ?: "—"}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Precio: ${session.price} €",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
