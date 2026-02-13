package com.project.cineversemobile.Screens

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
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
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.cineversemobile.Data.CartItem
import com.project.cineversemobile.Navigation.Routes
import com.project.cineversemobile.Util.BottomBarApp
import com.project.cineversemobile.Util.TopBarApp
import com.project.cineversemobile.Util.generateTicketsPdf
import com.project.cineversemobile.ViewModels.CartViewModel
import com.project.cineversemobile.ViewModels.UserViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    userViewModel: UserViewModel,
    cartViewModel: CartViewModel
) {
    // Estado reactivo del carrito de compra
    val items by cartViewModel.items.collectAsState()

    val context = LocalContext.current

    // Estado del proceso de pago y posibles errores
    val isPaying by cartViewModel.isPaying.collectAsState()
    val error by cartViewModel.error.collectAsState()

    // Estado del menú lateral (drawer)
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Control del diálogo de confirmación para eliminar la cuenta
    var showDeleteDialog by remember { mutableStateOf(false) }

    val darkColor = Color(0xFF0F172A)
    val lightText = Color.White

    // Usuario autenticado obtenido desde el ViewModel
    val user by userViewModel.loggedUser.observeAsState()

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
                    onClick = { navController.navigate(Routes.Session.route) }
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
                    fullName = user?.fullName ?: "Usuario",
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {

                Text(
                    text = "Carrito",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.height(12.dp))

                // Estado de carrito vacío
                if (items.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Tu carrito está vacío")
                    }
                } else {
                    // Listado de elementos del carrito
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(items) { item ->
                            CartItemRow(
                                item = item,
                                onRemove = { cartViewModel.removeItem(item) }
                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    val total = items.sumOf { it.price }

                    // Acción de pago de entradas
                    Button(
                        onClick = {
                            val purchasedItems = items.toList()

                            cartViewModel.pay(userEmail = user!!.email) {

                                val uri = generateTicketsPdf(
                                    context = context,
                                    items = purchasedItems,
                                    userFullName = user!!.fullName
                                )

                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(uri, "application/pdf")
                                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                }
                                context.startActivity(
                                    Intent.createChooser(intent, "Abrir tickets en PDF")
                                )

                                navController.navigate(Routes.Main.route) {
                                    popUpTo(Routes.Cart.route) { inclusive = true }
                                }
                            }
                        },
                        enabled = items.isNotEmpty() && !isPaying,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isPaying) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        } else {
                            Text("Pagar ahora · %.2f €".format(total))
                        }
                    }

                    // Visualización de errores en el proceso de pago
                    if (error != null) {
                        Text(
                            text = error!!,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }

    // Diálogo de confirmación para la eliminación de la cuenta del usuario
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CartItemRow(
    item: CartItem,
    onRemove: () -> Unit
) {
    // Formatea la fecha de la sesión para su visualización en el carrito
    val formattedDate = remember(item.dateTime) {
        try {
            val input = LocalDateTime.parse(item.dateTime)
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy · HH:mm")
            input.format(formatter)
        } catch (e: Exception) {
            item.dateTime.replace("T", " ")
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.movieTitle, style = MaterialTheme.typography.titleMedium)
                Text(text = formattedDate)
                Text(text = "Sala: ${item.roomName ?: "—"}")
                Text(text = "Asiento ${item.seatNumber} · %.2f €".format(item.price))
            }

            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}
