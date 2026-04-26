package uniquindio.marketplace2.features.notificaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uniquindio.marketplace2.R
import uniquindio.marketplace2.data.modelos.Notificacion
import uniquindio.marketplace2.data.modelos.TipoNotificacion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaNotificaciones(
    onBackPressed: () -> Unit,
    onNotificacionClick: (String, String) -> Unit = { _, _ -> },
    viewModel: NotificacionesViewModel = hiltViewModel()
) {
    val notificaciones by viewModel.notificaciones.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val noLeidas by viewModel.noLeidas.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.notificaciones_titulo)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.volver))
                    }
                },
                actions = {
                    if (noLeidas > 0) {
                        TextButton(onClick = viewModel::marcarTodasComoLeidas) {
                            Text(stringResource(R.string.notificaciones_leer_todas, noLeidas))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (notificaciones.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🔔", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(stringResource(R.string.notificaciones_vacio), fontSize = 16.sp)
                        Text(
                            text = stringResource(R.string.notificaciones_vacio_desc),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(notificaciones) { notificacion ->
                        TarjetaNotificacion(
                            notificacion = notificacion,
                            onClick = {
                                viewModel.marcarComoLeida(notificacion.id)
                                onNotificacionClick(notificacion.tipo.valor, notificacion.relacionId ?: "")
                            },
                            onEliminar = { viewModel.eliminarNotificacion(notificacion.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaNotificacion(
    notificacion: Notificacion,
    onClick: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (notificacion.leida)
                MaterialTheme.colorScheme.surface
            else
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (notificacion.leida) 0.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Indicador visual de no leída
            if (!notificacion.leida) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.extraSmall
                        )
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            // Icono según tipo
            Icon(
                imageVector = when (notificacion.tipo) {
                    TipoNotificacion.SOLICITUD_NUEVA,
                    TipoNotificacion.SOLICITUD_ACEPTADA,
                    TipoNotificacion.SOLICITUD_RECHAZADA -> Icons.Default.Email
                    TipoNotificacion.MENSAJE_NUEVO -> Icons.Default.Message
                    TipoNotificacion.OFERTA_VERIFICADA,
                    TipoNotificacion.OFERTA_RECHAZADA -> Icons.Default.CheckCircle
                    TipoNotificacion.SERVICIO_COMPLETADO -> Icons.Default.Check
                    TipoNotificacion.MODERACION_ALERTA -> Icons.Default.Warning
                },
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = when (notificacion.tipo) {
                    TipoNotificacion.SOLICITUD_NUEVA -> MaterialTheme.colorScheme.primary
                    TipoNotificacion.SOLICITUD_ACEPTADA -> MaterialTheme.colorScheme.primary
                    TipoNotificacion.SOLICITUD_RECHAZADA -> MaterialTheme.colorScheme.error
                    TipoNotificacion.OFERTA_VERIFICADA -> MaterialTheme.colorScheme.primary
                    TipoNotificacion.OFERTA_RECHAZADA -> MaterialTheme.colorScheme.error
                    TipoNotificacion.MODERACION_ALERTA -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Contenido
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    notificacion.titulo,
                    fontWeight = if (notificacion.leida) FontWeight.Normal else FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    notificacion.mensaje,
                    fontSize = 12.sp,
                    maxLines = 2,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    notificacion.fechaFormateada,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Botón eliminar
            IconButton(onClick = onEliminar) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = stringResource(R.string.eliminar),
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}