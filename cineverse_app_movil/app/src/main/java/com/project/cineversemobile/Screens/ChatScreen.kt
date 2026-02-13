package com.project.cineversemobile.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.cineversemobile.Data.ChatMessage
import com.project.cineversemobile.Util.TopBarApp
import com.project.cineversemobile.ViewModels.ChatViewModel
import com.project.cineversemobile.ViewModels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    navController: NavHostController,
    chatViewModel: ChatViewModel,
    userViewModel: UserViewModel
) {
    // Estado reactivo de la lista de mensajes del chat
    val messages by chatViewModel.messages.collectAsState()

    var text by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Usuario autenticado
    val user by userViewModel.loggedUser.observeAsState()

    // Establece la conexión con el servidor de chat al entrar en la pantalla
    LaunchedEffect(Unit) {
        chatViewModel.connect()
    }

    // Desplaza automáticamente la lista al último mensaje recibido
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopBarApp(
                navController = navController,
                showBack = true,
                showHome = true
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAFC))
        ) {

            // Listado de mensajes del chat
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages) { msg ->
                    ChatBubble(
                        message = msg,
                        isMine = msg.sender == user?.fullName
                    )
                }
            }

            Divider()

            // Barra de entrada de texto y botón de envío de mensajes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    placeholder = { Text("Escribe un mensaje…") },
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2563EB),
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    ),
                    maxLines = 3
                )

                Spacer(Modifier.width(8.dp))

                FloatingActionButton(
                    onClick = {
                        if (text.isNotBlank()) {
                            chatViewModel.sendMessage(
                                ChatMessage(
                                    sender = user?.fullName,
                                    content = text
                                )
                            )
                            text = ""
                        }
                    },
                    containerColor = Color(0xFF2563EB),
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Enviar",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    isMine: Boolean
) {
    // Estilos visuales diferenciados para mensajes propios y ajenos
    val myGradient = Brush.horizontalGradient(
        colors = listOf(Color(0xFF2563EB), Color(0xFF1D4ED8))
    )

    val bg = if (isMine) myGradient else Brush.linearGradient(
        colors = listOf(Color(0xFFE5E7EB), Color(0xFFF1F5F9))
    )

    val textColor = if (isMine) Color.White else Color.Black
    val nameColor = if (isMine) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.6f)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .shadow(4.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(bg)
                .padding(12.dp)
        ) {
            if (!isMine) {
                Text(
                    text = message.sender ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = nameColor
                )
                Spacer(Modifier.height(2.dp))
            }

            Text(
                text = message.content,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )
        }
    }
}