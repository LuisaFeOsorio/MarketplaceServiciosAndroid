package uniquindio.marketplace2.features.mensajes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import uniquindio.marketplace2.data.modelos.Mensaje

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMensajes(
    solicitudId: String,
    tituloServicio: String,
    onBackPressed: () -> Unit,
    viewModel: MensajesViewModel = hiltViewModel()
) {
    val mensajes by viewModel.mensajes.collectAsState()
    val textoActual by viewModel.textoActual.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val usuarioId by viewModel.usuarioId.collectAsState()

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(solicitudId) {
        viewModel.cargarMensajes(solicitudId)
    }

    // Hacer scroll al último mensaje automáticamente
    LaunchedEffect(mensajes.size) {
        if (mensajes.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(mensajes.size - 1)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Mensajes", fontWeight = FontWeight.Bold)
                        Text(
                            tituloServicio,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .navigationBarsPadding()
                        .imePadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = textoActual,
                        onValueChange = { viewModel.onTextoChange(it) },
                        placeholder = { Text("Escribe un mensaje...", fontSize = 14.sp) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        maxLines = 3,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = { viewModel.enviarMensaje(solicitudId) }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    FloatingActionButton(
                        onClick = { viewModel.enviarMensaje(solicitudId) },
                        modifier = Modifier.size(48.dp),
                        containerColor = if (textoActual.isBlank())
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        else
                            MaterialTheme.colorScheme.primary,
                        elevation = FloatingActionButtonDefaults.elevation(0.dp)
                    ) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = "Enviar",
                            tint = if (textoActual.isBlank())
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            else
                                MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        if (isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        if (mensajes.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("💬", fontSize = 48.sp)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Sin mensajes aún",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Sé el primero en escribir",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
            return@Scaffold
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(mensajes, key = { it.id }) { mensaje ->
                BurbujaMensaje(
                    mensaje = mensaje,
                    esPropio = mensaje.autorId == usuarioId
                )
            }
        }
    }
}

@Composable
private fun BurbujaMensaje(
    mensaje: Mensaje,
    esPropio: Boolean
) {
    val colorBurbuja = if (esPropio)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surfaceVariant

    val colorTexto = if (esPropio)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurfaceVariant

    val colorHora = if (esPropio)
        MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
    else
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (esPropio) Alignment.End else Alignment.Start
    ) {
        // Nombre del autor (solo si no es el usuario actual)
        if (!esPropio) {
            Text(
                text = mensaje.autorNombre,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 12.dp, bottom = 2.dp)
            )
        }

        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (esPropio) 18.dp else 4.dp,
                        bottomEnd = if (esPropio) 4.dp else 18.dp
                    )
                )
                .background(colorBurbuja)
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Column {
                Text(
                    text = mensaje.texto,
                    fontSize = 14.sp,
                    color = colorTexto,
                    lineHeight = 20.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = mensaje.horaFormateada,
                    fontSize = 10.sp,
                    color = colorHora,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}
