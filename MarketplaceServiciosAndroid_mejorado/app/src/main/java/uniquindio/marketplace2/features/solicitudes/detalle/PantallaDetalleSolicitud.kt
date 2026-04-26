package uniquindio.marketplace2.features.solicitudes.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uniquindio.marketplace2.R
import uniquindio.marketplace2.features.solicitudes.SolicitudViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleSolicitud(
    solicitudId: String,
    rolUsuario: String,
    onBackPressed: () -> Unit,
    viewModel: SolicitudViewModel = hiltViewModel()
) {

    val solicitudes by viewModel.solicitudes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(solicitudId) {
        viewModel.cargarSolicitudesDelTrabajador("2")
    }

    val solicitud = solicitudes.find { it.id == solicitudId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.detalle_solicitud_titulo)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.volver))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        if (solicitud == null) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Error, null, modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(12.dp))
                    Text(stringResource(R.string.error_general))
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onBackPressed) { Text(stringResource(R.string.volver)) }
                }
            }
            return@Scaffold
        }

        val (estadoColor, estadoLabel) = when (solicitud.estado) {
            "pendiente" -> Pair(Color(0xFFD97706), "Pendiente")
            "aceptada"  -> Pair(Color(0xFF16A34A), "Aceptada")
            "completada"-> Pair(Color(0xFF2563EB), "Completada")
            "rechazada" -> Pair(Color(0xFFDC2626), "Rechazada")
            else        -> Pair(Color(0xFF6B7280), solicitud.estado)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Estado banner
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(estadoColor.copy(alpha = 0.12f))
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        when (solicitud.estado) {
                            "pendiente" -> Icons.Default.Schedule
                            "aceptada"  -> Icons.Default.CheckCircle
                            "completada"-> Icons.Default.CheckCircle
                            else        -> Icons.Default.Cancel
                        },
                        null, tint = estadoColor, modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(estadoLabel, color = estadoColor, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                // Tarjeta de datos
                Card(elevation = CardDefaults.cardElevation(2.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        InfoRow(Icons.Default.Build, stringResource(R.string.detalle_solicitud_servicio), solicitud.ofertaTitulo)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        InfoRow(Icons.Default.Person, stringResource(R.string.detalle_solicitud_cliente), solicitud.clienteNombre)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        InfoRow(Icons.Default.CalendarToday, stringResource(R.string.detalle_solicitud_fecha), solicitud.fechaPreferida)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        InfoRow(Icons.Default.Schedule, "Fecha de solicitud", solicitud.fechaSolicitud)
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Mensaje
                Text(stringResource(R.string.detalle_solicitud_mensaje), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        solicitud.mensaje,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Acciones según rol y estado
                if (rolUsuario == "trabajador" && solicitud.estado == "pendiente") {
                    Spacer(Modifier.height(24.dp))
                    Text(stringResource(R.string.detalle_solicitud_acciones), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick = {
                                viewModel.cambiarEstado(solicitud.id, "rechazada")
                                onBackPressed()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.Close, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(stringResource(R.string.detalle_solicitud_rechazar))
                        }
                        Button(
                            onClick = {
                                viewModel.cambiarEstado(solicitud.id, "aceptada")
                                onBackPressed()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF16A34A))
                        ) {
                            Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(stringResource(R.string.detalle_solicitud_aceptar))
                        }
                    }
                }

                if (rolUsuario == "trabajador" && solicitud.estado == "aceptada") {
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = {
                            viewModel.cambiarEstado(solicitud.id, "completada")
                            onBackPressed()
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.detalle_solicitud_completar), fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}
