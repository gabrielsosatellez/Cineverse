package com.project.cineversemobile.Screens

import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.cineversemobile.Navigation.Routes
import com.project.cineversemobile.R
import com.project.cineversemobile.Util.MovieCard
import com.project.cineversemobile.ViewModels.MovieViewModel
import com.project.cineversemobile.ViewModels.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    movieViewModel: MovieViewModel
) {
    // Usuario autenticado
    val user by userViewModel.loggedUser.observeAsState()

    // Estado de carga del listado de películas
    val movies by movieViewModel.movies.observeAsState(emptyList())
    val isLoading by movieViewModel.isLoading.observeAsState(false)
    val error by movieViewModel.error.observeAsState()

    // Control del diálogo de confirmación para eliminar la cuenta
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Estado del menú lateral
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val darkColor = Color(0xFF0F172A)
    val lightText = Color.White

    // Recarga periódica del catálogo de películas
    LaunchedEffect(Unit) {
        while (true) {
            movieViewModel.loadMovies()
            delay(5000)
        }
    }

    user?.let { currentUser ->

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

                    // Opciones de navegación principales
                    DrawerItem(
                        text = "Películas",
                        onClick = {
                            navController.navigate(Routes.Main.route) { }
                        }
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

                    // Acciones de sesión del usuario
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
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = darkColor,
                            titleContentColor = lightText
                        ),
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(R.drawable.logo_cineverse),
                                    contentDescription = "CineVerse",
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("CineVerse", color = lightText)
                            }
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate(Routes.Cart.route) }) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Carrito",
                                    tint = lightText
                                )
                            }
                        }
                    )
                },
                bottomBar = {
                    BottomAppBar(containerColor = darkColor) {
                        Text(
                            text = currentUser.fullName,
                            color = lightText,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Spacer(Modifier.weight(1f))
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = lightText
                            )
                        }
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navController.navigate(Routes.Chat.route) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "Soporte",
                            tint = Color.White
                        )
                    }
                }
            ) { padding ->

                // Gestión de estados de carga, error y contenido
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    error != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(error!!)
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            items(movies) { movie ->
                                MovieCard(movie)
                            }
                        }
                    }
                }
            }
        }

        // Diálogo de confirmación para la eliminación de la cuenta
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

@Composable
fun DrawerItem(
    text: String,
    onClick: () -> Unit = {}
) {
    // Elemento reutilizable del menú lateral de navegación
    Text(
        text = text,
        color = Color.White,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 20.dp)
    )
}
